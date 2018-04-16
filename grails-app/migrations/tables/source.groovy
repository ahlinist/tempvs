package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-22") {
        createTable(tableName: "source") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "sourcePK")
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

            column(name: "source_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "period", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "item_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(2000)")
        }
    }
}
