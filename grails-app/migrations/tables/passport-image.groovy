package tables

databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-19") {
        createTable(tableName: "passport_image") {
            column(name: "passport_images_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "image_id", type: "BIGINT")

            column(name: "images_idx", type: "INT")
        }
    }
}
