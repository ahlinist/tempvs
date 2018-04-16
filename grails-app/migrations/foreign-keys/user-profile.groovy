databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-64") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_profile", constraintName: "FKqcd5nmg7d7ement27tt9sf3bi", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-66") {
        addForeignKeyConstraint(baseColumnNames: "avatar_id", baseTableName: "user_profile", constraintName: "FKqyhgmwqnsk8h682f586o6244n", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "image")
    }
}
