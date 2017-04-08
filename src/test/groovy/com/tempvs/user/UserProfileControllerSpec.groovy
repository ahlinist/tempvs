package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserProfileController)
class UserProfileControllerSpec extends Specification {

    private static final String NO_SUCH_USER = 'userProfile.noSuchUser.message'
    private static final String LOGIN_PAGE_URI = '/auth/index'
    private static final String SHOW_PROFILE_PAGE_URI = '/userProfile/show'
    private static final String PROFILE_ID = 'profileId'
    private static final String USER_PROFILE = 'userProfile'
    private static final String ID = 'id'
    private static final String PROFILE_EMAIL = 'profileEmail'
    private static final String USER_PROFILE_UPDATED_MESSAGE = 'user.userProfile.updated'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'user.edit.profileEmail.verification.sent.message'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String EMAIL = 'email'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String UPDATE_PROFILE_EMAIL_ACTION = 'profileEmail'

    def userProfileService = Mock(UserProfileService)
    def springSecurityService = Mock(SpringSecurityService)
    def user = Mock(User)
    def userProfile = Mock(UserProfile)
    def userService = Mock(UserService)
    def verifyService = Mock(VerifyService)
    def userProfileCommand = Mock(UserProfileCommand)
    def ajaxResponseService = Mock(AjaxResponseService)
    def json = Mock(JSON)
    def emailVerification = Mock(EmailVerification)

    def setup() {
        controller.userProfileService = userProfileService
        controller.ajaxResponseService = ajaxResponseService
        controller.springSecurityService = springSecurityService
        controller.userService = userService
        controller.verifyService = verifyService
    }

    def cleanup() {
    }

    void "Render show page being not logged in"() {
        when:
        controller.show()

        then:
        1 * springSecurityService.currentUser
        0 * _

        and:
        response.redirectedUrl == LOGIN_PAGE_URI
    }

    void "Render show page being logged in having customId in userProfile"() {
        when:
        controller.show()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(PROFILE_ID) >> PROFILE_ID
        0 * _

        and:
        response.redirectedUrl == "${SHOW_PROFILE_PAGE_URI}/${PROFILE_ID}"
    }

    void "Render show page being logged in having no profileId in userProfile"() {
        when:
        controller.show()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(PROFILE_ID)
        1 * userProfile.getProperty(ID)
        0 * _

        and:
        response.redirectedUrl == "${SHOW_PROFILE_PAGE_URI}"
    }

    void "Render show page for non-existent id being not logged in"() {
        when:
        params.id = ID
        Map model = controller.show()

        then:
        1 * springSecurityService.currentUser
        1 * userProfileService.getUserProfile(ID)
        0 * _

        and:
        model == [id: ID, message: NO_SUCH_USER, args: [ID]]
    }

    void "Render show page for existent id being not logged in"() {
        when:
        params.id = ID
        Map model = controller.show()

        then:
        1 * springSecurityService.currentUser
        1 * userProfileService.getUserProfile(ID) >> userProfile
        1 * userProfile.getProperty(ID) >> ID
        0 * _

        and:
        model == [profile: userProfile, id: ID]
    }

    void "Render show page for non-existent id being logged in having no profileId"() {
        when:
        params.id = ID
        Map model = controller.show()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(PROFILE_ID)
        1 * userProfile.getProperty(ID)
        1 * userProfileService.getUserProfile(ID)
        0 * _

        and:
        model == [id: ID, message: NO_SUCH_USER, args: [ID]]
    }

    void "Render show page for non-existent id being logged in having customId"() {
        when:
        params.id = PROFILE_ID
        Map model = controller.show()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(PROFILE_ID) >> PROFILE_ID
        0 * _

        and:
        model == [profile: userProfile, id: PROFILE_ID]
    }

    void "Check updateUserProfile action"() {
        when:
        controller.updateUserProfile(userProfileCommand)

        then:
        _ * userProfileCommand._ >> [:]
        1 * userProfileService.updateUserProfile(_ as Map) >> userProfile
        1 * ajaxResponseService.composeJsonResponse(userProfile, USER_PROFILE_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updateProfileEmail action for duplicate"() {
        when:
        controller.updateProfileEmail(PROFILE_EMAIL)

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(PROFILE_EMAIL) >> PROFILE_EMAIL
        0 * _

        and:
        response.json.messages == [EMAIL_UPDATE_DUPLICATE]
    }

    void "Check updateProfileEmail action for used profileEmail"() {
        when:
        controller.updateProfileEmail(PROFILE_EMAIL)

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(PROFILE_EMAIL)
        1 * userService.getUserByEmail(PROFILE_EMAIL) >> user
        1 * user.getProperty(EMAIL) >> PROFILE_EMAIL
        1 * userProfileService.getProfileByProfileEmail(PROFILE_EMAIL) >> userProfile
        0 * _
        response.json.messages == [EMAIL_USED]
    }

    void "Check updateProfileEmail action"() {
        when:
        controller.updateProfileEmail(PROFILE_EMAIL)

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(PROFILE_EMAIL)
        1 * userService.getUserByEmail(PROFILE_EMAIL)
        1 * userProfileService.getProfileByProfileEmail(PROFILE_EMAIL)
        1 * user.getProperty(ID) >> ID
        1 * verifyService.createEmailVerification(
                [userId: ID, email: PROFILE_EMAIL, action: UPDATE_PROFILE_EMAIL_ACTION]) >> emailVerification
        1 * ajaxResponseService.composeJsonResponse(emailVerification, UPDATE_PROFILE_EMAIL_MESSAGE_SENT) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test edit page rendering"() {
        when:
        Map model = controller.edit()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile

        and:
        model == [profile: userProfile]
    }
}
