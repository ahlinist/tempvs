databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1543266068746-1") {
        addColumn(tableName: "user") {
            column(name: "time_zone", type: "varchar(255)")
        }
    }
}
