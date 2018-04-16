databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-46") {
        addForeignKeyConstraint(baseColumnNames: "parent_object", baseTableName: "acl_object_identity", constraintName: "FK4soxn7uid8qxltqps8kewftx7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "acl_object_identity")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-53") {
        addForeignKeyConstraint(baseColumnNames: "object_id_class", baseTableName: "acl_object_identity", constraintName: "FKc06nv93ck19el45a3g1p0e58w", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "acl_class")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-60") {
        addForeignKeyConstraint(baseColumnNames: "owner_sid", baseTableName: "acl_object_identity", constraintName: "FKikrbtok3aqlrp9wbq6slh9mcw", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "acl_sid")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-61") {
        addForeignKeyConstraint(baseColumnNames: "acl_object_identity", baseTableName: "acl_entry", constraintName: "FKl39t1oqikardwghegxe0wdcpt", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "acl_object_identity")
    }
}
