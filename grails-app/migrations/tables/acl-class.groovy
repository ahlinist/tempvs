package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-2") {
        createTable(tableName: "acl_class") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "acl_classPK")
            }

            column(name: "class", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-31") {
        addUniqueConstraint(columnNames: "class", constraintName: "UC_ACL_CLASSCLASS_COL", tableName: "acl_class")
    }
}
