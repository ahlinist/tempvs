databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1525525219892-2") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "role_request", constraintName: "FK9ve684xlarq9w37wj1fxgqtn7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "albvs (generated)", id: "1525525219892-3") {
        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "role_request", constraintName: "FKq3pmcnl1x9ymtr4ypdboi0lv3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role")
    }
}
