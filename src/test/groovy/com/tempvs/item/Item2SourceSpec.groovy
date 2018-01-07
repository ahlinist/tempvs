package com.tempvs.item

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class Item2SourceSpec extends Specification implements DomainUnitTest<Item2Source> {

    def item = Mock Item
    def source = Mock Source

    def setup() {
    }

    def cleanup() {
    }

    void "Test Item2Source creation"() {
        expect:
        !domain.validate()

        when:
        domain.item = item
        domain.source = source

        then:
        domain.validate()
    }
}
