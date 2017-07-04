package com.tempvs.mongodb

import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.image.ImageBean
import com.tempvs.image.ImageBeanDAO
import groovy.transform.CompileStatic
import org.bson.types.ObjectId

/**
 * A MongoDB implementation of {@link com.tempvs.image.ImageBeanDAO} interface.
 */
@CompileStatic
class MongoImageBeanDAO implements ImageBeanDAO {
    GridFSFactory gridFSFactory
    DBObjectFactory dBObjectFactory
    MongoImageBeanFactory imageBeanFactory

    private static final Boolean CLOSE_STREAM_ON_PERSIST = Boolean.TRUE

    ImageBean save(ImageBean imageBean, Map metaData = null) {
        try {
            if (metaData) {
                imageBean.metaData = dBObjectFactory.createInstance(metaData)
            }

            imageBean.save()
        } catch (Exception e) {
            null
        }
    }

    ImageBean get(String collection, String id) {
        if (collection && id) {
            GridFS gridFS = gridFSFactory.getGridFS(collection)
            GridFSDBFile gridFSDBFile = gridFS.findOne(new ObjectId(id))
            imageBeanFactory.createInstance(gridFSDBFile)
        }
    }

    ImageBean create(InputStream inputStream, String collection) {
        GridFS gridFS = gridFSFactory.getGridFS(collection)
        GridFSInputFile gridFSInputFile = gridFS.createFile(inputStream, CLOSE_STREAM_ON_PERSIST)
        imageBeanFactory.createInstance(gridFSInputFile)
    }

    Boolean delete(String collection, Collection<String> objectIds) {
        try {
            GridFS gridFS = gridFSFactory.getGridFS(collection)

            objectIds?.each { String objectId ->
                gridFS.remove(gridFS.findOne(new ObjectId(objectId)))
            }

            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }
}
