package com.tempvs.user

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.item.PassportService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException

/**
 * Controller for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
class ProfileController {

    private static final String NO_ACTION = 'none'
    private static final String SUCCESS_ACTION = 'success'
    private static final String AVATAR_COLLECTION = 'avatar'
    private static final String REPLACE_ACTION = 'replaceElement'
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
    PassportService passportService
    PageRenderer groovyPageRenderer
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    def index() {
        Profile profile = profileHolder.profile
        redirect action: "${profile.class.simpleName.uncapitalize()}", id: profile.identifier
    }

    def userProfile(String id) {
        if (!id) {
            UserProfile profile = userService.currentUser?.userProfile
            return redirect(profile ? [action: 'userProfile', id: profile.identifier] : [controller: 'auth', action: 'index'])
        }

        UserProfile profile = profileService.getProfile(UserProfile, id)

        if (!profile) {
            return [id: id, notFoundMessage: NO_SUCH_PROFILE]
        }

        [profile: profile, user: profile.user, id: profile.identifier, editAllowed: profileHolder.profile == profile]
    }

    def clubProfile(String id) {
        if (!id) {
            return redirect(controller: 'auth', action: 'index')
        }

        ClubProfile clubProfile = profileService.getProfile(ClubProfile, id)

        if (!clubProfile) {
            return [id: id, notFoundMessage: NO_SUCH_PROFILE]
        }

        Boolean editAllowed = (profileHolder.profile == clubProfile)
        [profile: clubProfile, user: clubProfile.user, id: clubProfile.identifier, passports: clubProfile.passports, editAllowed: editAllowed]
    }

    def switchProfile(Long id) {
        profileHolder.profile = id ? profileService.getProfile(ClubProfile, id) : userService.currentUser?.userProfile
        redirect uri: request.getHeader('referer')
    }

    def createClubProfile(ClubProfileCommand command) {
        if (!command.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(command))
        }

        Image avatar = imageService.uploadImage(command.imageUploadBean, AVATAR_COLLECTION)

        ClubProfile clubProfile = (command.properties + [user: userService.currentUser]) as ClubProfile
        Profile profile = profileService.createProfile(clubProfile, avatar)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        profileHolder.profile = profile
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'profile', action: 'clubProfile', id: profile.id))
    }

    def editProfileField() {
        Profile profile = profileService.editProfileField(profileHolder.profile, params.fieldName as String, params.fieldValue as String)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        render([action: SUCCESS_ACTION] as JSON)
    }

    def editProfileEmail() {
        String email = params.fieldValue
        Profile profile = profileHolder.profile

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

            render ajaxResponseHelper.renderValidationResponse(emailVerification, EDIT_PROFILE_EMAIL_MESSAGE_SENT)
        } else {
            Profile persistedProfile = profileService.editProfileField(profile, PROFILE_EMAIL_FIELD, null)

            if (persistedProfile.hasErrors()) {
                return render(ajaxResponseHelper.renderValidationResponse(persistedProfile))
            }

            render([action: SUCCESS_ACTION] as JSON)
        }
    }

    def deleteProfile(String id) {
        ClubProfile clubProfile = profileService.getProfile(ClubProfile.class, id)

        if (!clubProfile) {
            return render([action: NO_ACTION] as JSON)
        }

        User user = clubProfile.user

        if (profileHolder.profile == clubProfile) {
            profileHolder.setProfile(null)
        }

        profileService.deleteProfile(clubProfile)

        Map model = [clubProfiles: user.clubProfiles, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/profile/templates/clubProfileList', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def deleteAvatar() {
        Profile profile = profileService.getProfile(params.profileClass as Class, params.profileId)
        profileService.deleteAvatar(profile)
        render ajaxResponseHelper.renderRedirect(request.getHeader('referer'))
    }

    def uploadAvatar(ImageUploadBean imageUploadBean) {
        Profile profile = profileHolder.profile
        Image avatar = imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION, profile.avatar)
        Profile persistedProfile = profileService.uploadAvatar(profile, avatar)

        if (persistedProfile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(persistedProfile))
        }

        render ajaxResponseHelper.renderRedirect(request.getHeader('referer'))
    }

    def list() {
        User user = userService.currentUser
        [userProfile: user.userProfile, clubProfiles: user.clubProfiles]
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect grailsLinkGenerator.link(controller: 'auth')
        }
    }
}
