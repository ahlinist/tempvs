databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-52") {
        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role", constraintName: "FKa68196081fvovjhkek5m97n3y", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-57") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role", constraintName: "FKfgsgxvihks805qcq8sq26ab7c", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }
}
