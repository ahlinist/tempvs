package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-13") {
        createTable(tableName: "item2source") {
            column(name: "item_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "source_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-29") {
        addPrimaryKey(columnNames: "item_id, source_id", constraintName: "item2sourcePK", tableName: "item2source")
    }
}
