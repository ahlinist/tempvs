databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-48") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "item_group", constraintName: "FK67urgr2etxe0afvgjkxxq3lo", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }
}
