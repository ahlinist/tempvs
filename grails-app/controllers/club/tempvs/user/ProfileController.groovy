package club.tempvs.user

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.communication.FollowingService
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.image.ImageUploadBean
import club.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import org.springframework.security.access.annotation.Secured
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException

/**
 * Controller for managing {@link Profile}.
 */
@Secured('isAuthenticated()')
@GrailsCompileStatic
class ProfileController {

    private static final String NO_ACTION = 'none'
    private static final String REFERER = 'referer'
    private static final String SUCCESS_ACTION = 'success'
    private static final String AVATAR_COLLECTION = 'avatar'
    private static final String FILL_DROPDOWN = 'fillDropdown'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String PROFILE_EMAIL_FIELD = 'profileEmail'
    private static final String PROFILE_EMAIL_ACTION = 'profileEmail'
    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'
    private static final String EDIT_PROFILE_EMAIL_MESSAGE_SENT = 'profileEmail.verification.sent.message'
    private static final String EDIT_PROFILE_EMAIL_FAILED_MESSAGE = 'profileEmail.verification.failed.message'

    static allowedMethods = [
            index: 'GET',
            search: 'GET',
            show: 'GET',
            switchProfile: 'GET',
            createClubProfile: 'POST',
            editProfileField: 'POST',
            editProfileEmail: 'POST',
            activateProfile: 'POST',
            deactivateProfile: 'POST',
            deleteAvatar: 'DELETE',
            uploadAvatar: 'POST',
            getProfileDropdown: 'GET',
    ]

    UserService userService
    ImageService imageService
    VerifyService verifyService
    ProfileService profileService
    PageRenderer groovyPageRenderer
    FollowingService followingService
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    def index() {
        Profile profile = profileService.currentProfile
        redirect action: 'show', id: profile.identifier
    }

    def search(String query, Integer offset) {
        List<Profile> profiles = profileService.searchProfiles(profileService.currentProfile, query, offset)
        Map model = [profiles: profiles]
        String template = groovyPageRenderer.render(template: '/profile/templates/profileSearchResult', model: model)
        render([action: profiles ? REPLACE_ACTION : NO_ACTION, template: template] as JSON)
    }

    @Secured('permitAll')
    def show(String id) {
        Profile currentProfile = profileService.currentProfile

        if (!id) {
            return redirect(currentProfile ? [action: 'show', id: currentProfile.identifier] : [controller: 'auth', action: 'index'])
        }

        Profile profile = profileService.getProfile(id)

        if (!profile) {
            return [id: id, notFoundMessage: NO_SUCH_PROFILE]
        }

        User user = profile.user
        List<Profile> profiles = user.profiles

        if (profile.ofUserType) {
            List<Profile> clubProfiles = profiles.findAll {it.type == ProfileType.CLUB}

            [
                    profile: profile,
                    user: user,
                    id: profile.identifier,
                    editAllowed: currentProfile == profile,
                    mayBeFollowed: followingService.mayBeFollowed(currentProfile, profile),
                    isFollowed: followingService.getFollowing(currentProfile, profile) as Boolean,
                    activeProfiles: clubProfiles.findAll { it.active },
                    inactiveProfiles: clubProfiles.findAll { !it.active },
                    periods: Period.values(),
            ]
        } else if (profile.ofClubType) {
            Profile userProfile = profiles.find {it.type == ProfileType.USER}

            [
                    profile: profile,
                    user: user,
                    userProfile: userProfile,
                    id: profile.identifier,
                    passports: profile.passports,
                    active: profile.active,
                    editAllowed: currentProfile == profile,
                    mayBeFollowed: followingService.mayBeFollowed(currentProfile, profile),
                    isFollowed: followingService.getFollowing(currentProfile, profile) as Boolean,
                    periods: Period.values(),
            ]
        }
    }

