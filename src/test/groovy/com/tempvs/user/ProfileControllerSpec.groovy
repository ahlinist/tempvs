package com.tempvs.user

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.communication.Following
import com.tempvs.communication.FollowingService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.item.Passport
import com.tempvs.item.PassportService
import com.tempvs.periodization.Period
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletRequest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

class ProfileControllerSpec extends Specification implements ControllerUnitTest<ProfileController>, DomainUnitTest<UserProfile> {

    private static final String ONE = '1'
    private static final Integer OFFSET = 0
    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String QUERY = 'query'
    private static final String GET_METHOD = 'GET'
    private static final String REFERER = 'referer'
    private static final String POST_METHOD = 'POST'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String AUTH_URL = '/auth/index'
    private static final String FIELD_NAME = 'fieldName'
    private static final String IDENTIFIER = 'identifier'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String SUCCESS_ACTION = 'success'
    private static final String AVATAR_COLLECTION = 'avatar'
    private static final String PROFILE_URL = '/profile/index'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String CLUB_PROFILE_URL = '/profile/club'
    private static final String USER_PROFILE_PAGE_URI = '/profile/user'
    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'
    private static final String EDIT_PROFILE_EMAIL_MESSAGE_SENT = 'profileEmail.verification.sent.message'

    def json = Mock JSON
    def user = Mock User
    def image = Mock Image
    def passport = Mock Passport
    def following = Mock Following
    def period = GroovyMock Period
    def userProfile = Mock UserProfile
    def clubProfile = Mock ClubProfile
    def imageUploadBean = Mock ImageUploadBean
    def emailVerification = Mock EmailVerification

    def userService = Mock UserService
    def imageService = Mock ImageService
    def verifyService = Mock VerifyService
    def profileService = Mock ProfileService
    def userInfoHelper = Mock UserInfoHelper
    def passportService = Mock PassportService
    def groovyPageRenderer = Mock PageRenderer
    def followingService = Mock FollowingService
    def ajaxResponseHelper = Mock AjaxResponseHelper

    def setup() {
        controller.userService = userService
        controller.imageService = imageService
        controller.verifyService = verifyService
        controller.userInfoHelper = userInfoHelper
        controller.profileService = profileService
        controller.passportService = passportService
        controller.followingService = followingService
        controller.ajaxResponseHelper = ajaxResponseHelper
        controller.groovyPageRenderer = groovyPageRenderer
    }

    def cleanup() {
    }

    void "Test index()"() {
        when:
        controller.index()

        then:
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> userProfile
        1 * userProfile.identifier >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl.contains USER_PROFILE_PAGE_URI
    }

    void "Test search()"() {
        given:
        params.query = QUERY
        params.offset = OFFSET
        request.method = GET_METHOD

        when:
        controller.search()

        then:
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> userProfile
        1 * profileService.searchProfiles(userProfile, QUERY, OFFSET) >> [userProfile]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test user()"() {
        when: 'No id given'
        controller.user()

        then:
        1 * userInfoHelper.getCurrentUser(_ as GrailsMockHttpServletRequest) >> user
        1 * user.userProfile  >> userProfile
        1 * userProfile.identifier >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl == "/profile/userProfile/${IDENTIFIER}"

        when: 'Id given'
        params.id = ONE
        def result = controller.user()

        then:
        1 * profileService.getProfile(UserProfile.class, ONE) >> userProfile
        1 * userProfile.user >> user
        1 * userProfile.identifier >> IDENTIFIER
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> userProfile
        1 * followingService.mayBeFollowed(userProfile, userProfile) >> Boolean.TRUE
        1 * followingService.getFollowing(userProfile, userProfile) >> following
        1 * following.asType(Boolean) >> Boolean.TRUE
        0 * _

        and:
        result == [profile: userProfile, user: user, id: IDENTIFIER, editAllowed: Boolean.TRUE, mayBeFollowed: Boolean.TRUE, isFollowed: Boolean.TRUE]
    }

    void "Test club() for non-existent profile"() {
        when:
        controller.club()

        then:
        0 * _

        and:
        response.redirectedUrl == AUTH_URL

        when: 'Id given'
        params.id = ONE
        def result = controller.club()

        then: 'For non-existing profile'
        1 * profileService.getProfile(ClubProfile.class, ONE) >> null
        0 * _

        and:
        result == [id: ONE, notFoundMessage: NO_SUCH_PROFILE]
    }

    void "Test club()"() {
        given:
        params.id = ONE

        when:
        def result = controller.club()

        then: 'For non-existing profile'
        1 * profileService.getProfile(ClubProfile.class, ONE) >> clubProfile
        1 * clubProfile.user >> user
        1 * clubProfile.identifier >> IDENTIFIER
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> clubProfile
        1 * clubProfile.passports >> [passport]
        1 * followingService.mayBeFollowed(clubProfile, clubProfile) >> Boolean.TRUE
        1 * followingService.getFollowing(clubProfile, clubProfile) >> following
        1 * following.asType(Boolean) >> Boolean.TRUE
        0 * _

        and:
        result == [profile: clubProfile, user: user, id: IDENTIFIER, passports: [passport] as Set, editAllowed: Boolean.TRUE, mayBeFollowed: Boolean.TRUE, isFollowed: Boolean.TRUE]
    }

    void "Test switchProfile() being logged in without id"() {
        when:
        controller.switchProfile()

        then:
        1 * userInfoHelper.getCurrentUser(_ as GrailsMockHttpServletRequest) >> user
        1 * user.userProfile >> userProfile
        1 * userProfile.active >> Boolean.TRUE
        1 * profileService.setCurrentProfile(userProfile)
        0 * _

        and:
        response.redirectedUrl == PROFILE_URL
    }

