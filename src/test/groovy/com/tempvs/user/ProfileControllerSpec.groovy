package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ProfileController)
class ProfileControllerSpec extends Specification {

    private static final String IDENTIFIER = 'identifier'
    private static final String USER_PROFILE = 'userProfile'
    private static final String ONE = '1'
    private static final String ID = 'id'
    private static final String USER = 'user'
    private static final String EMAIL = 'email'
    private static final String PROFILE_EMAIL = 'profileEmail'
    private static final String CLASS = 'class'
    private static final String PROPERTIES = 'properties'
    private static final String AVATAR_IMAGE = 'avatarImage'
    private static final String DIFFERENT_EMAIL = 'differentEmail'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'
    private static final String PROFILE_UPDATED_MESSAGE = 'profile.updated.message'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'profileEmail.verification.sent.message'
    private static final String AVATAR_UPDATED_MESSAGE = 'userProfile.update.avatar.success.message'
    private static final String AUTH_URL = '/auth/index'
    private static final String EDIT_PROFILE_PAGE = '/profile/edit'
    private static final String PROFILE_PAGE = '/profile/index'
    private static final String EDIT_CLUB_PROFILE_PAGE = '/profile/editClubProfile'

    def userService = Mock(UserService)
    def imageService = Mock(ImageService)
    def profileHolder = Mock(ProfileHolder)
    def verifyService = Mock(VerifyService)
    def profileService = Mock(ProfileService)
    def springSecurityService = Mock(SpringSecurityService)
    def ajaxResponseService = Mock(AjaxResponseService)
    def json = Mock(JSON)
    def user = Mock(User)
    def image = Mock(Image)
    def userProfile = Mock(UserProfile)
    def clubProfile = Mock(ClubProfile)
    def emailVerification = Mock(EmailVerification)
    def clubProfileCommand = Mock(ClubProfileCommand)
    def userProfileCommand = Mock(UserProfileCommand)
    def profileAvatarCommand = Mock(ProfileAvatarCommand)
    def multipartFile = new MockMultipartFile(AVATAR_IMAGE, "1234567" as byte[])

    def setup() {
        controller.profileService = profileService
        controller.profileHolder = profileHolder
        controller.springSecurityService = springSecurityService
        controller.ajaxResponseService = ajaxResponseService
        controller.userService = userService
        controller.verifyService = verifyService
        controller.imageService = imageService
    }

    def cleanup() {
    }

    void "Test index()"() {
        when:
        controller.index()

        then:
        1 * profileHolder.profile >> userProfile
        1 * userProfile.getProperty(CLASS) >> UserProfile.class
        1 * userProfile.getProperty(IDENTIFIER) >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl.contains "/profile/userProfile/${IDENTIFIER}"
    }

    void "Test userProfile() for existent profile"() {
        when: 'No id given'
        controller.userProfile()

        then: 'For existent profile'
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.getProperty(IDENTIFIER) >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl == "/profile/userProfile/${IDENTIFIER}"

        when: 'Id given'
        params.id = ONE
        def result = controller.userProfile()

        then: 'For existent profile'
        1 * profileService.getProfile(UserProfile.class, ONE) >> userProfile
        1 * userProfile.getProperty(IDENTIFIER) >> IDENTIFIER
        0 * _

        and:
        result == [profile: userProfile, id: IDENTIFIER]
    }

    void "Test userProfile() for non-existent profile"() {
        when: 'No id given'
        controller.userProfile()

        then: 'For non logged in user'
        1 * springSecurityService.currentUser >> null
        0 * _

        and:
        response.redirectedUrl == AUTH_URL

        when: 'Id given'
        params.id = ONE
        def result = controller.userProfile()

        then: 'For non-existing profile'
        1 * profileService.getProfile(UserProfile.class, ONE) >> null
        0 * _

        and:
        result == [id: ONE, message: NO_SUCH_PROFILE, args: [ONE]]
    }

    void "Test switchProfile() being not logged in"() {
        when:
        controller.switchProfile()

        then:
        1 * springSecurityService.currentUser >> null
        0 * _

        and:
        response.redirectedUrl == AUTH_URL
    }

    void "Test switchProfile() being logged in without id"() {
        when:
        controller.switchProfile()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * profileHolder.setProfile(userProfile)
        0 * _

        and:
        response.redirectedUrl == PROFILE_PAGE
    }

    void "Test switchProfile() being logged in with id"() {
        when:
        params.id = ONE
        controller.switchProfile()

        then:
        1 * profileService.getProfile(_, ONE) >> userProfile
        1 * profileHolder.setProfile(userProfile)
        0 * _

        and:
        response.redirectedUrl == PROFILE_PAGE
    }

    void "Test edit()"() {
        when:
        controller.edit()

        then:
        1 * profileHolder.profile >> clubProfile
        1 * clubProfile.getProperty(CLASS) >> ClubProfile.class
        0 * _

        and:
        controller.modelAndView.model == [profile: clubProfile]
        controller.modelAndView.viewName == EDIT_CLUB_PROFILE_PAGE
        response.redirectedUrl == null
    }

