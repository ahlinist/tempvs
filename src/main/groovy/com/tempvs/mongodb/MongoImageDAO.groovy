package com.tempvs.mongodb

import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.image.Image
import com.tempvs.image.ImageDAO
import groovy.transform.CompileStatic
import org.bson.types.ObjectId

/**
 * A MongoDB implementation of {@link com.tempvs.image.ImageDAO} interface.
 */
@CompileStatic
class MongoImageDAO implements ImageDAO {
    GridFSFactory gridFSFactory
    DBObjectFactory dBObjectFactory
    MongoImageFactory imageFactory

    private static final Boolean CLOSE_STREAM_ON_PERSIST = Boolean.TRUE

    Image save(Image image, Map metaData = null) {
        try {
            if (metaData) {
                image.metaData = dBObjectFactory.createInstance(metaData)
            }

            image.save()
        } catch (Exception e) {
            null
        }
    }

    Image get(String collection, String id) {
        if (collection && id) {
            GridFS gridFS = gridFSFactory.getGridFS(collection)
            GridFSDBFile gridFSDBFile = gridFS.findOne(new ObjectId(id))
            imageFactory.createInstance(gridFSDBFile)
        }
    }

    Image create(InputStream inputStream, String collection) {
        GridFS gridFS = gridFSFactory.getGridFS(collection)
        GridFSInputFile gridFSInputFile = gridFS.createFile(inputStream, CLOSE_STREAM_ON_PERSIST)
        imageFactory.createInstance(gridFSInputFile)
    }

    Boolean delete(String collection, String id) {
        try {
            GridFS gridFS = gridFSFactory.getGridFS(collection)
            gridFS.remove(new ObjectId(id))
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }
}
