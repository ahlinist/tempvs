package com.tempvs.item

import com.tempvs.user.ClubProfile
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class PassportServiceSpec extends Specification implements ServiceUnitTest<PassportService>, DomainUnitTest<Passport> {

    private static final Long LONG_ID = 1L
    private static final String DESCRIPTION = 'description'
    private static final String FIELD_VALUE = 'fieldValue'

    def item = Mock Item
    def passport = Mock Passport
    def clubProfile = Mock ClubProfile

    def setup() {
        GroovySpy(Passport, global: true)
    }

    def cleanup() {
    }

    void "Test getPassport()"() {
        when:
        def result = service.getPassport(LONG_ID)

        then:
        1 * Passport.get(LONG_ID) >> passport
        0 * _

        and:
        result == passport
    }

    void "Test createPassport()"() {
        when:
        def result = service.createPassport(passport)

        then:
        1 * passport.save() >> passport
        0 * _

        and:
        result == passport
    }

    void "Test editPassportField()"() {
        when:
        def result = service.editPassportField(passport, DESCRIPTION, FIELD_VALUE)

        then:
        1 * passport.setProperty(DESCRIPTION, FIELD_VALUE)
        1 * passport.save()
        0 * _

        and:
        result == passport
    }

    void "Test addItem()"() {
        when:
        def result = service.addItem(passport, item)

        then:
        1 * passport.addToItems(item) >> passport
        1 * passport.save() >> passport
        0 * _

        and:
        result == passport
    }

    void "Test removeItem()"() {
        when:
        def result = service.removeItem(passport, item)

        then:
        1 * passport.removeFromItems(item)
        1 * passport.save()
        0 * _

        and:
        result == passport
    }

    void "Test deletePassport()"() {
        when:
        service.deletePassport(passport)

        then:
        1 * passport.delete()
        0 * _
    }
}
