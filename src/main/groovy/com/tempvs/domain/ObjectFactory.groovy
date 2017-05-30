package com.tempvs.domain

import groovy.transform.CompileStatic

/**
 * Factory for domain object instances creation.
 */
@CompileStatic
class ObjectFactory<T> {
    T create(Class clazz) {
        clazz.newInstance()
    }
}
