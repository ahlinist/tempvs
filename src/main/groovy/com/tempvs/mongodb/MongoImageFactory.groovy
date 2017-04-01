package com.tempvs.mongodb

import com.mongodb.gridfs.GridFSFile
import com.tempvs.image.Image
import groovy.transform.CompileStatic

@CompileStatic
class MongoImageFactory {
    Image createInstance(GridFSFile fridFSFile) {
        new MongoImage(fridFSFile)
    }
}
