package club.tempvs.user

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.communication.Following
import club.tempvs.communication.FollowingService
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.image.ImageUploadBean
import club.tempvs.item.Passport
import club.tempvs.periodization.Period
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

class ProfileControllerSpec extends Specification implements ControllerUnitTest<ProfileController>, DomainUnitTest<Profile> {

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
    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'

    def json = Mock JSON
    def user = Mock User
    def image = Mock Image
    def passport = Mock Passport
    def following = Mock Following
    def period = GroovyMock Period
    def profile = Mock Profile
    def imageUploadBean = Mock ImageUploadBean
    def emailVerification = Mock EmailVerification

    def userService = Mock UserService
    def imageService = Mock ImageService
    def verifyService = Mock VerifyService
    def profileService = Mock ProfileService
    def groovyPageRenderer = Mock PageRenderer
    def followingService = Mock FollowingService
    def ajaxResponseHelper = Mock AjaxResponseHelper

    def setup() {
        controller.userService = userService
        controller.imageService = imageService
        controller.verifyService = verifyService
        controller.profileService = profileService
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
        1 * profileService.currentProfile >> profile
        1 * profile.identifier >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl.contains '/profile/user'
    }

    void "Test search()"() {
        given:
        params.query = QUERY
        params.offset = OFFSET
        request.method = GET_METHOD

        when:
        controller.search()

        then:
        1 * profileService.currentProfile >> profile
        1 * profileService.searchProfiles(profile, QUERY, OFFSET) >> [profile]
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test user()"() {
        when: 'No id given'
        controller.user()

        then:
        1 * profileService.currentProfile >> profile
        1 * profile.identifier >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl == "/profile/show/${IDENTIFIER}"

        when: 'Id given'
        params.id = ONE
        def result = controller.user()

        then:
        1 * profileService.currentProfile >> profile
        1 * profileService.getProfile(ONE) >> profile
        1 * profile.user >> user
        1 * user.profiles >> [profile]
        2 * profile.active >> Boolean.TRUE
        1 * profile.identifier >> IDENTIFIER
        1 * followingService.mayBeFollowed(profile, profile) >> Boolean.TRUE
        1 * followingService.getFollowing(profile, profile) >> following
        1 * following.asType(Boolean) >> Boolean.TRUE
        0 * _

        and:
        result == [
                profile: profile,
                user: user,
                id: IDENTIFIER,
                editAllowed: Boolean.TRUE,
                mayBeFollowed: Boolean.TRUE,
                isFollowed: Boolean.TRUE,
                activeProfiles: [profile],
                inactiveProfiles: [],
                periods: Period.values(),
        ]
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
        1 * profileService.currentProfile >> profile
        1 * profileService.getProfile(ONE) >> null
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
        1 * profileService.getProfile(ONE) >> profile
        1 * profile.user >> user
        1 * profile.identifier >> IDENTIFIER
        1 * profileService.currentProfile >> profile
        1 * profile.passports >> [passport]
        1 * followingService.mayBeFollowed(profile, profile) >> Boolean.TRUE
        1 * followingService.getFollowing(profile, profile) >> following
        1 * following.asType(Boolean) >> Boolean.TRUE
        1 * profile.active >> Boolean.TRUE
        0 * _

        and:
        result == [profile: profile, user: user, id: IDENTIFIER, passports: [passport], active: Boolean.TRUE, editAllowed: Boolean.TRUE, mayBeFollowed: Boolean.TRUE, isFollowed: Boolean.TRUE]
    }

    void "Test switchProfile() being logged in without id"() {
        when:
        controller.switchProfile()

        then:
        1 * userService.currentUser >> user
        1 * user.profiles >> [profile]
        1 * profile.active >> Boolean.TRUE
        1 * profileService.setCurrentProfile(profile)
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
        1 * userService.currentUser >> user
        1 * user.profiles >> [profile]
        1 * profile.id >> LONG_ID
        1 * profile.active >> Boolean.TRUE
        1 * profileService.setCurrentProfile(profile)
        0 * _

        and:
        response.redirectedUrl == PROFILE_URL
    }

