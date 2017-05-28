package com.tempvs.user

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(VerifyController)
@Mock([UserProfile, ClubProfile])
class VerifyControllerSpec extends Specification {

    private static final String NO_VERIFICATION_CODE = 'verify.noCode.message'
    private static final String ID = 'id'
    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String REGISTRATION_PAGE_URI = '/verify/registration'
    private static final String ERROR_PAGE_URI = '/verify/error'
    private static final String USER_EDIT_PAGE_URI = '/user/edit'
    private static final String EDIT_PROFILE_PAGE_URI = '/profile/edit'
    private static final String REGISTRATION = 'registration'
    private static final String USERPROFILE = 'userprofile'
    private static final String CLUBPROFILE = 'clubprofile'

    def userService = Mock(UserService)
    def profileService = Mock(ProfileService)
    def verifyService = Mock(VerifyService)
    def emailVerification = Mock(EmailVerification)
    def user = Mock(User)
    def userProfile = Mock(UserProfile)
    def clubProfile = Mock(ClubProfile)
    def profileHolder = Mock(ProfileHolder)

    def setup() {
        controller.userService = userService
        controller.profileService = profileService
        controller.verifyService = verifyService
        controller.profileHolder = profileHolder
    }

    def cleanup() {
    }

    void "Check registration verification"() {
        when:
        controller.byEmail()

        then:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [message: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.byEmail()

        then:
        1 * verifyService.getVerification(ID) >> null
        0 * _

        and:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [message: NO_VERIFICATION_CODE]
    }

    void "Check successful registration verification"() {
       when:
        params.id = ID
        controller.byEmail()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.getAction() >> REGISTRATION
        1 * emailVerification.getEmail() >> EMAIL
        1 * emailVerification.delete([flush: Boolean.TRUE])
        0 * _

        and:
        controller.modelAndView.viewName == REGISTRATION_PAGE_URI
        controller.modelAndView.model == [email: EMAIL]
    }

    void "Check email update verification"() {
        when:
        controller.byEmail()

        then:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [message: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.byEmail()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.getAction() >> EMAIL
        1 * emailVerification.getInstanceId() >> LONG_ID
        1 * emailVerification.getEmail() >> EMAIL
        1 * userService.updateEmail(LONG_ID, EMAIL) >> user
        1 * user.hasErrors() >> Boolean.FALSE
        1 * emailVerification.delete(['flush':true])
        0 * _

        and:
        response.redirectedUrl == USER_EDIT_PAGE_URI
    }

    void "Check userprofile email update verification"() {
        when:
        controller.byEmail()

        then:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [message: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.byEmail()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.getAction() >> USERPROFILE
        1 * emailVerification.getEmail() >> EMAIL
        1 * emailVerification.getInstanceId() >> LONG_ID
        1 * profileService.updateProfileEmail(UserProfile.class, LONG_ID, EMAIL) >> userProfile
        1 * userProfile.hasErrors() >> Boolean.FALSE
        1 * profileHolder.setProfile(userProfile)
        1 * emailVerification.delete(['flush':true])
        0 * _

        and:
        response.redirectedUrl == EDIT_PROFILE_PAGE_URI
    }

    void "Check clubprofile email update verification"() {
        when:
        controller.byEmail()

        then:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [message: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.byEmail()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.getAction() >> CLUBPROFILE
        1 * emailVerification.getEmail() >> EMAIL
        1 * emailVerification.getInstanceId() >> LONG_ID
        1 * profileService.updateProfileEmail(ClubProfile.class, LONG_ID, EMAIL) >> clubProfile
        1 * clubProfile.hasErrors() >> Boolean.FALSE
        1 * profileHolder.setProfile(clubProfile)
        1 * emailVerification.delete(['flush':true])
        0 * _

        and:
        response.redirectedUrl == EDIT_PROFILE_PAGE_URI
    }
}
