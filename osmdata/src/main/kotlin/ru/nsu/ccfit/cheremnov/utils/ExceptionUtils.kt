package ru.nsu.ccfit.cheremnov.utils

fun Throwable.printMessageTrace(indent: String = "\t") {
    printMessageTrace("", indent)
}

private fun Throwable.printMessageTrace(collectedIndent: String, indent: String) {
    if (cause == null) {
        System.err.println("${collectedIndent}${localizedMessage}")
    } else {
        System.err.println("${collectedIndent}${localizedMessage}. Cause:")
        cause!!.printMessageTrace("${collectedIndent}${indent}", indent)
    }
}
