databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-58") {
        addForeignKeyConstraint(baseColumnNames: "comment_id", baseTableName: "item_comment", constraintName: "FKfqcrpo3y4l5tsko8698ke0r4e", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "comment")
    }
}
