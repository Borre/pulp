/**
 * Generación de historico de posiciones.
 *
 * Uso:  Cargar ultimo archivo de acomulación dentro de la base de datos en la tabla de rankings utilizando el script de "BarItalia"
 * posteriormente generar por división los resultados de los concursos.
 */
@Grab('mysql:mysql-connector-java:5.1.6')
@GrabConfig(systemClassLoader = true)
import groovy.sql.Sql
import groovy.xml.XmlUtil

Integer period = 26

List<Groups> groupsList = []
List<Users> usersList = []
List<Rankings> rankingsList = []
List<String> contests = [
        "Identificador1",
        "Identificador2",
        "Identificador3",
        "Identificador4",
        "Identificador5",
        "Identificador6",
        "Identificador7"
]

Sql sql = Sql.newInstance("jdbc:mysql://127.0.0.1:3306/member", "root", "panda")

sql.eachRow("SELECT id, group_name, group_division_id FROM member.groups WHERE group_period = $period") {
    groupsList << new Groups(id: it.id, groupName: it.group_name, divisionId: it.group_division_id)
}

sql.eachRow("SELECT user_id, group_id FROM group_users WHERE period_id = $period") {
    usersList << new Users(userId: it.user_id, groupId: it.group_id)
}

sql.eachRow("SELECT user_id, kmps, identifier FROM groups_rankings_users") { rank ->
    rankingsList << new Rankings(userId: rank.user_id, kmps: rank.kmps, identifier: rank.identifier)
}

groupsList.each { group ->
    group.users = usersList.findAll { it.groupId == group.id }
}

contests.each { contest ->
    List<Groups> groupsKmps = []

    groupsList.each { group ->
        group.kmps = 0
        List<Rankings> identifierKmps = rankingsList.findAll { it.identifier == contest }
        group.users.each { user ->
            Rankings userkmps = identifierKmps.find { it.userId == user.userId }
            group.kmps += userkmps ? userkmps.kmps : 0
        }
        groupsKmps << group
    }

    Integer counter = 0
    println(contest + " division 3")
    printHeads()
    groupsKmps.findAll { it.divisionId == 3 }.sort { -it.kmps }.each { groupFinal ->
        counter++
        String groupName = XmlUtil.escapeXml(groupFinal.groupName)
        println("<tr><td>$counter</td><td>$groupName</td></tr>")
    }
    printFoot()
}

void printHeads() {
    println("<table class=\"\"contests table1 table table-condensed table-hover subastas-sidebar-table\"\">" +
            "<thead><tr><th>RANKING</th><th>EQUIPO</th></tr></thead><tbody>")
}

void printFoot() {
    println("</tbody>" +
            "</table>")
}

class Groups {
    Integer id
    String groupName
    Integer divisionId
    Integer kmps = 0
    List<Users> users = []
}

class Users {
    String userId
    Integer groupId
}

class Rankings {
    String userId
    Integer kmps
    String identifier
}

