package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-4") {
        createTable(tableName: "acl_object_identity") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "acl_object_identityPK")
            }

            column(name: "object_id_identity", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "entries_inheriting", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "object_id_class", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "owner_sid", type: "BIGINT")

            column(name: "parent_object", type: "BIGINT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-40") {
        addUniqueConstraint(columnNames: "object_id_class, object_id_identity", constraintName: "UK56103a82abb455394f8c97a95587", tableName: "acl_object_identity")
    }
}
