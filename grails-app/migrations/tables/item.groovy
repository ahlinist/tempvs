package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-11") {
        createTable(tableName: "item") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "itemPK")
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

            column(name: "name", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "period", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "item_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "item_group_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(2000)")

            column(name: "items_idx", type: "INT")
        }
    }
}
