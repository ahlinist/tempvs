package com.tempvs.services

import com.mongodb.BasicDBObject
import com.mongodb.DB
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSFile
import com.mongodb.gridfs.GridFSInputFile

class FileDAOService {
    DB mongoDB

    Boolean save(GridFSFile gridFSFile, Map metaData = null) {
        if (gridFSFile) {
            gridFSFile.metaData = new BasicDBObject(metaData)
            gridFSFile.save()
            Boolean.TRUE
        } else {
            Boolean.FALSE
        }
    }

    GridFSFile get(String collection, Map query) {
        new GridFS(mongoDB, collection).findOne(new BasicDBObject(query))
    }

    GridFSFile create(InputStream inputStream, String collection, String fileName) {
        new GridFSInputFile(new GridFS(mongoDB, collection), inputStream, fileName, Boolean.TRUE)
    }
}
