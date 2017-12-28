package com.tempvs.item

import com.tempvs.user.ClubProfile
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PassportService)
class PassportServiceSpec extends Specification {

    private static final Long LONG_ID = 1L
    private static final String DESCRIPTION = 'description'
    private static final String FIELD_VALUE = 'fieldValue'

    def item = Mock Item
    def passport = Mock Passport
    def clubProfile = Mock ClubProfile
    def item2Passport = Mock Item2Passport

    def setup() {
        GroovySpy(Passport, global: true)
        GroovySpy(Item2Passport, global: true)
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

    void "Test getPassportsByProfile()"() {
        when:
        def result = service.getPassportsByProfile(clubProfile)

        then:
        1 * Passport.findAllByClubProfile(clubProfile) >> [passport]
        0 * _

        and:
        result == [passport]
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
        def result = service.addItem(passport, item, 1)

        then:
        1 * new Item2Passport([item: item, passport: passport, quantity: 1]) >> item2Passport
        1 * item2Passport.save() >> item2Passport
        0 * _

        and:
        result == item2Passport
    }

    void "Test removeItem()"() {
        when:
        service.removeItem(passport, item)

        then:
        1 * Item2Passport.findByPassportAndItem(passport, item) >> item2Passport
        1 * item2Passport.delete()
        0 * _
    }

    void "Test deletePassport()"() {
        when:
        service.deletePassport(passport)

        then:
        1 * Item2Passport.findAllByPassport(passport) >> [item2Passport]
        1 * item2Passport.delete()
        1 * passport.delete()
        0 * _
    }
}
