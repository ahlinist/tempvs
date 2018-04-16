package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-20") {
        createTable(tableName: "request_map") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "request_mapPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "http_method", type: "VARCHAR(255)")

            column(name: "config_attribute", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "url", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-44") {
        addUniqueConstraint(columnNames: "http_method, url", constraintName: "UKf721bf1f2340334e273dd57aedcb", tableName: "request_map")
    }
}
