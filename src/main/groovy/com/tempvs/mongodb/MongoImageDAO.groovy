package com.tempvs.mongodb

import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.ImageBean
import com.tempvs.image.ImageDAO
import groovy.transform.CompileStatic
import org.bson.types.ObjectId

/**
 * A MongoDB implementation of {@link com.tempvs.image.ImageDAO} interface.
 */
@CompileStatic
class MongoImageDAO implements ImageDAO {

    GridFSFactory gridFSFactory
    ObjectFactory objectFactory

    private static final Boolean CLOSE_STREAM_ON_PERSIST = Boolean.TRUE

    ImageBean get(String collection, String objectId) {
        if (collection && objectId) {
            GridFS gridFS = gridFSFactory.getGridFS(collection)
            GridFSDBFile gridFSDBFile = gridFS.findOne(objectFactory.create(ObjectId, objectId))
            objectFactory.create(MongoImageBean, gridFSDBFile)
        }
    }

    ImageBean create(InputStream inputStream, String collection) {
        GridFS gridFS = gridFSFactory.getGridFS(collection)
        GridFSInputFile gridFSInputFile = gridFS.createFile(inputStream, CLOSE_STREAM_ON_PERSIST)
        objectFactory.create(MongoImageBean, gridFSInputFile).save()
    }

    Boolean delete(String collection, String objectId) {
        try {
            GridFS gridFS = gridFSFactory.getGridFS(collection)
            gridFS.remove(gridFS.findOne(objectFactory.create(ObjectId, objectId)))
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }
}
