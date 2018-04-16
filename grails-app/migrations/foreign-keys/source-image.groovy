databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-55") {
        addForeignKeyConstraint(baseColumnNames: "image_id", baseTableName: "source_image", constraintName: "FKeu8sa8tipk8chkldwqq0lmtln", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "image")
    }
}
