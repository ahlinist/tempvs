package com.tempvs.item

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class Item2PassportSpec extends Specification implements DomainUnitTest<Item2Passport> {

    def item = Mock Item
    def passport = Mock Passport

    def setup() {
    }

    def cleanup() {
    }

    void "Test Item2Passport creation"() {
        expect:
        !domain.validate()

        when:
        domain.item = item
        domain.passport = passport
        domain.quantity = 1L

        then:
        domain.validate()
    }

    void "Test Item2Passport quantity constraint"() {
        given:
        domain.item = item
        domain.passport = passport
        domain.quantity = -1L

        expect:
        !domain.validate()
    }
}
