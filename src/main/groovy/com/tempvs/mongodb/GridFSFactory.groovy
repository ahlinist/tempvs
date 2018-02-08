package com.tempvs.mongodb

import com.mongodb.DB
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.gridfs.GridFS
import groovy.transform.CompileStatic

@CompileStatic
class GridFSFactory {

    private static final String MONGODB_URI = System.getenv 'MONGODB_URI'
    private static final DB mongoDB = new DB(new MongoClient(new MongoClientURI(MONGODB_URI)), MONGODB_URI.split('/').last())

    GridFS getGridFS(String collection) {
        new GridFS(mongoDB, collection)
    }
}
