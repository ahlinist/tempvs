package com.tempvs.user

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.item.Passport
import com.tempvs.item.PassportService
import com.tempvs.periodization.Period
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@Mock([ClubProfile])
@TestFor(ProfileController)
class ProfileControllerSpec extends Specification {

    private static final String ID = 'id'
    private static final String ONE = '1'
    private static final Long LONG_ID = 1L
    private static final String USER = 'user'
    private static final String EMAIL = 'email'
    private static final String CLASS = 'class'
    private static final String AVATAR = 'avatar'
    private static final String GET_METHOD = 'GET'
    private static final String POST_METHOD = 'POST'
    private static final String PROFILE = 'profile'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String FIRST_NAME = 'firstName'
    private static final String AUTH_URL = '/auth/index'
    private static final String FIELD_NAME = 'fieldName'
    private static final String PROPERTIES = 'properties'
    private static final String IDENTIFIER = 'identifier'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String SUCCESS_ACTION = 'success'
    private static final String AVATAR_COLLECTION = 'avatar'
    private static final String USER_PROFILE = 'userProfile'
    private static final String CLUB_PROFILES = 'clubProfiles'
    private static final String PROFILE_URL = '/profile/index'
    private static final String DELETE_ACTION = 'deleteElement'
    private static final String IMAGE_UPLOAD_BEAN = 'imageUploadBean'
    private static final String CLUB_PROFILE_URL = '/profile/clubProfile'
    private static final String USER_PROFILE_PAGE_URI = '/profile/userProfile'
    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'
    private static final String EDIT_PROFILE_EMAIL_MESSAGE_SENT = 'profileEmail.verification.sent.message'

    def json = Mock JSON
    def user = Mock User
    def image = Mock Image
    def passport = Mock Passport
    def period = GroovyMock Period
    def userService = Mock UserService
    def userProfile = Mock UserProfile
    def clubProfile = Mock ClubProfile
    def imageService = Mock ImageService
    def profileHolder = Mock ProfileHolder
    def verifyService = Mock VerifyService
    def profileService = Mock ProfileService
    def imageUploadBean = Mock ImageUploadBean
    def passportService = Mock PassportService
    def emailVerification = Mock EmailVerification
    def clubProfileCommand = Mock ClubProfileCommand
    def ajaxResponseHelper = Mock AjaxResponseHelper

    def setup() {
        controller.userService = userService
        controller.imageService = imageService
        controller.profileHolder = profileHolder
        controller.verifyService = verifyService
        controller.profileService = profileService
        controller.passportService = passportService
        controller.ajaxResponseHelper = ajaxResponseHelper
    }

    def cleanup() {
    }

    void "Test index()"() {
        when:
        controller.index()

        then:
        1 * profileHolder.profile >> userProfile
        1 * userProfile.getProperty(CLASS) >> UserProfile
        1 * userProfile.getProperty(IDENTIFIER) >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl.contains USER_PROFILE_PAGE_URI
    }

    void "Test userProfile()"() {
        when: 'No id given'
        controller.userProfile()

        then:
        1 * userService.currentUser >> user
        1 * user.getProperty(USER_PROFILE)  >> userProfile
        1 * userProfile.getProperty(IDENTIFIER) >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl == "/profile/userProfile/${IDENTIFIER}"

        when: 'Id given'
        params.id = ONE
        def result = controller.userProfile()

        then:
        1 * profileService.getProfile(UserProfile.class, ONE) >> userProfile
        1 * userProfile.getProperty(USER) >> user
        1 * userProfile.getProperty(IDENTIFIER) >> IDENTIFIER
        1 * profileHolder.profile >> userProfile
        0 * _

        and:
        result == [profile: userProfile, user: user, id: IDENTIFIER, editAllowed: Boolean.TRUE]
    }

    void "Test clubProfile() for non-existent profile"() {
        when:
        controller.clubProfile()

        then:
        0 * _

        and:
        response.redirectedUrl == AUTH_URL

        when: 'Id given'
        params.id = ONE
        def result = controller.clubProfile()

        then: 'For non-existing profile'
        1 * profileService.getProfile(ClubProfile.class, ONE) >> null
        0 * _

        and:
        result == [id: ONE, notFoundMessage: NO_SUCH_PROFILE]
    }

    void "Test clubProfile()"() {
        when:
        params.id = ONE
        def result = controller.clubProfile()

        then: 'For non-existing profile'
        1 * profileService.getProfile(ClubProfile.class, ONE) >> clubProfile
        1 * clubProfile.getProperty(USER) >> user
        1 * clubProfile.getProperty(IDENTIFIER) >> IDENTIFIER
        1 * profileHolder.profile >> clubProfile
        1 * passportService.getPassportsByProfile(clubProfile) >> [passport]
        0 * _

        and:
        result == [profile: clubProfile, user: user, id: IDENTIFIER, passports: [passport], editAllowed: Boolean.TRUE]
    }

