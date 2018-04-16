databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-70") {
        addForeignKeyConstraint(baseColumnNames: "comment_id", baseTableName: "passport_comment", constraintName: "FKyyu8k8oa59y5eg4pdonqyu2h", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "comment")
    }
}
