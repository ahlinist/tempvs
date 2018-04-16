package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-5") {
        createTable(tableName: "acl_sid") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "acl_sidPK")
            }

            column(name: "sid", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "principal", type: "BOOLEAN") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-39") {
        addUniqueConstraint(columnNames: "sid, principal", constraintName: "UK1781b9a084dff171b580608b3640", tableName: "acl_sid")
    }
}
