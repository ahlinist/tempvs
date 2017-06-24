package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import grails.converters.JSON
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ProfileController)
class ProfileControllerSpec extends Specification {

    private static final String IDENTIFIER = 'identifier'
    private static final String ONE = '1'
    private static final Long LONG_ID = 1L
    private static final String EMAIL = 'email'
    private static final String PROPERTIES = 'properties'
    private static final String AVATAR_IMAGE = 'avatarImage'
    private static final String DIFFERENT_EMAIL = 'differentEmail'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'
    private static final String PROFILE_UPDATED_MESSAGE = 'profile.updated.message'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'profileEmail.verification.sent.message'
    private static final String AVATAR_UPDATED_MESSAGE = 'profile.update.avatar.success.message'
    private static final String AUTH_URL = '/auth/index'
    private static final String EDIT_PROFILE_PAGE = '/profile/edit'
    private static final String PROFILE_URL = '/profile/index'
    private static final String USER_PROFILE_PAGE_URI = '/profile/userProfile'
    private static final String EDIT_CLUB_PROFILE_PAGE = '/profile/editClubProfile'
    private static final String IMAGE_EMPTY = 'image.empty'

    def userService = Mock(UserService)

    def profileHolder = Mock(ProfileHolder)
    def verifyService = Mock(VerifyService)
    def profileService = Mock(ProfileService)
    def ajaxResponseService = Mock(AjaxResponseService)
    def json = Mock(JSON)
    def user = Mock(User)
    def image = Mock(Image)
    def userProfile = Mock(UserProfile)
    def clubProfile = Mock(ClubProfile)
    def emailVerification = Mock(EmailVerification)
    def clubProfileCommand = Mock(ClubProfileCommand)
    def userProfileCommand = Mock(UserProfileCommand)
    def messageSource = Mock(MessageSource)

    def setup() {
        controller.profileService = profileService
        controller.profileHolder = profileHolder
        controller.ajaxResponseService = ajaxResponseService
        controller.userService = userService
        controller.verifyService = verifyService
        controller.messageSource = messageSource
    }

    def cleanup() {
    }

    void "Test index()"() {
        when:
        controller.index()

        then:
        1 * profileHolder.profile >> userProfile
        1 * userProfile.getIdentifier() >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl.contains USER_PROFILE_PAGE_URI
    }

    void "Test userProfile() for existent profile"() {
        when: 'No id given'
        controller.userProfile()

        then: 'For existent profile'
        1 * userService.currentUser >> user
        1 * user.getUserProfile()  >> userProfile
        1 * userProfile.getIdentifier() >> IDENTIFIER
        0 * _

        and:
        response.redirectedUrl == "/profile/userProfile/${IDENTIFIER}"

        when: 'Id given'
        params.id = ONE
        def result = controller.userProfile()

        then: 'For existent profile'
        1 * profileService.getProfile(UserProfile.class, ONE) >> userProfile
        1 * userProfile.asType(BaseProfile.class) >> userProfile
        1 * userProfile.getIdentifier() >> IDENTIFIER
        0 * _

        and:
        result == [profile: userProfile, id: IDENTIFIER]
    }

    void "Test userProfile() for non-existent profile"() {
        when:
        controller.userProfile()

        then:
        1 * userService.currentUser
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
        1 * userService.currentUser
        0 * _

        and:
        response.redirectedUrl == AUTH_URL
    }

    void "Test switchProfile() being logged in without id"() {
        when:
        controller.switchProfile()

        then:
        1 * userService.currentUser >> user
        1 * user.getUserProfile() >> userProfile
        1 * profileHolder.setProfile(userProfile)
        0 * _

        and:
        response.redirectedUrl == PROFILE_URL
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
        response.redirectedUrl == PROFILE_URL
    }

    void "Test edit()"() {
        when:
        controller.edit()

        then:
        1 * profileHolder.profile >> clubProfile
        0 * _

        and:
        controller.modelAndView.model == [profile: clubProfile]
        controller.modelAndView.viewName.contains EDIT_CLUB_PROFILE_PAGE
        response.redirectedUrl == null
    }

    void "Test list()"() {
        when:
        def result = controller.list()

        then:
        1 * userService.currentUser >> user
        0 * _

        and:
        result == [user: user]
    }

    void "Test create()"() {
        when: 'Command invalid'
        params.isAjaxRequest = Boolean.TRUE
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
        7 * clubProfileCommand._
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
        5 * userProfileCommand._
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
        1 * userProfile.getProfileEmail() >> EMAIL
        1 * messageSource.getMessage(EMAIL_UPDATE_DUPLICATE, null, EMAIL_UPDATE_DUPLICATE, LocaleContextHolder.locale) >> EMAIL_UPDATE_DUPLICATE
        0 * _

        and:
        response.json.messages == [EMAIL_UPDATE_DUPLICATE]
    }

    void "Test updateProfileEmail() for non-unique entry"() {
        when:
        params.profileEmail = EMAIL
        controller.updateProfileEmail()

        then:
        1 * profileHolder.getProfile() >> clubProfile
        1 * clubProfile.getProfileEmail() >> DIFFERENT_EMAIL
        1 * userService.isEmailUnique(EMAIL) >> Boolean.FALSE
        1 * messageSource.getMessage(EMAIL_USED, null, EMAIL_USED, LocaleContextHolder.locale) >> EMAIL_USED
        0 * _

        and:
        response.json.messages == [EMAIL_USED]
    }

    void "Test updateProfileEmail()"() {
        when:
        params.profileEmail = EMAIL
        controller.updateProfileEmail()

        then:
        1 * profileHolder.getProfile() >> userProfile
        1 * userProfile.getProfileEmail() >> DIFFERENT_EMAIL
        1 * userService.isEmailUnique(EMAIL) >> Boolean.TRUE
        1 * userProfile.getId() >> LONG_ID
        1 * verifyService.createEmailVerification(_) >> emailVerification
        1 * ajaxResponseService.composeJsonResponse(emailVerification, UPDATE_PROFILE_EMAIL_MESSAGE_SENT) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test updateAvatar()"() {
        given:
        def avatar = new MockMultipartFile(AVATAR_IMAGE, "1234567" as byte[])
        controller.request.addFile(avatar)

        when:
        controller.updateAvatar()

        then:
        1 * profileHolder.profile >> userProfile
        1 * profileService.updateAvatar(userProfile, avatar) >> userProfile
        1 * ajaxResponseService.composeJsonResponse(userProfile, AVATAR_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test updateAvatar() without file"() {
        given:
        def avatar = new MockMultipartFile(AVATAR_IMAGE, "" as byte[])
        controller.request.addFile(avatar)

        when:
        controller.updateAvatar()

        then:
        1 * ajaxResponseService.renderMessage(Boolean.FALSE, IMAGE_EMPTY) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test deleteProfile()"() {
        given:
        params.isAjaxRequest = Boolean.TRUE
        params.id = ONE

        when:
        controller.deleteProfile()

        then:
        1 * userService.currentUserId >> LONG_ID
        1 * profileService.getProfile(ClubProfile.class, ONE) >> clubProfile
        1 * clubProfile.user >> user
        1 * user.id >> LONG_ID
        1 * profileService.deleteProfile(clubProfile) >> Boolean.TRUE
        1 * profileHolder.setProfile(null)
        0 * _

        and:
        !controller.modelAndView
        response.json.redirect == PROFILE_URL
    }
}
