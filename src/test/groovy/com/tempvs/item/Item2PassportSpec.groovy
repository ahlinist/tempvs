package com.tempvs.item

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Item2Passport)
class Item2PassportSpec extends Specification {

    private static final Long DEFAULT_QUANTITY = 1

    def item = Mock Item
    def passport = Mock Passport

    def setup() {
    }

    def cleanup() {
    }

    void "Test Item2Passport creation"() {
        expect:
        !new Item2Passport().validate()

        and:
        !new Item2Passport(item: item).validate()

        and:
        !new Item2Passport(item: item, passport: passport).validate()

        and:
        new Item2Passport(item: item, passport: passport, quantity: DEFAULT_QUANTITY).validate()
    }

    void "Test item uniqueness within a passport"() {
        given:
        new Item2Passport(item: item, passport: passport, quantity: DEFAULT_QUANTITY).save(flush: true)

        expect:
        !new Item2Passport(item: item, passport: passport, quantity: DEFAULT_QUANTITY).save(flush: true)
    }
}
