package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-27") {
        createTable(tableName: "user_role") {
            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "role_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-30") {
        addPrimaryKey(columnNames: "user_id, role_id", constraintName: "user_rolePK", tableName: "user_role")
    }
}
