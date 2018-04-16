databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-51") {
        addForeignKeyConstraint(baseColumnNames: "sid", baseTableName: "acl_entry", constraintName: "FK9r4mj8ewa904g3wivff0tb5b0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "acl_sid")
    }
}
