package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-12") {
        createTable(tableName: "item2passport") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "item2passportPK")
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

            column(name: "item_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "quantity", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "passport_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-41") {
        addUniqueConstraint(columnNames: "passport_id, item_id", constraintName: "UK6b2df26b03b2b985bf3c4698f396", tableName: "item2passport")
    }
}
