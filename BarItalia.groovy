#!/usr/bin/env groovy
String fileName = "GruposAcu04242014.txt"
File rankingsFile = new File(fileName)
List<String> fileLines = rankingsFile ? rankingsFile.readLines() : []
File destiny = new File("inserts_dummy.sql")
destiny.append("INSERT INTO groups_rankings_users (user_id, kmps, transaction, identifier, version) VALUES ")
fileLines.each { line ->
    try {
        List<String> slitted = line.split(";")
        String identifier
        if(slitted[2] == 'Identificador5'|| slitted[2] == 'Identificador7'){
            identifier = 'Identificador6'
        }else if(slitted[2] == 'Identificador6'){
            identifier = 'Identificador5'
        }else{
            identifier = slitted[2]
        }
        String query = "(${slitted.get(0)}, ${slitted.get(1)}, '${"2014-04-26 00:00:00"}' , '${identifier}', 0),"
        destiny.append(query)
    } catch (e) {
        print("tehehe: " + e.toString())
    }
}
println("finish")
