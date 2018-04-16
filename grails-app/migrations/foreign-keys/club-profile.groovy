databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-47") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "club_profile", constraintName: "FK5y6d942owugpoycjt59bmxlpd", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "albvs (generated)", id: "1523810664995-49") {
        addForeignKeyConstraint(baseColumnNames: "avatar_id", baseTableName: "club_profile", constraintName: "FK6un39ggsb20886ttw182qymo6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "image")
    }
}