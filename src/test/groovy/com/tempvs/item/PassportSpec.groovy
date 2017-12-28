package com.tempvs.item

import com.tempvs.user.ClubProfile
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Passport)
class PassportSpec extends Specification {

    private static final String ITEM = 'item'
    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def item = Mock Item
    def clubProfile = Mock ClubProfile
    def item2Passport = Mock Item2Passport

    def setup() {
        GroovySpy(Item2Passport, global: true)
    }

    def cleanup() {
    }

    void "Test Passport creation"() {
        expect:
        !new Passport().validate()

        and:
        !new Passport(clubProfile: clubProfile).validate()

        and:
        !new Passport(clubProfile: clubProfile, description: DESCRIPTION).validate()

        and:
        new Passport(clubProfile: clubProfile, description: DESCRIPTION, name: NAME).validate()
    }

    void "Test getItems()"() {
        when:
        def result = domain.getItems()

        then:
        1 * Item2Passport.findAllByPassport(domain) >> [item2Passport]
        1 * item2Passport.getProperty(ITEM) >> item
        0 * _

        and:

        result == [item]
    }
}