    void "Test switchProfile() being logged in with id"() {
        given:
        params.id = LONG_ID

        when:
        controller.switchProfile()

        then:
        1 * profileService.getProfile(_, LONG_ID) >> userProfile
        1 * userProfile.active >> Boolean.TRUE
        1 * profileService.setCurrentProfile(userProfile)
        0 * _

        and:
        response.redirectedUrl == PROFILE_URL
    }

    void "Test createClubProfile() against invalid input"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createClubProfile(clubProfile, imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * userInfoHelper.getCurrentUser(_ as GrailsMockHttpServletRequest) >> user
        1 * clubProfile.setUser(user)
        1 * clubProfile.validate() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderValidationResponse(clubProfile) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createClubProfile() against invalid profile"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createClubProfile(clubProfile, imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * userInfoHelper.getCurrentUser(_ as GrailsMockHttpServletRequest) >> user
        1 * clubProfile.setUser(user)
        1 * clubProfile.validate() >> Boolean.TRUE
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * clubProfile.setAvatar(image)
        1 * profileService.createProfile(_ as ClubProfile) >> clubProfile
        1 * clubProfile.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseHelper.renderValidationResponse(_ as ClubProfile) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createClubProfile()"() {
        given:
        request.method = POST_METHOD

        when: 'Command valid. Profile created.'
        controller.createClubProfile(clubProfile, imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * userInfoHelper.getCurrentUser(_ as GrailsMockHttpServletRequest) >> user
        1 * clubProfile.setUser(user)
        1 * clubProfile.validate() >> Boolean.TRUE
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * clubProfile.setAvatar(image)
        1 * profileService.createProfile(_ as ClubProfile) >> clubProfile
        1 * clubProfile.hasErrors() >> Boolean.FALSE
        1 * profileService.setCurrentProfile(clubProfile)
        1 * clubProfile.id >> LONG_ID
        1 * ajaxResponseHelper.renderRedirect("${CLUB_PROFILE_URL}/${LONG_ID}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test editProfileField()"() {
        given:
        params.fieldName = FIELD_NAME
        params.fieldValue = FIELD_VALUE
        request.method = POST_METHOD

        when:
        controller.editProfileField()

        then:
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> userProfile
        1 * profileService.editProfileField(userProfile, FIELD_NAME, FIELD_VALUE) >> userProfile
        1 * userProfile.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.action == SUCCESS_ACTION
    }

    void "Test editProfileEmail()"() {
        given:
        params.fieldValue = EMAIL
        request.method = POST_METHOD

        when:
        controller.editProfileEmail()

        then:
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> userProfile
        1 * userProfile.id >> LONG_ID
        1 * verifyService.createEmailVerification(_ as EmailVerification) >> emailVerification
        1 * emailVerification.hasErrors() >> Boolean.FALSE
        1 * verifyService.sendEmailVerification(emailVerification)
        1 * ajaxResponseHelper.renderValidationResponse(emailVerification, EDIT_PROFILE_EMAIL_MESSAGE_SENT) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deactivateProfile()"() {
        given:
        params.id = ONE
        request.method = POST_METHOD

        when:
        controller.deactivateProfile()

        then:
        1 * profileService.getProfile(ClubProfile.class, ONE) >> clubProfile
        1 * clubProfile.user >> user
        1 * user.clubProfiles >> [clubProfile]
        1 * profileService.deactivateProfile(clubProfile) >> clubProfile
        1 * clubProfile.hasErrors() >> Boolean.FALSE
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> userProfile
        1 * userProfile.equals(clubProfile) >> Boolean.FALSE
        2 * clubProfile.active >> Boolean.TRUE
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test activateProfile()"() {
        given:
        params.id = ONE
        request.method = POST_METHOD
        controller.request.addHeader('referer', REFERER)

        when:
        controller.activateProfile()

        then:
        1 * profileService.getProfile(ClubProfile.class, ONE) >> clubProfile
        1 * clubProfile.active >> Boolean.FALSE
        1 * profileService.activateProfile(clubProfile) >> clubProfile
        1 * clubProfile.hasErrors() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderRedirect(REFERER) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteAvatar()"() {
        given:
        params.profileId = ONE
        request.method = DELETE_METHOD
        params.profileClass = UserProfile.class.name
        controller.request.addHeader('referer', REFERER)

        when:
        controller.deleteAvatar()

        then:
        1 * profileService.getProfile(UserProfile, ONE) >> userProfile
        1 * profileService.deleteAvatar(userProfile)
        1 * ajaxResponseHelper.renderRedirect(REFERER) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test uploadAvatar()"() {
        given:
        params.profileId = ONE
        request.method = POST_METHOD
        params.profileClass = UserProfile.class.name
        controller.request.addHeader('referer', REFERER)

        when:
        controller.uploadAvatar(imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> userProfile
        1 * userProfile.avatar >> image
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION, image) >> image
        1 * profileService.uploadAvatar(userProfile, image) >> userProfile
        1 * userProfile.hasErrors() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderRedirect(REFERER) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test list()"() {
        given:
        request.method = GET_METHOD

        when:
        def result = controller.list()

        then:
        1 * userInfoHelper.getCurrentUser(_ as GrailsMockHttpServletRequest) >> user
        1 * user.userProfile >> userProfile
        1 * user.clubProfiles >> [clubProfile]
        2 * clubProfile.active >> Boolean.TRUE
        0 * _

        result == [
                userProfile: userProfile,
                activeProfiles: [clubProfile],
                inactiveProfiles: [],
                availablePeriods: Period.values(),
        ]
    }
}
