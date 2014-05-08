#!/usr/bin/env groovy

/**
 * Todos los archivos a procecesar deben de estar en home y deben llamarse axis.log.
 */

File file = new File("/home/eduardo/axis.log")
String buffer = ""
Integer bufferCounter = 0
file.eachLine { line ->
    if (line == "=======================================================" && bufferCounter == 0) {
        bufferCounter++
    } else {
        if (line.startsWith("= Elapsed:")) {
            bufferCounter++
        } else {
            if (line.find("enroll")) {
                bufferCounter = 0
                println(line)
            }
        }
    }
}
