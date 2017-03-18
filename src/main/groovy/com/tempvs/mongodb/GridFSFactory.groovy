package com.tempvs.mongodb

import com.mongodb.DB
import com.mongodb.Mongo
import com.mongodb.gridfs.GridFS
import grails.config.Config
import grails.util.Holders

class GridFSFactory {
    private static final Config config = Holders.grailsApplication.config
    private static final String host = config.getProperty('grails.mongodb.host', "localhost")
    private static final int port = config.getProperty('grails.mongodb.port', "27017").toInteger()
    private static final String databaseName = config.getProperty('grails.mongodb.databaseName', "tempvs")

    DB mongoDB = new DB(new Mongo(host, port), databaseName)

    GridFS getGridFS(String collection) {
        new GridFS(mongoDB, collection)
    }
}
