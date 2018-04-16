package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-15") {
        createTable(tableName: "item_group") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "item_groupPK")
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

            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(2000)")

            column(name: "item_groups_idx", type: "INT")
        }
    }
}
