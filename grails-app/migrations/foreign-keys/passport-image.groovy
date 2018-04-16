databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-68") {
        addForeignKeyConstraint(baseColumnNames: "image_id", baseTableName: "passport_image", constraintName: "FKsiok32hg03dnuropo3meotkwm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "image")
    }
}
