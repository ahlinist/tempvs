package com.tempvs.user

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(VerifyController)
class VerifyControllerSpec extends Specification {

    private static final String NO_VERIFICATION_CODE = 'verify.noCode.message'
    private static final String ID = 'id'
    private static final String USER_ID = 'userId'
    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String REGISTRATION_PAGE_URI = '/verify/registration'
    private static final String ERROR_PAGE_URI = '/verify/error'
    private static final String USER_EDIT_PAGE_URI = '/user/edit'
    private static final String EDIT_USER_PROFILE_PAGE_URI = '/userProfile/edit'

    def userService = Mock(UserService)
    def userProfileService = Mock(UserProfileService)
    def verifyService = Mock(VerifyService)
    def emailVerification = Mock(EmailVerification)

    def setup() {
        controller.userService = userService
        controller.userProfileService = userProfileService
        controller.verifyService = verifyService
    }

    def cleanup() {
    }

    void "Check registration() action"() {
        when:
        controller.registration()

        then:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [message: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.registration()

        then:
        1 * verifyService.getVerification(ID) >> null

        and:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [message: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.registration()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.getProperty(EMAIL) >> EMAIL
        1 * emailVerification.delete([flush: Boolean.TRUE])

        and:
        controller.modelAndView.viewName == REGISTRATION_PAGE_URI
        controller.modelAndView.model == [email: EMAIL]
    }

    void "Check email() action"() {
        when:
        controller.email()

        then:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [message: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.email()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.getProperty(EMAIL) >> EMAIL
        1 * emailVerification.getProperty(USER_ID) >> LONG_ID
        1 * userService.updateEmail(LONG_ID, EMAIL)

        and:
        response.redirectedUrl == USER_EDIT_PAGE_URI
    }

    void "Check profileEmail() action"() {
        when:
        controller.profileEmail()

        then:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [message: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.profileEmail()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.getProperty(EMAIL) >> EMAIL
        1 * emailVerification.getProperty(USER_ID) >> LONG_ID
        1 * userProfileService.updateProfileEmail(LONG_ID, EMAIL)

        and:
        response.redirectedUrl == EDIT_USER_PROFILE_PAGE_URI
    }
}
