package com.tempvs.domain

import groovy.transform.CompileStatic

@CompileStatic
abstract class BasePersistent {
    Date dateCreated
    Date lastUpdated
}
