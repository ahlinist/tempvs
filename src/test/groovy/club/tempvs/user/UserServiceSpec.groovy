package club.tempvs.user

import club.tempvs.object.ObjectFactory
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import org.springframework.security.core.GrantedAuthority
import spock.lang.Specification

class UserServiceSpec extends Specification implements ServiceUnitTest<UserService>, DomainUnitTest<User> {

    private static final String EMAIL = 'email'
    private static final String FIELD_VALUE = 'fieldValue'

    def json = Mock JSON
    def user = Mock User
    def grailsUser = Mock GrailsUser
    def grantedAuthority = Mock GrantedAuthority
    def profile = Mock Profile
    def verifyService = Mock VerifyService
    def profileService = Mock ProfileService
    def emailVerification = Mock EmailVerification
    def springSecurityService = Mock SpringSecurityService
    def objectFactory = Mock ObjectFactory

    def setup() {
        service.verifyService = verifyService
        service.profileService = profileService
        service.springSecurityService = springSecurityService
        service.objectFactory = objectFactory
    }

    def cleanup() {
    }

    void "Test register()"() {
        when:
        def result = service.register(user)

        then:
        1 * user.save() >> user
        1 * verifyService.getRegistrationVerificationByUser(user) >> emailVerification
        1 * emailVerification.delete()
        0 * _

        and:
        result == user
    }

    void "Test editUserField()"() {
        when:
        def result = service.editUserField(user, EMAIL, FIELD_VALUE)

        then:
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

    void "Test getCurrentProfile()"() {
        when:
        def result = service.currentProfile

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User) >> user
        1 * user.currentProfile >> profile
        0 * _

        and:
        result == profile
    }

    void "Test getRoles()"() {
        given:
        String authority = "ROLE_ADMIN"

        when:
        def result = service.roles

        then:
        1 * springSecurityService.principal >> grailsUser
        1 * grailsUser.authorities >> [grantedAuthority, grantedAuthority]
        2 * grantedAuthority.authority >> authority
        0 * _

        and:
        result == [authority, authority]
    }
}
