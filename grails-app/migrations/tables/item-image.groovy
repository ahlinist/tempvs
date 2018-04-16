package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-16") {
        createTable(tableName: "item_image") {
            column(name: "item_images_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "image_id", type: "BIGINT")

            column(name: "images_idx", type: "INT")
        }
    }
}
