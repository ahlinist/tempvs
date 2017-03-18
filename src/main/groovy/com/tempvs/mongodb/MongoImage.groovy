package com.tempvs.mongodb

import com.tempvs.image.Image

class MongoImage implements Image {
    def gridFSFile

    Boolean save() {
        gridFSFile.save()
    }

    void setMetaData(Map metaData) {
        gridFSFile.metaData = metaData
    }

    List<Byte> getBytes() {
        gridFSFile?.inputStream?.bytes
    }
}
