package club.tempvs.mongodb

import com.mongodb.DBObject
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSFile
import club.tempvs.image.ImageBean
import groovy.transform.CompileStatic

/**
 * A MongoDB implementation of {@link ImageBean} interface.
 */
@CompileStatic
class MongoImageBean implements ImageBean {
    GridFSFile gridFSFile

    MongoImageBean(GridFSFile gridFSFile) {
        this.gridFSFile = gridFSFile
    }

    MongoImageBean save() {
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
