package com.tempvs.user

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.communication.FollowingService
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
    private static final String REFERER = 'referer'
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
            activateProfile: 'POST',
            deactivateProfile: 'POST',
            deleteAvatar: 'DELETE',
            uploadAvatar: 'POST'
    ]

    UserService userService
    ImageService imageService
    VerifyService verifyService
    ProfileService profileService
    PassportService passportService
    PageRenderer groovyPageRenderer
    FollowingService followingService
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    def index() {
        Profile profile = profileService.currentProfile
        redirect action: "${profile.class.simpleName.uncapitalize()}", id: profile.identifier
    }

    def userProfile(String id) {
        if (!id) {
            UserProfile profile = userService.currentUser?.userProfile
            return redirect(profile ? [action: 'userProfile', id: profile.identifier] : [controller: 'auth', action: 'index'])
        }

        UserProfile userProfile = profileService.getProfile(UserProfile, id)

        if (!userProfile) {
            return [id: id, notFoundMessage: NO_SUCH_PROFILE]
        }

        Profile currentProfile = profileService.currentProfile

        [
                profile: userProfile,
                user: userProfile.user,
                id: userProfile.identifier,
                editAllowed: currentProfile == userProfile,
                mayBeFollowed: followingService.mayBeFollowed(currentProfile, userProfile),
                isFollowed: followingService.getFollowing(currentProfile, userProfile) as Boolean,
        ]
    }

    def clubProfile(String id) {
        if (!id) {
            return redirect(controller: 'auth', action: 'index')
        }

        ClubProfile clubProfile = profileService.getProfile(ClubProfile, id)

        if (!clubProfile) {
            return [id: id, notFoundMessage: NO_SUCH_PROFILE]
        }

        Profile currentProfile = profileService.currentProfile

        [
                profile: clubProfile,
                user: clubProfile.user,
                id: clubProfile.identifier,
                passports: clubProfile.passports,
                editAllowed: currentProfile == clubProfile,
                mayBeFollowed: followingService.mayBeFollowed(currentProfile, clubProfile),
                isFollowed: followingService.getFollowing(currentProfile, clubProfile) as Boolean,
        ]
    }

    def switchProfile(Long id) {
        Profile profile = id ? profileService.getProfile(ClubProfile, id) : userService.currentUser?.userProfile


        if (profile.active) {
            profileService.currentProfile = profile
        }

        redirect uri: request.getHeader(REFERER)
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

        profileService.currentProfile = profile
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'profile', action: 'clubProfile', id: profile.id))
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

    def deactivateProfile(String id) {
        ClubProfile clubProfile = profileService.getProfile(ClubProfile, id)

        if (!clubProfile) {
            return render([action: NO_ACTION] as JSON)
        }

        if (profileService.currentProfile == clubProfile) {
            profileService.setCurrentProfile(null)
        }

        Profile profile = profileService.deactivateProfile(clubProfile)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        User user = clubProfile.user
        Collection<ClubProfile> clubProfiles = user.clubProfiles
        Map model = [activeProfiles: clubProfiles.findAll {it.active}, inactiveProfiles: clubProfiles.findAll {!it.active}, editAllowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/profile/templates/clubProfileList', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def activateProfile(String id) {
        ClubProfile clubProfile = profileService.getProfile(ClubProfile, id)

        if (!clubProfile || clubProfile.active) {
            return render([action: NO_ACTION] as JSON)
        }

        Profile profile = profileService.activateProfile(clubProfile)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        render ajaxResponseHelper.renderRedirect(request.getHeader(REFERER))
    }

    def deleteAvatar() {
        Profile profile = profileService.getProfile(params.profileClass as Class, params.profileId)
        profileService.deleteAvatar(profile)
        render ajaxResponseHelper.renderRedirect(request.getHeader(REFERER))
    }

    def uploadAvatar(ImageUploadBean imageUploadBean) {
        Profile profile = profileService.currentProfile
        Image avatar = imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION, profile.avatar)
        Profile persistedProfile = profileService.uploadAvatar(profile, avatar)

        if (persistedProfile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(persistedProfile))
        }

        render ajaxResponseHelper.renderRedirect(request.getHeader(REFERER))
    }

    def list() {
        User user = userService.currentUser
        Collection<ClubProfile> clubProfiles = user.clubProfiles
        [userProfile: user.userProfile, activeProfiles: clubProfiles.findAll {it.active}, inactiveProfiles: clubProfiles.findAll {!it.active}]
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect grailsLinkGenerator.link(controller: 'auth')
        }
    }
}
