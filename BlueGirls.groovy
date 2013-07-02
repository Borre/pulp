#!/usr/bin/env groovy
class BlueGirls {
	List<String> contests = [
		"Identificador1", 
		"Identificador2", 
		"Identificador3", 
		"Identificador4", 
		"Identificador5", 
		"Identificador6", 
		"Identificador7"
	]

	Integer counter = 0

	List<String> inserts = []

	void BlueGirls() {
		println("Dummy starts")
		new File("source/users.sql").eachLine { line ->
			insertCreator(line)
		}
	}

	void insertCreator(String inputText) {
		String date = "2013-07-02 00:00:00"
		String account = inputText.replaceAll('"', '')
		contests.each { contest ->
			inserts.add("('$contest', 100, '$date', account)")
			counter++
		}
	}

	void finalize() {
		String insertHead = "INSERT INTO groups_rankings_users (identifier, kmps, transaction, user_id) VALUES "
		String insertInFile = inserts.join(' , ')
		String finalString = insertHead + insertInFile[0..-2] + ";"
		File destinyFile = new File("processedFiles/groupRankingsDummyInsert.sql")
		destinyFile.append(finalString)
		println("Done. \n Total inserts: $counter.")
	}
}

new BlueGirls()
