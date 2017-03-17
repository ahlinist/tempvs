package com.tempvs.services

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
import grails.plugin.springsecurity.SpringSecurityService
import grails.plugins.mail.MailService
import grails.test.mixin.TestFor
import org.codehaus.groovy.runtime.InvokerHelper
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(UserService)
class UserServiceSpec extends Specification {
    private static final String ID = '1'
    private static final Long LONG_ID = 1L
    private static final String CUSTOM_ID = 'customId'
    private static final String USER = 'user'
    private static final String EMAIL = 'email'
    private static final String PROFILE_EMAIL = 'profileEmail'
    private static final String PASSWORD = 'password'
    private static final String VERIFICATION_CODE = 'verificationCode'
    private static final String USER_PROFILE = 'userProfile'

    def springSecurityService = Mock(SpringSecurityService)
    def mailService = Mock(MailService)
    def user = Mock(User)
    def userProfile = Mock(UserProfile)
    def emailVerification = Mock(EmailVerification)

    def setup() {
        GroovySpy(User, global: true)
        GroovySpy(UserProfile, global: true)
        GroovySpy(EmailVerification, global: true)
        GroovySpy(InvokerHelper, global: true)

        service.mailService = mailService
        service.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Check getUser() method for id"() {
        when:
        def result = service.getUser(ID)

        then:
        1 * UserProfile.findByCustomId(ID) >> null
        1 * User.get(_) >> user
        0 * _

        and:
        result == user
    }

    void "Check getUser() method for customId"() {
        when:
        def result = service.getUser(CUSTOM_ID)

        then:
        1 * UserProfile.findByCustomId(CUSTOM_ID) >> userProfile
        1 * userProfile.getProperty(USER) >> user

        and:
        result == user
    }

    void "Check getUserByEmail()"() {
        when:
        def result = service.getUserByEmail(EMAIL)

        then:
        1 * User.findByEmail(EMAIL) >> user

        and:
        result == user
    }

    void "Check getUserByProfileEmail()"() {
        when:
        def result = service.getUserByProfileEmail(PROFILE_EMAIL)

        then:
        1 * UserProfile.findByProfileEmail(PROFILE_EMAIL) >> userProfile
        1 * userProfile.getProperty(USER) >> user

        and:
        result == user
    }

    void "Check getVerification()"() {
        when:
        def result = service.getVerification(VERIFICATION_CODE)

        then:
        1 * EmailVerification.findByVerificationCode(VERIFICATION_CODE) >> emailVerification

        and:
        result == emailVerification
    }

    void "Check successful creation of email verification"() {
        when:
        def result = service.createEmailVerification([email: EMAIL])

        then: 'Verification created and mail sent'
        1 * new EmailVerification(_) >> emailVerification
        1 * emailVerification.save([flush: true]) >> emailVerification
        1 * mailService.sendMail(_)

        and:
        result == emailVerification
    }

    void "Check failed creation of email verification"() {
        when:
        def result = service.createEmailVerification([email: EMAIL])

        then: 'Verification created, not saved and not sent'
        1 * new EmailVerification(_) >> emailVerification
        1 * emailVerification.save([flush: true])

        and:
        result == emailVerification
    }

    void "Check user creation"() {
        given:
        Map properties = [:]

        when:
        def result = service.createUser(properties)

        then: "Appropriate constructors are called"
        1 * new User(properties) >> user
        1 * user.setProperty(PASSWORD, PASSWORD)
        1 * user.getProperty(PASSWORD) >> PASSWORD
        1 * user.getProperty('springSecurityService') >> springSecurityService
        1 * springSecurityService.encodePassword(_) >> PASSWORD
        1 * user.setProperty(USER_PROFILE, userProfile)
        1 * new UserProfile(properties) >> userProfile
        1 * user.save([flush: true])

        and:
        result == user
    }

    void "Check email update"() {
        when:
        def result = service.updateEmail(LONG_ID, EMAIL)

        then:
        1 * User.get(LONG_ID) >> user
        1 * user.setEmail(EMAIL)
        1 * user.save([flush: true])

        and:
        result == user
    }

    void "Check password update"() {
        when:
        def result = service.updatePassword(PASSWORD)

        then:
        1 * springSecurityService.currentUser >> user
        1 * springSecurityService.encodePassword(PASSWORD) >> PASSWORD
        1 * user.setPassword(PASSWORD)
        1 * user.save([flush: true])

        and:
        result == user
    }

    void "Check updateLastActive()"() {
        when:
        service.updateLastActive()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.setLastActive(_ as Date)
        1 * user.save([flush: true])
    }

    void "Check profile email update"() {
        when:
        def result = service.updateProfileEmail(LONG_ID, PROFILE_EMAIL)

        then:
        1 * User.get(LONG_ID) >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.setProfileEmail(PROFILE_EMAIL)
        1 * userProfile.save([flush: true])

        and:
        result == userProfile
    }

    void "Check update of user's profile"() {
        given:
        Map properties = [:]

        when:
        def result = service.updateUserProfile(properties)

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * InvokerHelper.setProperties(userProfile, properties)
        1 * userProfile.save([flush: true])

        and:
        result == userProfile
    }
}
