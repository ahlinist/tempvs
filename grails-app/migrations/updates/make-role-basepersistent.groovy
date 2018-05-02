package updates

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1525288292291-1") {
        addColumn(tableName: "role") {
            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1525288292291-2") {
        addColumn(tableName: "role") {
            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }
        }
    }
}
