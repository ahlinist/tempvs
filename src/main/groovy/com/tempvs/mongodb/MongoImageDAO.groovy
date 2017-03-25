package com.tempvs.mongodb

import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.image.Image

class MongoImageDAO {
    def gridFSFactory
    def dBObjectFactory
    def imageFactory

    private static final Boolean CLOSE_STREAM_ON_PERSIST = Boolean.TRUE

    Boolean save(Image image, Map metaData = null) {
        try {
            if (metaData) {
                image.metaData = dBObjectFactory.createInstance(metaData)
            }

            image.save()

            Boolean.TRUE
        } catch (Exception e) {
            Boolean.FALSE
        }
    }

    Image get(String collection, Map query) {
        GridFS gridFS = gridFSFactory.getGridFS(collection)
        GridFSDBFile gridFSDBFile = gridFS.findOne(dBObjectFactory.createInstance(query))
        imageFactory.createInstance(gridFSDBFile)
    }

    Image create(InputStream inputStream, String collection, String fileName) {
        GridFS gridFS = gridFSFactory.getGridFS(collection)
        GridFSInputFile gridFSInputFile = gridFS.createFile(inputStream, fileName, CLOSE_STREAM_ON_PERSIST)
        imageFactory.createInstance(gridFSInputFile)
    }
}
