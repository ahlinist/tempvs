package com.tempvs.domain

import grails.transaction.Transactional
import groovy.transform.CompileStatic
import org.hibernate.Criteria
import org.hibernate.SessionFactory
import org.hibernate.criterion.Restrictions

/**
 * A service to handle data access object operations.
 */
@Transactional
@CompileStatic
class ObjectDAOService {

    SessionFactory sessionFactory

    public <T> T create(Class<T> clazz) {
        clazz.newInstance()
    }

    public <T> T create(Class<T> clazz, Object[] args) {
        clazz.newInstance(args)
    }

    public <T> T get(Class clazz, Object id) {
        if (id) {
            try {
                sessionFactory.currentSession.get(clazz, id as Long) as T
            } catch (NumberFormatException e) {
            }
        }
    }

    public <T> T find(Class clazz, Map restrictions) {
        try {
            createCriteria(clazz, restrictions).uniqueResult() as T
        } catch (Throwable e) {
        }
    }

    public <T> List<T> findAll(Class clazz, Map restrictions) {
        createCriteria(clazz, restrictions).list() as List<T>
    }

    public <T> T save(BasePersistent object) {
        object.save()
        object as T
    }

    void delete(BasePersistent object) {
        object.delete()
    }

    private Criteria createCriteria(Class clazz, Map restrictions) {
        Criteria criteria = sessionFactory.currentSession.createCriteria(clazz)

        restrictions.each { fieldName, value ->
            criteria.add(Restrictions.eq(fieldName as String, value))
        }

        criteria
    }
}
