databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-59") {
        addForeignKeyConstraint(baseColumnNames: "club_profile_id", baseTableName: "passport", constraintName: "FKhmv85e2axjr1dh6u0yk1nl3rb", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "club_profile")
    }
}
