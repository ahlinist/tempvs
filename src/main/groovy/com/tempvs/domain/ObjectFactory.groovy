package com.tempvs.domain

import groovy.transform.CompileStatic

/**
 * Factory for domain object instances creation.
 */
@CompileStatic
class ObjectFactory {
    public <T> T create(Class<T> clazz) {
        clazz.newInstance()
    }

    public <T> T create(Class<T> clazz, Object[] args) {
        clazz.newInstance(args)
    }
}
