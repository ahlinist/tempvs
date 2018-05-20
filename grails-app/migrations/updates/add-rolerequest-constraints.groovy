package updates

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1525591160372-1") {
        addUniqueConstraint(columnNames: "user_id, role_id", constraintName: "UKc8fa29d7d73775bc92c5795bc721", tableName: "role_request")
    }
}
