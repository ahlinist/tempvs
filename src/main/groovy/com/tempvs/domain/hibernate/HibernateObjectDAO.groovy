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
class HibernateObjectDAO implements ObjectDAO {
    SessionFactory sessionFactory

    public <T> T get(Class clazz, Object id) {
        try {
            sessionFactory.currentSession.get(clazz, id as Long) as T
        } catch (NumberFormatException e) {
        }
    }

    public <T> T find(Class clazz, Map restrictions) {
        createCriteria(clazz, restrictions).uniqueResult() as T
    }

    public <T> List<T> findAll(Class clazz, Map restrictions) {
        createCriteria(clazz, restrictions).list() as List<T>
    }

    private Criteria createCriteria(Class clazz, Map restrictions) {
        Criteria criteria = sessionFactory.currentSession.createCriteria(clazz)

        restrictions.each { fieldName, value ->
            criteria.add(Restrictions.eq(fieldName as String, value))
        }

        criteria
    }
}
