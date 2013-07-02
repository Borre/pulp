#!/usr/bin/env groovy
List<String> filesToRead = 	[
        "Emails_Jul1_1.csv",
        "Emails_Jul1_2.csv",
        "Emails_Jul1_3.csv"
]
List<Users> target = []
filesToRead.each { fileName ->
    new File("files/" + fileName).eachLine { line ->
        List<String> user = line.split(/(,)/)
        if(target.find { it.account == formatData(user[0]) } == null) {
            target << new Users(account: formatData(user[0]).toInteger(), email: formatData(user[1]))
        }
        return null
    }
}

File targetFile = new File("target.sql")
targetFile.append("TRUNCATE TABLE group_user_emails; INSERT INTO group_user_emails (version, account, can_receive, email) VALUES ")
target.each { user ->
    Integer account = formatData(user.account as String).toInteger()
    String email = formatData(user.email as String)
    targetFile.append("(0, $account, 1, '$email'),")
}

public String formatData(String data) {
    return data != null || data != "" ? data.replaceAll("\"","") : ""
}

class Users {
    Integer account
    String email
}