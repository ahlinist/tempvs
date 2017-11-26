package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException

/**
 * Controller for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
class ProfileController {

    private static final String SUCCESS_ACTION = 'success'
    private static final String DELETE_ACTION = 'deleteElement'
    private static final String PROFILE_EMAIL_FIELD = 'profileEmail'
    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'
    private static final String EDIT_PROFILE_EMAIL_MESSAGE_SENT = 'profileEmail.verification.sent.message'

    static allowedMethods = [
            index: 'GET',
            userProfile: 'GET',
            clubProfile: 'GET',
            switchProfile: 'GET',
            list: 'GET',
            createClubProfile: 'POST',
            editProfileField: 'POST',
            editProfileEmail: 'POST',
            deleteProfile: 'DELETE',
            deleteAvatar: 'DELETE',
            uploadAvatar: 'POST'
    ]

    UserService userService
    ImageService imageService
    ProfileHolder profileHolder
    VerifyService verifyService
    ProfileService profileService
    LinkGenerator grailsLinkGenerator
    AjaxResponseService ajaxResponseService

    def index() {
        BaseProfile profile = profileHolder.profile
        redirect action: "${profile.class.simpleName}", id: profile.identifier
    }

    def userProfile(String id) {
        profile(id, UserProfile)
    }

    def clubProfile(String id) {
        profile(id, ClubProfile)
    }

    def switchProfile(String id) {
        Map destination = [uri: request.getHeader('referer')]

        if (id) {
            profileHolder.profile = profileService.getProfile(ClubProfile, id)
        } else {
            User user = userService.currentUser

            user ? (profileHolder.profile = user.userProfile) : (destination = [controller: 'auth', action: 'index'])
        }

        redirect destination
    }

    def createClubProfile(ClubProfileCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseService.renderValidationResponse(command))
        }

        Map properties = command.properties + [user: userService.currentUser]
        BaseProfile profile = profileService.createProfile(properties as ClubProfile, command.imageUploadBean)

        if (profile.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(profile))
        }

        profileHolder.profile = profile
        render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'profile', action: 'clubProfile', id: profile.id))
    }

    def editProfileField() {
        BaseProfile profile = profileService.editProfileField(profileHolder.profile, params.fieldName as String, params.fieldValue as String)

        if (profile.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(profile))
        }

        render([action: SUCCESS_ACTION] as JSON)
    }

    def editProfileEmail() {
        String email = params.fieldValue
        BaseProfile profile = profileHolder.profile

        if (email) {
            Map properties = [
                    instanceId: profile.id,
                    email: email,
                    action: profile.class.simpleName.uncapitalize(),
            ]

            EmailVerification emailVerification = verifyService.createEmailVerification(properties as EmailVerification)

            if (!emailVerification.hasErrors()) {
                verifyService.sendEmailVerification(emailVerification)
            }

            render ajaxResponseService.renderValidationResponse(emailVerification, EDIT_PROFILE_EMAIL_MESSAGE_SENT)
        } else {
            BaseProfile persistedProfile = profileService.editProfileField(profile, PROFILE_EMAIL_FIELD, null)

            if (persistedProfile.hasErrors()) {
                return render(ajaxResponseService.renderValidationResponse(persistedProfile))
            }

            render([action: SUCCESS_ACTION] as JSON)
        }
    }

    def deleteProfile(String id) {
        profileService.deleteProfile(profileService.getProfile(ClubProfile.class, id))
        profileHolder.profile = null
        render([action: DELETE_ACTION] as JSON)
    }

    def deleteAvatar() {
        BaseProfile profile = profileService.getProfile(params.profileClass as Class, params.profileId)
        profileService.deleteAvatar(profile)
        render ajaxResponseService.renderRedirect(request.getHeader('referer'))
    }

    def uploadAvatar(ImageUploadBean imageUploadBean) {
        BaseProfile profile = profileService.uploadAvatar(profileHolder.profile, imageUploadBean)

        if (profile.hasErrors()) {
            return render(ajaxResponseService.renderValidationResponse(profile))
        }

        render ajaxResponseService.renderRedirect(request.getHeader('referer'))
    }

    def list() {
        User user = userService.currentUser
        [userProfile: user.userProfile, clubProfiles: user.clubProfiles]
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect grailsLinkGenerator.link(controller: 'auth')
        }
    }

    private profile(String id, Class clazz) {
        if (id) {
            BaseProfile profile = profileService.getProfile(clazz, id)

            if (profile) {
                [profile: profile, user: profile.user, id: profile.identifier, editAllowed: profileHolder.profile == profile]
            } else {
                [id: id, notFoundMessage: NO_SUCH_PROFILE]
            }
        } else {
            User user = userService.currentUser
            UserProfile profile = user?.userProfile

            redirect(profile ? [action: 'userProfile', id: profile.identifier] : [controller: 'auth', action: 'index'])
        }
    }
}
