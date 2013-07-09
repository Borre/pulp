Map verticals = ["A":"cars", "B":"banks", "H":"hotels", "L": "airlines", "O":"others", "T":"stores"]

File file = new File("source/GraphValues__Accum-Vertical.csv")

List<String> lines = file.readLines()

println("We will process the file into a classes.")

List<Line> lineList = []

Integer count = 0

lines.each { line ->
	List<String> slitted = line.split(',')
	lineList << new Line(accountNumber: slitted.get(0), industryType: verticals.find {vertical -> vertical?.key == slitted.get(1)}?.value, kmps: slitted.get(2))
	slitted.removeAll()
	if (count % 100000 == 0) println("Processed : " + count)
	count++
}

println("Total lines: " + lines.size())

lines.removeAll()

println("Now we will group the objects and turn them into a list of sql statements.")

def groupedCommandObjects = lineList.groupBy { it.accountNumber }

println("Total independent accounts: " + groupedCommandObjects.size())

lineList.removeAll()

String queryHead = "INSERT INTO ranking (autos, bancos, date_created, hoteles, lineas_aereas, num_socio, otros, tiendas) VALUES "

count = 0

List<String> bufferToInsert = []

File destiny = new File("processedFiles/acumulation.sql")

for (currentUser in groupedCommandObjects) {

	Accumulation object = new Accumulation()

	object.accountNumber = currentUser.key

	currentUser.value.each {object?."${it?.industryType}" = it.kmps }

	object != null ? bufferToInsert.add(object.toString()) : println("Something wrong in this iteration")
}

bufferToInsert.collate(1000).each { listOfInserts ->
	destiny.append(queryHead)
	destiny.append(listOfInserts.join(','))
	destiny.append("; \n\n\n")
}

println("Records inserted: " + bufferToInsert.size())

println("This is the end my friend :D")

class Line {
    String accountNumber
    String industryType
    String kmps
}

class Accumulation {
    String accountNumber
    String cars = null
    String banks = null
    String hotels = null
    String airlines = null
    String others = null
    String stores = null

    String currentDate = (new Date() - 1).clearTime().format("yyyy-MM").toString() + "-01 00:00:00"

    String toString() {
        return "($cars, $banks, '$currentDate', $hotels, $airlines, $accountNumber, $others, $stores)"
    }
}
