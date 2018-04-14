package club.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class UserServiceSpec extends Specification implements ServiceUnitTest<UserService>, DomainUnitTest<User> {

    private static final Long LONG_ID = 1L
    private static final String USER = 'user'
    private static final String EMAIL = 'email'
    private static final String PASSWORD = 'password'
    private static final String FIELD_VALUE = 'fieldValue'

    def user = Mock User
    def grailsUser = Mock GrailsUser
    def userProfile = Mock UserProfile
    def clubProfile = Mock ClubProfile
    def profileService = Mock ProfileService
    def springSecurityService = Mock SpringSecurityService

    def setup() {
        GroovySpy(User, global: true)

        service.profileService = profileService
        service.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Test getCurrentUser()"() {
        when:
        def result = service.getCurrentUser()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User) >> user
        0 * _

        result == user
    }

    void "Test getCurrentUserId()"() {
        when:
        def result = service.getCurrentUserId()

        then:
        1 * springSecurityService.currentUserId >> LONG_ID
        0 * _

        result == LONG_ID
    }

    void "Test getCurrentUserEmail()"() {
        when:
        def result = service.getCurrentUserEmail()

        then:
        1 * springSecurityService.loggedIn >> Boolean.TRUE
        1 * springSecurityService.principal >> grailsUser
        1 * grailsUser.username >> EMAIL
        0 * _

        result == EMAIL
    }

    void "Check getUserByEmail()"() {
        when:
        def result = service.getUserByEmail(EMAIL)

        then:
        1 * User.findByEmail(EMAIL) >> user
        0 * _

        and:
        result == user
    }

    void "Test editUserField()"() {
        when:
        def result = service.editUserField(user, EMAIL, FIELD_VALUE)

        then:
        1 * profileService.getProfileByProfileEmail(UserProfile, FIELD_VALUE)
        1 * profileService.getProfileByProfileEmail(ClubProfile, FIELD_VALUE)
        1 * user.setEmail(FIELD_VALUE)
        1 * user.save() >> user
        0 * _

        and:
        result == user
    }

    void "Test register()"() {
        when:
        def result = service.register(user, userProfile)

        then:
        1 * user.email >> EMAIL
        1 * profileService.getProfileByProfileEmail(UserProfile, EMAIL)
        1 * profileService.getProfileByProfileEmail(ClubProfile, EMAIL)
        1 * user.setUserProfile(userProfile)
        1 * user.save() >> user
        0 * _

        and:
        result == user
    }

    void "Test isEmailUnique()"() {
        when:
        def result = service.isEmailUnique(EMAIL)

        then:
        1 * profileService.getProfileByProfileEmail(UserProfile, EMAIL)
        1 * profileService.getProfileByProfileEmail(ClubProfile, EMAIL) >> clubProfile
        1 * clubProfile.user >> user
        1 * user.email >> EMAIL
        0 * _

        and:
        result == Boolean.TRUE
    }
}
