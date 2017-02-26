package com.tempvs.services.mongodb

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSFile
import com.mongodb.gridfs.GridFSInputFile

class MongoDAOService {
    DB mongoDB
    private static final String METADATA = 'metadata'

    Boolean save(GridFSFile gridFSFile, Map metaData = null) {
        if (gridFSFile) {
            metaData?.each { propertyName, value ->
                gridFSFile.metaData = new BasicDBObject((String) propertyName, value)
            }

            gridFSFile.save()
            Boolean.TRUE
        } else {
            Boolean.FALSE
        }
    }

    GridFSFile get(String collection, Map metaData) {
        GridFS gridFS = new GridFS(mongoDB, collection)
        BasicDBObject query

        metaData.each { propertyName, value ->
            query = new BasicDBObject(METADATA, new BasicDBObject((String) propertyName, value));
        }

        gridFS.findOne(query)
    }

    GridFSFile create(InputStream inputStream, String collection, String fileName) {
        new GridFSInputFile(new GridFS(mongoDB, collection), inputStream, fileName, Boolean.TRUE)
    }
}
