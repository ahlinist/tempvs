databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-56") {
        addForeignKeyConstraint(baseColumnNames: "image_id", baseTableName: "item_image", constraintName: "FKffekuuetvxc58mlha2e9i3tj5", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "image")
    }
}
