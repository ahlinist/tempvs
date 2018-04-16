package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-26") {
        createTable(tableName: "user_profile") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "user_profilePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "profile_email", type: "VARCHAR(35)")

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "first_name", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "location", type: "VARCHAR(35)")

            column(name: "profile_id", type: "VARCHAR(35)")

            column(name: "active", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "last_name", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "avatar_id", type: "BIGINT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-37") {
        addUniqueConstraint(columnNames: "profile_email", constraintName: "UC_USER_PROFILEPROFILE_EMAIL_COL", tableName: "user_profile")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-38") {
        addUniqueConstraint(columnNames: "profile_id", constraintName: "UC_USER_PROFILEPROFILE_ID_COL", tableName: "user_profile")
    }
}
