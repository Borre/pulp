#!/usr/bin/env groovy
List<String> inserts = []

Integer counter = 0

println("Dummy starts")

new File("source/users_today.csv").eachLine { line ->

    List<String> contests = [
            "Identificador1",
            "Identificador2",
            "Identificador3",
            "Identificador4",
            "Identificador5",
            "Identificador6",
            "Identificador7"
    ]

    String date = "2013-09-27 00:00:00"

    String account = line.replaceAll('"', '')

    contests.each { contest ->

        inserts.add("('$contest', 100, '$date', $account)")

        counter++
    }

}

String insertHead = "INSERT INTO groups_rankings_users (identifier, kmps, transaction, user_id) VALUES "

String insertInFile = inserts.join(' , ')

String finalString = insertHead + insertInFile + ";"

File destinyFile = new File("processedFiles/groupRankingsDummyInsert.sql")

destinyFile.append(finalString)

println("Done. \nTotal inserts: $counter.")
