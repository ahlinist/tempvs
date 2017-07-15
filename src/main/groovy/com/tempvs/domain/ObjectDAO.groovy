package com.tempvs.domain

import groovy.transform.CompileStatic

/**
 * Object DAO interface.
 */
@CompileStatic
interface ObjectDAO<T> {
    T get(Class clazz, Object id)
    T find(Class clazz, Map restrictions)
    List<T> findAll(Class clazz, Map restrictions)
}
