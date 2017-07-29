package com.tempvs.mongodb

import com.mongodb.gridfs.GridFSFile
import com.tempvs.image.ImageBean
import groovy.transform.CompileStatic

/**
 * A factory that created {@link com.tempvs.mongodb.MongoImageBean} objects.
 */
@CompileStatic
class MongoImageBeanFactory {
    ImageBean createInstance(GridFSFile gridFSFile) {
        new MongoImageBean(gridFSFile)
    }
}
