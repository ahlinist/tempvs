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
import grails.validation.ValidationException
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
        User currentUser = userService.currentUser

        if (currentUser.userProfile) {
            redirect action: 'show', id: currentUser.currentProfile.identifier
        } else {
            render view: 'registration', model: [user: currentUser]
        }
    }

    def search(String query, Integer offset) {
        List<Profile> profiles = profileService.searchProfiles(profileService.currentProfile, query, offset)
        render(profiles.collect { Profile profile -> [id: profile.id, name: profile.toString()]} as JSON)
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

        if (profile.ofUserType) {
            List<Profile> clubProfiles = user.clubProfiles

            [
                    profile: profile,
                    user: user,
                    id: profile.identifier,
                    editAllowed: currentProfile == profile,
                    mayBeFollowed: followingService.mayBeFollowed(currentProfile, profile) as Boolean,
                    isFollowed: followingService.getFollowing(currentProfile, profile) as Boolean,
                    activeProfiles: clubProfiles.findAll { it.active },
                    inactiveProfiles: clubProfiles.findAll { !it.active },
                    periods: Period.values(),
                    currentProfile: currentProfile,
            ]
        } else if (profile.ofClubType) {
            Profile userProfile = user.userProfile

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
                    currentProfile: currentProfile,
            ]
        }
    }

    def switchProfile(Long id) {
        User user = userService.currentUser
        List<Profile> profiles = user?.profiles
        Profile profile = id ? profiles.find { it.id == id } : profiles.find { it.type == ProfileType.USER}

        if (profile?.active) {
            profileService.setCurrentProfile(user, profile)
        }

        redirect uri: request.getHeader(REFERER)
    }

    def createProfile(Profile profile, ImageUploadBean imageUploadBean) {
        if (!imageUploadBean.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBean))
        }

        Profile persistentProfile
        Image avatar = imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION)
        User user = userService.currentUser

        String profileEmail = profile.profileEmail
        profile.profileEmail = null

        try {
            persistentProfile = profileService.createProfile(user, profile, avatar)

            if (persistentProfile.hasErrors()) {
                throw new ValidationException("Profile has errors", persistentProfile.errors)
            }
        } catch (ValidationException e) {
            imageService.deleteImage(avatar)
            return render(ajaxResponseHelper.renderValidationResponse(e.errors))
        } catch (Exception e) {
            imageService.deleteImage(avatar)
        }

        if (profileEmail) {
            Map properties = [
                    instanceId: profile.id,
                    email: profileEmail,
                    action: PROFILE_EMAIL_ACTION,
            ]

            EmailVerification emailVerification = verifyService.createEmailVerification(properties as EmailVerification)
            verifyService.sendEmailVerification(emailVerification)
        }

        profileService.setCurrentProfile(user, persistentProfile)
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'profile', action: 'show', id: persistentProfile.id))
    }

    def editProfileField() {
        Profile profile

        try {
            profile = profileService.editProfileField(profileService.currentProfile, params.fieldName as String, params.fieldValue as String)

            if (profile.hasErrors()) {
                throw new ValidationException("Profile has errors", profile.errors)
            }
        } catch (ValidationException e) {
            return render(ajaxResponseHelper.renderValidationResponse(e.errors))
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
            User user = userService.currentUser
            profileService.setCurrentProfile(user, null)
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

        Image avatar = imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION)

        if (!avatar) {
            return render([action: NO_ACTION] as JSON)
        }

        Profile updatedProfile

        try {
            updatedProfile = profileService.uploadAvatar(profileService.currentProfile, avatar)

            if (updatedProfile.hasErrors()) {
                throw new ValidationException("Profile has errors", updatedProfile.errors)
            }
        } catch (ValidationException e) {
            imageService.deleteImage(avatar)
            return render(ajaxResponseHelper.renderValidationResponse(e.errors))
        } catch (Exception e) {
            imageService.deleteImage(avatar)
        }

        Map model = [profile: updatedProfile, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/profile/templates/avatar', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def getProfileDropdown() {
        User user = userService.currentUser
        Map body = profileService.getProfileDropdown(user)
        Map result = body ? [action: FILL_DROPDOWN, body: body] : [action: NO_ACTION]
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
