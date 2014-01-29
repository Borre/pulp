Integer args = this.args.size() > 0 ? this.args[0] : 0
Date initialDate = new Date().parse("yyyy-M-d H:m:s", "2014-01-02 00:00:00")
String insertHead = "INSERT INTO groups_rankings_users (user_id, kmps, transaction, identifier, version) VALUES "
String currentLocation = new File(".").getAbsolutePath().toString()[0..-2] + "processedFiles/"
println('Creating directory for output: ')
String currentFilesLocation = currentLocation + new Date().format("dd-MM-yyyy")
new File(currentFilesLocation + "/sqls").mkdirs()
Integer counter = 0;
println("Starting to create dump files :")
Date destinyDate = args != 0 ? new Date() - args : new Date()
(initialDate..destinyDate.clearTime()).each { date ->
    String fileName = "GruposAcu" + date.format("MMddyyyy") + ".txt"
    File rankingsFile = new File("source/" + fileName)
    List<String> fileLines = rankingsFile ? rankingsFile.readLines() : []
    File destinyFile = new File(currentFilesLocation + "/sqls/sql$counter" + ".sql")
    destinyFile.append(insertHead)
    fileLines.each { line ->
        try {
            List<String> slitted = line.split(";")
            String query = "(${slitted.get(0)}, ${slitted.get(1)}, '${date.clearTime().format("yyyy-MM-dd H:m:s")}', '${slitted.get(2)}', 0),"
            destinyFile.append(query)
        } catch (e) {
            print("tehehe: " + e.toString())
        }
    }
    counter++
    println("Done file number: " + counter + " -> " + fileName + " -> Date :" + new Date())
}
println('Done creating files proceeding to create dump:')

File insert = new File(currentFilesLocation + "/dump.sql");
File dir = new File(currentFilesLocation + "/sqls/")

insert.append('truncate groups_rankings_users;')
dir.eachFile {
    if (it.isFile()) {
        insert.append(it.text[0..-2] + "; \n")
        it.delete()
    }
}
dir.delete()
println("Done creating dump.")
