package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-10") {
        createTable(tableName: "image") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "imagePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "image_info", type: "VARCHAR(255)")

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "object_id", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "collection", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }
}
