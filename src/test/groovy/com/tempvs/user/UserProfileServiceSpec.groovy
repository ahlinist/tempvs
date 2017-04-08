package com.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.codehaus.groovy.runtime.InvokerHelper
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(UserProfileService)
@Mock(UserProfile)
class UserProfileServiceSpec extends Specification {

    private static final String PROFILE_EMAIL = 'profileEmail'
    private static final Long LONG_ID = 1L
    private static final String ONE = '1'
    private static final String USER_PROFILE = 'userProfile'

    def springSecurityService = Mock(SpringSecurityService)
    def userProfile = Mock(UserProfile)
    def user = Mock(User)

    def setup() {
        GroovySpy(InvokerHelper, global: true)
        GroovySpy(User, global: true)
        GroovySpy(UserProfile, global: true)

        service.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Check getProfileByProfileEmail()"() {
        when:
        def result = service.getProfileByProfileEmail(PROFILE_EMAIL)

        then:
        1 * UserProfile.findByProfileEmail(PROFILE_EMAIL) >> userProfile
        0 * _

        and:
        result == userProfile
    }

    void "Check getUserProfile()"() {
        when:
        def result = service.getUserProfile(ONE)

        then:
        1 * UserProfile.findByProfileId(ONE) >> null
        1 * UserProfile.get(LONG_ID) >> userProfile

        and:
        result == userProfile
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
