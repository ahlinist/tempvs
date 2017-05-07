package com.tempvs.domain

import groovy.transform.CompileStatic

/**
 * Factory for domain object instances creation.
 */
@CompileStatic
class ObjectFactory {
    Object create(Class clazz) {
        clazz.newInstance()
    }
}
