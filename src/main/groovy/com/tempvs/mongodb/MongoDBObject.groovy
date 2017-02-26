package com.tempvs.mongodb

import com.mongodb.DB
import com.mongodb.Mongo
import grails.config.Config
import grails.util.Holders

class MongoDBObject extends DB {
    private static final Config config = Holders.grailsApplication.config
    private static final String host = config.getProperty('grails.mongodb.host', "localhost")
    private static final int port = config.getProperty('grails.mongodb.port', "27017").toInteger()
    private static final String databaseName = config.getProperty('grails.mongodb.databaseName', "tempvs")

    public MongoDBObject() {
        super(new Mongo(host, port), databaseName)
    }
}
