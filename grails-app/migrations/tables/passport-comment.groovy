package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-18") {
        createTable(tableName: "passport_comment") {
            column(name: "passport_comments_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "comment_id", type: "BIGINT")

            column(name: "comments_idx", type: "INT")
        }
    }
}
