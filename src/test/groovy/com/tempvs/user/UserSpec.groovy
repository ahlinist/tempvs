package com.tempvs.user

import com.tempvs.item.ItemGroup
import grails.test.mixin.TestFor
import spock.lang.Specification

import static com.tempvs.tests.utils.TestingUtils.DEFAULT_USER_PROPS

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(User)
class UserSpec extends Specification {

    def itemGroup = Mock ItemGroup
    def clubProfile = Mock ClubProfile

    def userProfile = Mock(UserProfile)

    def userService = Mock(UserService) {
        isEmailUnique(_ as String) >> Boolean.TRUE
    }

    User user

    def setup() {
        GroovySpy(ItemGroup, global: true)
        GroovySpy(ClubProfile, global: true)

        Map params = DEFAULT_USER_PROPS.clone()
        user = new User(params)
        user.userService = userService
        user.userProfile = userProfile
    }

    def cleanup() {
    }

    void "User without password has not been created"() {
        given:
        user.password = null

        expect:
        !user.validate()
    }

    void "User without email has not been created"() {
        given:
        user.email = null

        expect:
        !user.validate()
    }

    void "User created"() {
        expect:
        user.validate()
    }

    void "Users with only unique emails are saved"() {
        given:
        def userService = Mock(UserService) {
            isEmailUnique(_ as String) >> Boolean.FALSE
        }

        user.userService = userService

        expect:
        !user.validate()
    }

    void "Test getItemGroups()"() {
        when:
        def result = user.getItemGroups()

        then:
        1 * ItemGroup.findAllByUser(_ as User) >> [itemGroup]
        0 * _

        and:
        result == [itemGroup]
    }

    void "Test getClubProfiles()"() {
        when:
        def result = user.getClubProfiles()

        then:
        1 * ClubProfile.findAllByUser(_ as User) >> [clubProfile]
        0 * _

        and:
        result == [clubProfile]
    }
}
