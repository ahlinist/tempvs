package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-6") {
        createTable(tableName: "club_profile") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "club_profilePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "profile_email", type: "VARCHAR(35)")

            column(name: "club_name", type: "VARCHAR(35)")

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "nick_name", type: "VARCHAR(35)")

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

            column(name: "period", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "last_name", type: "VARCHAR(35)")

            column(name: "avatar_id", type: "BIGINT")

            column(name: "club_profiles_idx", type: "INT")
        }
    }


    changeSet(author: "albvs (generated)", id: "1523810664995-32") {
        addUniqueConstraint(columnNames: "profile_email", constraintName: "UC_CLUB_PROFILEPROFILE_EMAIL_COL", tableName: "club_profile")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-33") {
        addUniqueConstraint(columnNames: "profile_id", constraintName: "UC_CLUB_PROFILEPROFILE_ID_COL", tableName: "club_profile")
    }
}
