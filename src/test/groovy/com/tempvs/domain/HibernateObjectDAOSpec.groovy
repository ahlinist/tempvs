package com.tempvs.domain

import com.tempvs.domain.hibernate.HibernateObjectDAO
import com.tempvs.item.Item
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory
import spock.lang.Specification

/**
 * Test suite for hibernate implementation of
 * {@link com.tempvs.domain.ObjectDAO} interface.
 */
class HibernateObjectDAOSpec extends Specification {

    private static final String ID = 'id'
    private static final String ONE = '1'
    private static final Long LONG_ONE = 1L

    ObjectDAO objectDAO
    
    def item = Mock(Item)
    def session = Mock(Session)
    def criteria = Mock(Criteria)
    def sessionFactory = Mock(SessionFactory)

    def setup() {
        objectDAO = new HibernateObjectDAO(sessionFactory: sessionFactory)
    }

    def cleanup() {

    }

    void "Test get()"() {
        when:
        def result = objectDAO.get(Item, ONE)

        then:
        1 * sessionFactory.currentSession >> session
        1 * session.get(Item, LONG_ONE) >> item
        0 * _

        and:
        result == item
    }

    void "Test get() against invalid id"() {
        when:
        objectDAO.get(Item, ID)

        then:
        1 * sessionFactory.currentSession >> session
        0 * _
    }

    void "Test find()"() {
        given:
        Map params = [parameter: 'value']

        when:
        def result = objectDAO.find(Item, params)

        then:
        1 * sessionFactory.currentSession >> session
        1 * session.createCriteria(Item) >> criteria
        1 * criteria.add(_) >> criteria
        1 * criteria.uniqueResult() >> item
        0 * _

        and:
        result == item
    }

    void "Test findAll()"() {
        given:
        Map params = [parameter: 'value']

        when:
        def result = objectDAO.findAll(Item, params)

        then:
        1 * sessionFactory.currentSession >> session
        1 * session.createCriteria(Item) >> criteria
        1 * criteria.add(_) >> criteria
        1 * criteria.list() >> [item]
        0 * _

        and:
        result == [item]
    }
}
