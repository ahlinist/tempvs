package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-7") {
        createTable(tableName: "comment") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "commentPK")
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

            column(name: "text", type: "VARCHAR(2000)") {
                constraints(nullable: "false")
            }

            column(name: "club_profile_id", type: "BIGINT")

            column(name: "user_profile_id", type: "BIGINT")
        }
    }
}
