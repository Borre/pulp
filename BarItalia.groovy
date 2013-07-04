#!/usr/bin/env groovy
String fileName = "source/GruposAcu07032013.txt"
File rankingsFile = new File(fileName)
List<String> fileLines = rankingsFile ? rankingsFile.readLines() : []
File destiny = new File("inserts_dummy.sql")
destiny.append("INSERT INTO groups_rankings_users (user_id, kmps, transaction, identifier, version) VALUES ")
fileLines.each { line ->
    try {
        List<String> slitted = line.split(";")
        String query = "(${slitted.get(0)}, ${slitted.get(1)}, '${"2013-07-01 00:00:00"}', '${slitted.get(2)}', 0),"
        destiny.append(query)
    } catch (e) {
        print("tehehe: " + e.toString())
    }
}
println("finish")