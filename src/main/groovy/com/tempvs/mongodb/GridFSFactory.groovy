package com.tempvs.mongodb

import com.mongodb.DB
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.gridfs.GridFS
import groovy.transform.CompileStatic

@CompileStatic
class GridFSFactory {

    private static final String user = System.getenv'MONGO_USER'
    private static final String password = System.getenv'MONGO_PASS'
    private static final String host = System.getenv'MONGO_HOST'
    private static final String port = System.getenv'MONGO_PORT'
    private static final String databaseName = System.getenv'MONGO_DB_NAME'
    private static final String uriString = "mongodb://${user}:${password}@${host}:${port}/${databaseName}"

    DB mongoDB = new DB(new MongoClient(new MongoClientURI(uriString)), databaseName)

    GridFS getGridFS(String collection) {
        new GridFS(mongoDB, collection)
    }
}
