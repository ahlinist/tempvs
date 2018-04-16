package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-8") {
        createTable(tableName: "email_verification") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "email_verificationPK")
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

            column(name: "instance_id", type: "BIGINT")

            column(name: "action", type: "VARCHAR(12)") {
                constraints(nullable: "false")
            }

            column(name: "email", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "verification_code", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-34") {
        addUniqueConstraint(columnNames: "verification_code", constraintName: "UC_EMAIL_VERIFICATIONVERIFICATION_CODE_COL", tableName: "email_verification")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-42") {
        addUniqueConstraint(columnNames: "action, email", constraintName: "UKb232bd49e5b8712edc8d6f1868a4", tableName: "email_verification")
    }
}
