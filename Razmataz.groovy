#!/usr/bin/env groovy
String output = ""
Integer counter = 0
def currentFile = new File("graficas.sql").eachLine { line ->
	Integer count = 0
	line.split(',').each {  param ->
		if(count == 0) {
			output = output + "("
		} else {
			output = output + param
		}
		count++
	}
	count = 0
}

def razmatazz = new File('razmatazz.sql')
razmatazz.append(output)
