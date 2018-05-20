databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1525862692627-1") {
        addColumn(tableName: "user_role") {
            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1525862692627-2") {
        addColumn(tableName: "user_role") {
            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }
        }
    }
}
