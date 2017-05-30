package com.tempvs.domain.hibernate

import com.tempvs.domain.ObjectDAO
import groovy.transform.CompileStatic
import org.hibernate.Criteria
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions
/**
 * Hibernate implementation of database object retriever.
 */
@CompileStatic
class HibernateObjectDAO<T> implements ObjectDAO<T> {
    SessionFactory sessionFactory

    T get(Class clazz, Object id) {
        try {
            sessionFactory.currentSession.get(clazz, id as Long) as T
        } catch (NumberFormatException e) {
        }
    }

    T find(Class clazz, Map restrictions) {
        Criteria criteria = sessionFactory.currentSession.createCriteria(clazz)

        restrictions.each { fieldName, value ->
            criteria.add(Restrictions.eq(fieldName as String, value))
        }

        criteria.uniqueResult() as T
    }
}
