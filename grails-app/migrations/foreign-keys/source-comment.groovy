databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-62") {
        addForeignKeyConstraint(baseColumnNames: "comment_id", baseTableName: "source_comment", constraintName: "FKlef4yys4enjfcnngx65ldjwh", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "comment")
    }
}