    void "Test switchProfile() being logged in without id"() {
        when:
        controller.switchProfile()

        then:
        1 * userService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * profileHolder.setProperty(PROFILE, userProfile)
        0 * _

        and:
        response.redirectedUrl == PROFILE_URL
    }

    void "Test switchProfile() being logged in with id"() {
        when:
        params.id = LONG_ID
        controller.switchProfile()

        then:
        1 * profileService.getProfile(_, LONG_ID) >> userProfile
        1 * profileHolder.setProperty(PROFILE, userProfile)
        0 * _

        and:
        response.redirectedUrl == PROFILE_URL
    }

    void "Test createClubProfile() against invalid command"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createClubProfile(clubProfileCommand)

        then:
        1 * clubProfileCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderValidationResponse(clubProfileCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createClubProfile() against invalid profile"() {
        given:
        request.method = POST_METHOD
        Map properties = [firstName: FIRST_NAME]

        when:
        controller.createClubProfile(clubProfileCommand)

        then:
        1 * clubProfileCommand.validate() >> Boolean.TRUE
        1 * clubProfileCommand.getProperty(IMAGE_UPLOAD_BEAN) >> imageUploadBean
        1 * userService.currentUser >> user
        1 * clubProfileCommand.getProperty(PROPERTIES) >> properties
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * profileService.createProfile(_ as ClubProfile, image) >> clubProfile
        1 * clubProfile.hasErrors() >> Boolean.TRUE
        1 * ajaxResponseHelper.renderValidationResponse(_ as ClubProfile) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test createClubProfile()"() {
        given:
        request.method = POST_METHOD
        Map properties = [user: user, period: period, firstName: FIRST_NAME]

        when: 'Command valid. Profile created.'
        controller.createClubProfile(clubProfileCommand)

        then:
        1 * clubProfileCommand.validate() >> Boolean.TRUE
        1 * clubProfileCommand.getProperty(IMAGE_UPLOAD_BEAN) >> imageUploadBean
        1 * userService.currentUser >> user
        1 * clubProfileCommand.getProperty(PROPERTIES) >> properties
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * profileService.createProfile(_ as ClubProfile, image) >> clubProfile
        1 * clubProfile.hasErrors() >> Boolean.FALSE
        1 * profileHolder.setProperty(PROFILE, clubProfile)
        1 * clubProfile.getProperty(ID) >> LONG_ID
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
        1 * profileHolder.getProfile() >> userProfile
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
        1 * profileHolder.getProfile() >> userProfile
        1 * userProfile.getProperty(ID) >> LONG_ID
        1 * userProfile.getProperty(CLASS) >> UserProfile
        1 * verifyService.createEmailVerification(_ as EmailVerification) >> emailVerification
        1 * emailVerification.hasErrors() >> Boolean.FALSE
        1 * verifyService.sendEmailVerification(emailVerification)
        1 * ajaxResponseHelper.renderValidationResponse(emailVerification, EDIT_PROFILE_EMAIL_MESSAGE_SENT) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteProfile()"() {
        given:
        params.id = ONE
        request.method = DELETE_METHOD

        when:
        controller.deleteProfile()

        then:
        1 * profileService.getProfile(ClubProfile.class, ONE) >> clubProfile
        1 * profileService.deleteProfile(clubProfile)
        1 * profileHolder.setProfile(null)
        0 * _

        and:
        response.json.action == DELETE_ACTION
    }

    void "Test deleteAvatar()"() {
        given:
        params.profileId = ONE
        request.method = DELETE_METHOD
        params.profileClass = UserProfile.class.name
        controller.request.addHeader('referer', "${USER_PROFILE_PAGE_URI}/${LONG_ID}")

        when:
        controller.deleteAvatar()

        then:
        1 * profileService.getProfile(UserProfile, ONE) >> userProfile
        1 * profileService.deleteAvatar(userProfile)
        1 * ajaxResponseHelper.renderRedirect("${USER_PROFILE_PAGE_URI}/${LONG_ID}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test uploadAvatar()"() {
        given:
        params.profileId = ONE
        request.method = POST_METHOD
        params.profileClass = UserProfile.class.name
        controller.request.addHeader('referer', "${USER_PROFILE_PAGE_URI}/${LONG_ID}")

        when:
        controller.uploadAvatar(imageUploadBean)

        then:
        1 * profileHolder.profile >> userProfile
        1 * userProfile.getProperty(AVATAR) >> image
        1 * imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION, image) >> image
        1 * profileService.uploadAvatar(userProfile, image) >> userProfile
        1 * userProfile.hasErrors() >> Boolean.FALSE
        1 * ajaxResponseHelper.renderRedirect("${USER_PROFILE_PAGE_URI}/${LONG_ID}") >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test list()"() {
        given:
        request.method = GET_METHOD

        when:
        def result = controller.list()

        then:
        1 * userService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * user.getProperty(CLUB_PROFILES) >> [clubProfile]
        0 * _

        result == [userProfile: userProfile, clubProfiles: [clubProfile]]
    }
}
