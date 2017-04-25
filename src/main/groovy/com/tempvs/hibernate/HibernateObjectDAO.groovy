package com.tempvs.hibernate

import groovy.transform.CompileStatic
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions

import com.tempvs.domain.ObjectDAO

/**
 * Hibernate implementation of database object retriever.
 */
@CompileStatic
class HibernateObjectDAO implements ObjectDAO {
    SessionFactory sessionFactory

    Object get(Class clazz, Object id) {
        Session session = sessionFactory.currentSession as Session

        try {
            session.get(clazz, id as Long)
        } catch (NumberFormatException e) {}
    }

    Object find(Class clazz, Map restrictions) {
        Session session = sessionFactory.currentSession as Session
        Criteria criteria = session.createCriteria(clazz)

        restrictions.each { fieldName, value ->
            criteria.add(Restrictions.eq(fieldName as String, value))
        }

        criteria.uniqueResult()
    }
}
