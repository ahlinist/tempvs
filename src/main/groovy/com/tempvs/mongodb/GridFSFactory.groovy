package com.tempvs.mongodb

import com.mongodb.DB
import com.mongodb.Mongo
import com.mongodb.gridfs.GridFS

class GridFSFactory {
    private static final String host = 'localhost'
    private static final int port = 27017
    private static final String databaseName = 'tempvs'

    DB mongoDB = new DB(new Mongo(host, port), databaseName)

    GridFS getGridFS(String collection) {
        new GridFS(mongoDB, collection)
    }
}
