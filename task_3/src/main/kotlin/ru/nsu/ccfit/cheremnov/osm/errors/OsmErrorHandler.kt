package ru.nsu.ccfit.cheremnov.osm.errors

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.nsu.ccfit.cheremnov.osm.api.errors.CommonErrorCode
import ru.nsu.ccfit.cheremnov.osm.api.errors.OsmErrorResponse
import ru.nsu.ccfit.cheremnov.osm.api.errors.OsmSubError
import ru.nsu.ccfit.cheremnov.osm.api.errors.ValidationSubError
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest


@ControllerAdvice
class OsmErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<*> {
        val validationErrors = ex.bindingResult.allErrors.map {
            ValidationSubError(
                field = (it as FieldError).field,
                message = it.defaultMessage
            )
        }

        return buildResponseEntity(
            request = request,
            responseStatus = HttpStatus.BAD_REQUEST,
            errorCode = CommonErrorCode.VALIDATION_FAILED.name,
            message = "Validation failed",
            subErrors = validationErrors
        )
    }

    @ExceptionHandler(OsmException::class)
    fun handleOsmException(ex: OsmException, request: HttpServletRequest): ResponseEntity<*> {
        val responseStatus = when (ex) {
            is OsmAlreadyExistsException -> HttpStatus.CONFLICT
            is OsmNotFoundException -> HttpStatus.NOT_FOUND
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        
        return buildResponseEntity(
            request = request,
            responseStatus = responseStatus,
            errorCode = ex.errorCode,
            message = ex.message,
        )
    }

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(ex: Throwable, request: HttpServletRequest): ResponseEntity<*> {
        return buildResponseEntity(
            request = request,
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR,
            errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR.name
        )
    }

    private fun buildResponseEntity(
        request: HttpServletRequest,
        responseStatus: HttpStatus,
        errorCode: String,
        message: String? = null,
        subErrors: List<OsmSubError>? = null
    ): ResponseEntity<*> =
        ResponseEntity
            .status(responseStatus)
            .body(
                OsmErrorResponse(
                    path = request.requestURI,
                    errorCode = errorCode,
                    timestamp = LocalDateTime.now(),
                    message = message,
                    subErrors = subErrors
                )
            )

}