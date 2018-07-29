package club.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class UserServiceSpec extends Specification implements ServiceUnitTest<UserService>, DomainUnitTest<User> {

    private static final Long LONG_ONE = 1L
    private static final String EMAIL = 'email'
    private static final String FIELD_VALUE = 'fieldValue'

    def user = Mock User
    def profile = Mock Profile
    def profileService = Mock ProfileService
    def springSecurityService = Mock SpringSecurityService

    def setup() {
        service.profileService = profileService
        service.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Test register()"() {
        when:
        def result = service.register(user, profile)

        then:
        1 * user.email >> EMAIL
        1 * profileService.getProfilesByProfileEmail(EMAIL)
        1 * user.addToProfiles(profile)
        1 * user.save() >> user
        0 * _

        and:
        result == user
    }

    void "Test editUserField()"() {
        when:
        def result = service.editUserField(user, EMAIL, FIELD_VALUE)

        then:
        1 * user.id >> LONG_ONE
        1 * profileService.getProfilesByProfileEmail(FIELD_VALUE)
        1 * user.setEmail(FIELD_VALUE)
        1 * user.save() >> user
        0 * _

        and:
        result == user
    }

    void "Test isEmailUnique()"() {
        when:
        def result = service.isEmailUnique(EMAIL)

        then:
        1 * profileService.getProfilesByProfileEmail(EMAIL)
        0 * _

        and:
        result == Boolean.TRUE
    }
}
