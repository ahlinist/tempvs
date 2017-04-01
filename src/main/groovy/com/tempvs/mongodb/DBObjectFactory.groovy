package com.tempvs.mongodb

import com.mongodb.BasicDBObject
import groovy.transform.CompileStatic

@CompileStatic
class DBObjectFactory {
    BasicDBObject createInstance(Map query) {
        new BasicDBObject(query)
    }
}
