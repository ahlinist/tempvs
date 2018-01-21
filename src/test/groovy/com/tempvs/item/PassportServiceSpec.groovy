package com.tempvs.item

import com.tempvs.communication.Comment
import com.tempvs.user.ClubProfile
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class PassportServiceSpec extends Specification implements ServiceUnitTest<PassportService>, DomainUnitTest<Passport> {

    private static final Long LONG_ID = 1L
    private static final String DESCRIPTION = 'description'
    private static final String FIELD_VALUE = 'fieldValue'

    def item = Mock Item
    def comment = Mock Comment
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

    void "Test getItem2PassportRelations()"() {
        when:
        def result = service.getItem2PassportRelations(passport)

        then:
        1 * Item2Passport.findAllByPassport(passport) >> [item2Passport]
        0 * _

        and:
        result == [item2Passport]
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
        1 * passport.setDescription(FIELD_VALUE)
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
        1 * Item2Passport.findByItemAndPassport(item, passport) >> item2Passport
        1 * item2Passport.delete()
        0 * _
    }

    void "Test deletePassport()"() {
        when:
        service.deletePassport(passport)

        then:
        1 * Item2Passport.findByPassport(passport) >> item2Passport
        1 * item2Passport.delete()
        1 * passport.delete()
        0 * _
    }

    void "Test addComment()"() {
        when:
        def result = service.addComment(passport, comment)

        then:
        1 * passport.addToComments(comment) >> passport
        1 * passport.save()
        0 * _

        and:
        result == passport
    }

    void "Test deleteComment()"() {
        when:
        def result = service.deleteComment(passport, comment)

        then:
        1 * passport.removeFromComments(comment) >> passport
        1 * passport.save()
        0 * _

        and:
        result == passport
    }
}
