package com.tempvs.mongodb

import com.mongodb.gridfs.GridFSFile
import com.tempvs.image.Image

class MongoImage implements Image {
    def gridFSFile

    MongoImage(GridFSFile gridFSFile) {
        this.gridFSFile = gridFSFile
    }

    void save() {
        gridFSFile.save()
    }

    void setMetaData(Map metaData) {
        gridFSFile.metaData = metaData
    }

    List<Byte> getBytes() {
        gridFSFile.inputStream?.bytes
    }
}