    void "Test list()"() {
        when:
        def result = controller.list()

        then:
        1 * springSecurityService.currentUser >> user
        0 * _

        and:
        result == [user: user]
    }

    void "Test create()"() {
        given:
        params.isAjaxRequest = Boolean.TRUE

        when: 'Command invalid'
        controller.create(clubProfileCommand)

        then:
        1 * clubProfileCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(clubProfileCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _

        when: 'Command valid. Profile not created.'
        controller.create(clubProfileCommand)

        then:
        1 * clubProfileCommand.validate() >> Boolean.TRUE
        1 * clubProfileCommand.getProperty(PROPERTIES)
        1 * profileService.createClubProfile(_) >> null
        1 * ajaxResponseService.composeJsonResponse(clubProfileCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _

        when: 'Command valid. Profile created.'
        controller.create(clubProfileCommand)

        then:
        1 * clubProfileCommand.validate() >> Boolean.TRUE
        1 * clubProfileCommand.getProperty(PROPERTIES)
        1 * profileService.createClubProfile(_) >> clubProfile
        1 * profileHolder.setProfile(clubProfile)
        0 * _

        and:
        response.json.redirect == EDIT_PROFILE_PAGE
    }

    void "Test updateClubProfile()"() {
        when:
        controller.updateClubProfile(clubProfileCommand)

        then:
        1 * clubProfileCommand.getProperty(PROPERTIES)
        1 * profileHolder.getProfile() >> clubProfile
        1 * profileService.updateProfile(clubProfile, _) >> clubProfile
        1 * ajaxResponseService.composeJsonResponse(clubProfile, PROFILE_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test updateUserProfile()"() {
        when:
        controller.updateUserProfile(userProfileCommand)

        then:
        1 * userProfileCommand.getProperty(PROPERTIES) >> [:]
        1 * profileHolder.getProfile() >> userProfile
        1 * profileService.updateProfile(userProfile, _) >> userProfile
        1 * ajaxResponseService.composeJsonResponse(userProfile, PROFILE_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test updateProfileEmail() for duplicate"() {
        when: 'Email duplicate'
        params.profileEmail = EMAIL
        controller.updateProfileEmail()

        then:
        1 * profileHolder.getProfile() >> userProfile
        1 * userProfile.getProperty(PROFILE_EMAIL) >> EMAIL
        0 * _

        and:
        response.json.messages == [EMAIL_UPDATE_DUPLICATE]
    }

    void "Test updateProfileEmail() for non-unique entry"() {
        when: 'Email non-unique'
        params.profileEmail = EMAIL
        controller.updateProfileEmail()

        then:
        1 * profileHolder.getProfile() >> clubProfile
        1 * clubProfile.getProperty(PROFILE_EMAIL) >> DIFFERENT_EMAIL
        1 * userService.isEmailUnique(EMAIL) >> Boolean.FALSE
        0 * _

        and:
        response.json.messages == [EMAIL_USED]
    }

    void "Test updateProfileEmail()"() {
        when: 'Email unique'
        params.profileEmail = EMAIL
        controller.updateProfileEmail()

        then:
        1 * profileHolder.getProfile() >> userProfile
        1 * userProfile.getProperty(PROFILE_EMAIL) >> DIFFERENT_EMAIL
        1 * userService.isEmailUnique(EMAIL) >> Boolean.TRUE
        1 * userProfile.getProperty(ID) >> ID
        1 * userProfile.getProperty(CLASS) >> UserProfile.class
        1 * verifyService.createEmailVerification(_) >> emailVerification
        1 * ajaxResponseService.composeJsonResponse(emailVerification, UPDATE_PROFILE_EMAIL_MESSAGE_SENT) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test updateAvatar()"() {
        when:
        controller.updateAvatar(profileAvatarCommand)

        then: 'JSON response received'
        1 * profileAvatarCommand.validate() >> Boolean.TRUE
        1 * profileAvatarCommand.getProperty(AVATAR_IMAGE) >> multipartFile
        1 * profileHolder.profile >> userProfile
        1 * userProfile.getProperty(USER) >> user
        1 * user.getProperty(ID) >> 1
        1 * profileHolder.clazz >> UserProfile.class
        1 * userProfile.getProperty(ID) >> 1
        1 * imageService.createImage(_ as ByteArrayInputStream, _ as String, _ as Map) >> image
        1 * image.getId() >> ONE
        1 * userProfile.setAvatar(ONE)
        1 * userProfile.save([flush: true])
        1 * ajaxResponseService.renderMessage(Boolean.TRUE, AVATAR_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test updateAvatar() without file"() {
        when:
        controller.updateAvatar(profileAvatarCommand)

        then:
        1 * profileAvatarCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(profileAvatarCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }
}
