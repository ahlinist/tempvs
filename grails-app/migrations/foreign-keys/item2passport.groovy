databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-45") {
        addForeignKeyConstraint(baseColumnNames: "passport_id", baseTableName: "item2passport", constraintName: "FK1t10y5yqkaadeb71bj4hrxxhg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "passport")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-50") {
        addForeignKeyConstraint(baseColumnNames: "item_id", baseTableName: "item2passport", constraintName: "FK8v1ebuj2ub3jk9j3pouey0h4g", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "item")
    }
}
