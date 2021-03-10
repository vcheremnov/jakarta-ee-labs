package ru.nsu.ccfit.cheremnov.exceptions

fun Throwable.printMessageTrace(indent: String = "") {
    if (cause == null) {
        System.err.println("${indent}${localizedMessage}")
    } else {
        System.err.println("${indent}${localizedMessage}. Cause:")
        cause!!.printMessageTrace("${indent}\t")
    }
}