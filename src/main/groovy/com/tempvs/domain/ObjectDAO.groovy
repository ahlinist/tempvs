package com.tempvs.domain

import groovy.transform.CompileStatic

/**
 * Object DAO interface.
 */
@CompileStatic
interface ObjectDAO {
    Object get(Class clazz, Object id)
    Object find(Class clazz, Map restrictions)
}
