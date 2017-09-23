package com.tempvs.domain

import com.tempvs.item.Item
import grails.test.mixin.TestFor
import org.hibernate.Criteria
import org.hibernate.Session
import org.hibernate.SessionFactory
import spock.lang.Specification
/**
 * Test suite for hibernate implementation of
 * {@link com.tempvs.domain.ObjectDAO} interface.
 */
@TestFor(ObjectDAOService)
class ObjectDAOServiceSpec extends Specification {

    private static final String ID = 'id'
    private static final String ONE = '1'
    private static final Long LONG_ONE = 1L

    def item = Mock(Item)
    def session = Mock(Session)
    def criteria = Mock(Criteria)
    def sessionFactory = Mock(SessionFactory)

    def setup() {
        service.sessionFactory = sessionFactory
    }

    def cleanup() {

    }

    void "Test get()"() {
        when:
        def result = service.get(Item, ONE)

        then:
        1 * sessionFactory.currentSession >> session
        1 * session.get(Item, LONG_ONE) >> item
        0 * _

        and:
        result == item
    }

    void "Test get() against invalid id"() {
        when:
        service.get(Item, ID)

        then:
        1 * sessionFactory.currentSession >> session
        0 * _
    }

    void "Test find()"() {
        given:
        Map params = [parameter: 'value']

        when:
        def result = service.find(Item, params)

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
        def result = service.findAll(Item, params)

        then:
        1 * sessionFactory.currentSession >> session
        1 * session.createCriteria(Item) >> criteria
        1 * criteria.add(_) >> criteria
        1 * criteria.list() >> [item]
        0 * _

        and:
        result == [item]
    }

    void "Test save()"() {
        when:
        def result = service.save(item)

        then:
        1 * item.save() >> item
        0 * _

        and:
        result == item
    }

    void "Test delete()"() {
        when:
        service.delete(item)

        then:
        1 * item.delete()
        0 * _
    }
}
