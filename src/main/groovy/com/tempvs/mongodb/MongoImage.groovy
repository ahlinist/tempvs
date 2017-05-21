package com.tempvs.mongodb

import com.mongodb.DBObject
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSFile
import com.tempvs.image.Image
import groovy.transform.CompileStatic

/**
 * A MongoDB implementation of {@link com.tempvs.image.Image} interface.
 */
@CompileStatic
class MongoImage implements Image {
    GridFSFile gridFSFile

    MongoImage(GridFSFile gridFSFile) {
        this.gridFSFile = gridFSFile
    }

    MongoImage save() {
        gridFSFile.save()
        this
    }

    void setMetaData(Map metaData) {
        gridFSFile.metaData = metaData as DBObject
    }

    byte[] getBytes() {
        if (gridFSFile) {
            ((GridFSDBFile) gridFSFile).inputStream?.bytes
        }
    }

    String getId() {
        this.gridFSFile?.id
    }
}