    def switchProfile(Long id) {
        User currentUser = userService.currentUser
        List<Profile> profiles = currentUser?.profiles
        Profile profile = id ? profiles?.find { it.id == id } : profiles.find { it.type == ProfileType.USER}

        if (profile.active) {
            profileService.currentProfile = profile
        }

        redirect uri: request.getHeader(REFERER)
    }

    def createClubProfile(Profile profile, ImageUploadBean imageUploadBean) {
        if (!imageUploadBean.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBean))
        }

        User user = userService.currentUser
        profile = profileService.validateClubProfile(profile, user)
        profile = profileService.validateClubProfile(profile, user)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        Image avatar = imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION)
        profile = profileService.createClubProfile(profile, avatar)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        profileService.currentProfile = profile
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'profile', action: 'club', id: profile.id))
    }

    def editProfileField() {
        Profile profile = profileService.editProfileField(profileService.currentProfile, params.fieldName as String, params.fieldValue as String)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        render([action: SUCCESS_ACTION] as JSON)
    }

    def editProfileEmail() {
        String email = params.fieldValue
        Profile profile = profileService.currentProfile

        if (!email) {
            Profile persistedProfile = profileService.editProfileField(profile, PROFILE_EMAIL_FIELD, null)

            if (persistedProfile.hasErrors()) {
                return render(ajaxResponseHelper.renderValidationResponse(persistedProfile))
            }

            return render([action: SUCCESS_ACTION] as JSON)
        }

        Map properties = [
                instanceId: profile.id,
                email: email,
                action: PROFILE_EMAIL_ACTION,
        ]

        EmailVerification emailVerification = verifyService.createEmailVerification(properties as EmailVerification)

        if (emailVerification.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(emailVerification, EDIT_PROFILE_EMAIL_FAILED_MESSAGE))
        }

        Boolean success = verifyService.sendEmailVerification(emailVerification)
        String message = success ? EDIT_PROFILE_EMAIL_MESSAGE_SENT : EDIT_PROFILE_EMAIL_FAILED_MESSAGE

        render ajaxResponseHelper.renderFormMessage(success, message)
    }

    def deactivateProfile(String id) {
        Profile profile = profileService.getProfile(id)

        if (!profile) {
            return render([action: NO_ACTION] as JSON)
        }

        if (profileService.currentProfile == profile) {
            profileService.currentProfile = null
        }

        profile = profileService.deactivateProfile(profile)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        render ajaxResponseHelper.renderRedirect(request.getHeader(REFERER))
    }

    def activateProfile(String id) {
        Profile profile = profileService.getProfile(id)

        if (!profile || profile.active) {
            return render([action: NO_ACTION] as JSON)
        }

        profile = profileService.activateProfile(profile)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        render ajaxResponseHelper.renderRedirect(request.getHeader(REFERER))
    }

    def deleteAvatar() {
        Profile profile = profileService.getProfile(params.profileId)
        profileService.deleteAvatar(profile)
        render ajaxResponseHelper.renderRedirect(request.getHeader(REFERER))
    }

    def uploadAvatar(ImageUploadBean imageUploadBean) {
        if (!imageUploadBean.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBean))
        }

        Profile profile = profileService.currentProfile
        Image avatarForUpload = imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION)

        if (!avatarForUpload) {
            return render([action: NO_ACTION] as JSON)
        }

        Image currentAvatar = profile.avatar

        if (currentAvatar) {
            imageService.deleteImage(currentAvatar)
        }

        profile = profileService.uploadAvatar(profile, avatarForUpload)

        if (profile.hasErrors()) {
            imageService.deleteImage(avatarForUpload)
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        Map model = [profile: profile, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/profile/templates/avatar', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def getProfileDropdown() {
        Map body = profileService.getProfileDropdown()
        Map result

        if (body) {
            result = [action: FILL_DROPDOWN, body: body]
        } else {
            result = [action: NO_ACTION]
        }

        render(result as JSON)
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect grailsLinkGenerator.link(controller: 'auth')
        }
    }
}
