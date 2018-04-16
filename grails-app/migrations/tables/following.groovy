package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-9") {
        createTable(tableName: "following") {
            column(name: "profile_class_name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "follower_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "following_id", type: "BIGINT") {
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

            column(name: "is_new", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "period", type: "VARCHAR(255)")
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-28") {
        addPrimaryKey(columnNames: "profile_class_name, follower_id, following_id", constraintName: "followingPK", tableName: "following")
    }
}