    void "Test createClubProfile() against invalid input"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createClubProfile(profile, imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * userService.currentUser >> user
        1 * profile.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseHelper.renderValidationResponse(profile) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createClubProfile() against invalid profile"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createClubProfile(profile, imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * userService.currentUser >> user
        1 * profile.hasErrors() >> Boolean.FALSE
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * profileService.createClubProfile(profile, image) >> profile
        1 * profile.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseHelper.renderValidationResponse(_ as Profile) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createClubProfile()"() {
        given:
        request.method = POST_METHOD

        when: 'Command valid. Profile created.'
        controller.createClubProfile(profile, imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * userService.currentUser >> user
        1 * profile.hasErrors() >> Boolean.FALSE
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * profileService.createClubProfile(profile, image) >> profile
        1 * profile.hasErrors() >> Boolean.FALSE
        1 * profileService.setCurrentProfile(profile)
        1 * profile.id >> LONG_ID
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
        1 * profileService.currentProfile >> profile
        1 * profileService.editProfileField(profile, FIELD_NAME, FIELD_VALUE) >> profile
        1 * profile.hasErrors() >> Boolean.FALSE
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
        1 * profileService.currentProfile >> profile
        1 * profile.id >> LONG_ID
        1 * verifyService.createEmailVerification(_ as EmailVerification) >> emailVerification
        1 * emailVerification.hasErrors() >> Boolean.FALSE
        1 * verifyService.sendEmailVerification(emailVerification) >> Boolean.TRUE
        1 * ajaxResponseHelper.renderFormMessage(Boolean.TRUE, 'profileEmail.verification.sent.message') >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deactivateProfile()"() {
        given:
        params.id = ONE
        request.method = POST_METHOD
        controller.request.addHeader('referer', REFERER)

        when:
        controller.deactivateProfile()

        then:
        1 * profileService.getProfile(ONE) >> profile
        1 * profileService.currentProfile >> profile
        1 * profile.equals(profile) >> Boolean.FALSE
        1 * profileService.deactivateProfile(profile) >> profile
        1 * profile.hasErrors() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderRedirect(REFERER) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test activateProfile()"() {
        given:
        params.id = ONE
        request.method = POST_METHOD
        controller.request.addHeader('referer', REFERER)

        when:
        controller.activateProfile()

        then:
        1 * profileService.getProfile(ONE) >> profile
        1 * profile.active >> Boolean.FALSE
        1 * profileService.activateProfile(profile) >> profile
        1 * profile.hasErrors() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderRedirect(REFERER) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteAvatar()"() {
        given:
        params.profileId = ONE
        request.method = DELETE_METHOD
        controller.request.addHeader('referer', REFERER)

        when:
        controller.deleteAvatar()

        then:
        1 * profileService.getProfile(ONE) >> profile
        1 * profileService.deleteAvatar(profile)
        1 * ajaxResponseHelper.renderRedirect(REFERER) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test uploadAvatar()"() {
        given:
        params.profileId = ONE
        request.method = POST_METHOD
        controller.request.addHeader('referer', REFERER)

        when:
        controller.uploadAvatar(imageUploadBean)

        then:
        1 * imageUploadBean.validate() >> Boolean.TRUE
        1 * profileService.currentProfile >> profile
        1 * profile.avatar >> image
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * imageService.deleteImage(image)
        1 * profileService.uploadAvatar(profile, image) >> profile
        1 * profile.hasErrors() >> Boolean.FALSE
        1 * groovyPageRenderer.render(_ as Map)
        0 * _
    }

    void "Test getProfileDropdown()"() {
        given:
        request.method = GET_METHOD

        when:
        controller.getProfileDropdown()

        then:
        1 * profileService.getProfileDropdown()
        0 * _
    }
}
