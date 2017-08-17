package com.tempvs.domain

import groovy.transform.CompileStatic

/**
 * Object DAO interface.
 */
@CompileStatic
interface ObjectDAO {
    public <T> T get(Class clazz, Object id)
    public <T> T find(Class clazz, Map restrictions)
    public <T> List<T> findAll(Class clazz, Map restrictions)
}
