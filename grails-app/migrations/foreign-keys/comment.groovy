databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-54") {
        addForeignKeyConstraint(baseColumnNames: "club_profile_id", baseTableName: "comment", constraintName: "FKd8gq9g90ee6ewk6aw9e2se5u", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "club_profile")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-63") {
        addForeignKeyConstraint(baseColumnNames: "user_profile_id", baseTableName: "comment", constraintName: "FKnxgp01rkhaqkd8h8y3vgmttpn", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user_profile")
    }
}