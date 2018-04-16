package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-21") {
        createTable(tableName: "role") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "rolePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "authority", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-35") {
        addUniqueConstraint(columnNames: "authority", constraintName: "UC_ROLEAUTHORITY_COL", tableName: "role")
    }
}