package com.tempvs.mongodb

import com.mongodb.BasicDBObject
import com.mongodb.DBObject

class DBObjectFactory {
    DBObject createInstance(Map query) {
        new BasicDBObject(query)
    }
}
