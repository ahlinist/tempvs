package com.tempvs.domain

import groovy.transform.CompileStatic

@CompileStatic
abstract class BasePersistent extends BaseObject {
    Long id
    Date dateCreated
    Date lastUpdated
}
