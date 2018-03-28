package club.tempvs.mongodb

import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import club.tempvs.image.ImageBean
import club.tempvs.image.ImageDAO
import groovy.transform.CompileStatic
import org.bson.types.ObjectId

/**
 * A MongoDB implementation of {@link ImageDAO} interface.
 */
@CompileStatic
class MongoImageDAO implements ImageDAO {

    GridFSFactory gridFSFactory

    private static final Boolean CLOSE_STREAM_ON_PERSIST = Boolean.TRUE

    ImageBean get(String collection, String objectId) {
        if (collection && objectId) {
            GridFS gridFS = gridFSFactory.getGridFS(collection)
            GridFSDBFile gridFSDBFile = gridFS.findOne(new ObjectId(objectId))
            new MongoImageBean(gridFSDBFile)
        }
    }

    ImageBean create(InputStream inputStream, String collection) {
        GridFS gridFS = gridFSFactory.getGridFS(collection)
        GridFSInputFile gridFSInputFile = gridFS.createFile(inputStream, CLOSE_STREAM_ON_PERSIST)
        MongoImageBean mongoImageBean = new MongoImageBean(gridFSInputFile)
        mongoImageBean.save()
    }

    Boolean delete(String collection, String objectId) {
        try {
            GridFS gridFS = gridFSFactory.getGridFS(collection)
            gridFS.remove(gridFS.findOne(new ObjectId(objectId)))
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }
}
