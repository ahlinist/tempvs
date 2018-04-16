databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-67") {
        addForeignKeyConstraint(baseColumnNames: "item_group_id", baseTableName: "item", constraintName: "FKr4fbv7293k0b5v1qjk5lm6md", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "item_group")
    }
}
