package com.tempvs.item

import com.tempvs.periodization.Period
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Item2Source)
class Item2SourceSpec extends Specification {

    def period = Period.XIX

    def item = Mock(Item) {
        getPeriod() >> period
    }

    def source = Mock(Source) {
        getPeriod() >> period
    }

    def setup() {
    }

    def cleanup() {
    }

    void "Test Source nullability"() {
        expect:
        !new Item2Source(item: item).validate()
    }

    void "Test Item nullability"() {
        expect:
        !new Item2Source(source: source).validate()
    }

    void "Test Item and Source period equality"() {
        given:
        def wrongSource = Mock(Source) {
            getPeriod() >> Period.OTHER
        }

        expect:
        new Item2Source(item: item, source: source).save(flush: true)

        and:
        !new Item2Source(item: item, source: wrongSource).save(flush: true)
    }

    void "Test Item2Source uniqueness"() {
        expect:
        new Item2Source(item: item, source: source).save(flush: true)
        !new Item2Source(item: item, source: source).save(flush: true)
    }
}
