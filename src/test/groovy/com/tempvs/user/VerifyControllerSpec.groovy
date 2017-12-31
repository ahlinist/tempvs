package com.tempvs.user

import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class VerifyControllerSpec extends Specification implements ControllerUnitTest<VerifyController> {

    private static final String ID = 'id'
    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String USERPROFILE = 'userProfile'
    private static final String CLUBPROFILE = 'clubProfile'
    private static final String PROFILE_PAGE_URI = '/profile'
    private static final String REGISTRATION = 'registration'
    private static final String PROFILE_EMAIL = 'profileEmail'
    private static final String ERROR_PAGE_URI = '/verify/error'
    private static final String USER_EDIT_PAGE_URI = '/user/edit'
    private static final String REGISTRATION_PAGE_URI = '/verify/registration'
    private static final String NO_VERIFICATION_CODE = 'verify.noCode.message'

    def user = Mock User
    def userService = Mock UserService
    def userProfile = Mock UserProfile
    def clubProfile = Mock ClubProfile
    def verifyService = Mock VerifyService
    def profileHolder = Mock ProfileHolder
    def profileService = Mock ProfileService
    def emailVerification = Mock EmailVerification
    def springSecurityService = Mock SpringSecurityService

    def setup() {
        controller.userService = userService
        controller.verifyService = verifyService
        controller.profileHolder = profileHolder
        controller.profileService = profileService
        controller.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "Check registration verification"() {
        when:
        controller.byEmail()

        then:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [notFoundMessage: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.byEmail()

        then:
        1 * verifyService.getVerification(ID) >> null
        0 * _

        and:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [notFoundMessage: NO_VERIFICATION_CODE]
    }

    void "Check successful registration verification"() {
       when:
        params.id = ID
        controller.byEmail()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.getAction() >> REGISTRATION
        0 * _

        and:
        controller.modelAndView.viewName == REGISTRATION_PAGE_URI
        controller.modelAndView.model == [emailVerification: emailVerification]
    }

    void "Check email update verification"() {
        when:
        controller.byEmail()

        then:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [notFoundMessage: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.byEmail()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.action >> EMAIL
        1 * emailVerification.instanceId >> LONG_ID
        1 * userService.getUser(LONG_ID) >> user
        1 * emailVerification.email >> EMAIL
        1 * user.hasErrors() >> Boolean.FALSE
        1 * user.email >> EMAIL
        1 * springSecurityService.reauthenticate(EMAIL)
        1 * userService.editUserField(user, EMAIL, EMAIL) >> user
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
        controller.modelAndView.model == [notFoundMessage: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.byEmail()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.getAction() >> USERPROFILE
        1 * emailVerification.getEmail() >> EMAIL
        1 * emailVerification.getInstanceId() >> LONG_ID
        1 * profileService.getProfile(UserProfile, LONG_ID) >> userProfile
        1 * userProfile.validate() >> Boolean.TRUE
        1 * profileService.editProfileField(userProfile, PROFILE_EMAIL, EMAIL) >> userProfile
        1 * profileHolder.setProfile(userProfile)
        1 * userProfile.identifier >> LONG_ID
        1 * emailVerification.delete(['flush':true])
        0 * _

        and:
        response.redirectedUrl.contains PROFILE_PAGE_URI
    }

    void "Check clubprofile email update verification"() {
        when:
        controller.byEmail()

        then:
        controller.modelAndView.viewName == ERROR_PAGE_URI
        controller.modelAndView.model == [notFoundMessage: NO_VERIFICATION_CODE]

        when:
        params.id = ID
        controller.byEmail()

        then:
        1 * verifyService.getVerification(ID) >> emailVerification
        1 * emailVerification.getAction() >> CLUBPROFILE
        1 * emailVerification.getEmail() >> EMAIL
        1 * emailVerification.getInstanceId() >> LONG_ID
        1 * profileService.getProfile(ClubProfile, LONG_ID) >> clubProfile
        1 * clubProfile.validate() >> Boolean.TRUE
        1 * profileService.editProfileField(clubProfile, PROFILE_EMAIL, EMAIL) >> clubProfile
        1 * profileHolder.setProfile(clubProfile)
        1 * clubProfile.identifier >> LONG_ID
        1 * emailVerification.delete(['flush':true])
        0 * _

        and:
        response.redirectedUrl.contains PROFILE_PAGE_URI
    }
}
