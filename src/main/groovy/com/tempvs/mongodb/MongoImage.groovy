package com.tempvs.mongodb

import com.mongodb.DBObject
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSFile
import com.tempvs.image.Image
import groovy.transform.CompileStatic

@CompileStatic
class MongoImage implements Image {
    GridFSFile gridFSFile

    MongoImage(GridFSFile gridFSFile) {
        this.gridFSFile = gridFSFile
    }

    void save() {
        gridFSFile.save()
    }

    void setMetaData(Map metaData) {
        gridFSFile.metaData = metaData as DBObject
    }

    byte[] getBytes() {
        ((GridFSDBFile) gridFSFile).inputStream?.bytes
    }
}
