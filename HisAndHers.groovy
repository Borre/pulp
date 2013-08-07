#!/usr/bin/env groovy
List<String> filesToRead = 	[
        "Emails_Jul11_1.csv",
        "Emails_Jul11_2.csv",
        "Emails_Jul11_3.csv"
]
List<Users> target = []
filesToRead.each { fileName ->
    new File("source/" + fileName).eachLine { line ->
        List<String> user = line.split(/(,)/)
        if(target.find { it.account == formatData(user[0]) } == null) {
            target << new Users(account: formatData(user[0]).toInteger(), email: formatData(user[1]))
        }
        return null
    }
}

File targetFile = new File("processedFiles/target.sql")
targetFile.append("TRUNCATE TABLE group_user_emails; INSERT INTO group_user_emails (version, account, can_receive, email) VALUES ")
target.each { user ->
    Integer account = formatData(user.account as String).toInteger()
    String email = formatData(user.email as String)
    targetFile.append("(0, $account, 1, '$email'),")
}

println("Done")

public String formatData(String data) {
    return data != null || data != "" ? data.replaceAll("\"","") : ""
}

class Users {
    Integer account
    String email
}