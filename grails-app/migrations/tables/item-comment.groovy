package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-14") {
        createTable(tableName: "item_comment") {
            column(name: "item_comments_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "comment_id", type: "BIGINT")

            column(name: "comments_idx", type: "INT")
        }
    }
}
