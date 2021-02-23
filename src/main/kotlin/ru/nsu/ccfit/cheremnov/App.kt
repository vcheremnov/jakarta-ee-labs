package ru.nsu.ccfit.cheremnov

import org.apache.logging.log4j.LogManager

class App() {
    private val logger = LogManager.getLogger()

    fun greetTheWorld() {
        logger.info("Preparing to greet the world...")
        println("Hello, world!")
        logger.info("Done!")
    }

}

fun main() {
    App().greetTheWorld()
}