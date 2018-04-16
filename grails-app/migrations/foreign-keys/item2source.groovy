databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-65") {
        addForeignKeyConstraint(baseColumnNames: "source_id", baseTableName: "item2source", constraintName: "FKqu63sppr2062vav0ky6rua1m", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "source")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-69") {
        addForeignKeyConstraint(baseColumnNames: "item_id", baseTableName: "item2source", constraintName: "FKtp4u7one6eah0pp4u5ul0ami6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "item")
    }
}
