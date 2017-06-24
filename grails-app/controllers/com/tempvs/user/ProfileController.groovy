package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.web.mapping.LinkGenerator
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

/**
 * Controller for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
class ProfileController {

    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'profileEmail.verification.sent.message'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String PROFILE_DELETION_FAILED = 'profile.delete.failed.message'
    private static final String IMAGE_EMPTY = 'image.empty'
    private static final String AVATAR_IMAGE = 'avatarImage'

    AjaxResponseService ajaxResponseService
    ProfileHolder profileHolder
    ProfileService profileService
    VerifyService verifyService
    UserService userService
    LinkGenerator grailsLinkGenerator
    MessageSource messageSource

    def index() {
        BaseProfile profile = profileHolder.profile
        redirect action: "${profile.class.simpleName}", id: profile.identifier
    }

    def userProfile(String id) {
        profile(id) {
            profileService.getProfile(UserProfile.class, id)
        }
    }

    def clubProfile(String id) {
        profile(id) {
            profileService.getProfile(ClubProfile.class, id)
        }
    }

    def switchProfile(String id) {
        Map destination = [uri: request.getHeader('referer')]

        if (id) {
            profileHolder.profile = profileService.getProfile(ClubProfile.class, id)
        } else {
            User user = userService.currentUser

            user ? (profileHolder.profile = user.userProfile) : (destination = [controller: 'auth', action: 'index'])
        }

        redirect destination
    }

    def list() {
        [user: userService.currentUser]
    }

    def create(ClubProfileCommand command) {
        if (params.isAjaxRequest) {
            if (command?.validate()) {
                ClubProfile clubProfile = profileService.createClubProfile(command.properties)

                if (clubProfile) {
                    profileHolder.profile = clubProfile
                    render([redirect: grailsLinkGenerator.link(controller: 'profile', action: 'edit')] as JSON)
                } else {
                    render ajaxResponseService.composeJsonResponse(command)
                }
            } else {
                render ajaxResponseService.composeJsonResponse(command)
            }
        }
    }

    def updateUserProfile(UserProfileCommand command) {
        updateProfile(command)
    }

    def updateClubProfile(ClubProfileCommand command) {
        updateProfile(command)
    }

    def updateProfileEmail(String profileEmail) {
        BaseProfile profile = profileHolder.profile

        if (profileEmail == profile.profileEmail) {
            String message = messageSource.getMessage(EMAIL_UPDATE_DUPLICATE, null, EMAIL_UPDATE_DUPLICATE, LocaleContextHolder.locale)
            render([messages: [message]] as JSON)
        } else {
            if (!userService.isEmailUnique(profileEmail)) {
                String message = messageSource.getMessage(EMAIL_USED, null, EMAIL_USED, LocaleContextHolder.locale)
                render([messages: [message]] as JSON)
            } else {
                Map props = [instanceId: profile.id,
                             email: profileEmail,
                             action: profile.class.simpleName.toLowerCase()]
                render ajaxResponseService.composeJsonResponse(verifyService.createEmailVerification(props), UPDATE_PROFILE_EMAIL_MESSAGE_SENT)
            }
        }
    }
    
    def updateAvatar() {
	    MultipartFile multipartAvatar = ((MultipartHttpServletRequest) request).getFile(AVATAR_IMAGE)

        if (multipartAvatar.empty) {
            render ajaxResponseService.renderMessage(Boolean.FALSE, IMAGE_EMPTY)
        } else {
            BaseProfile profile = profileService.updateAvatar(profileHolder.profile, multipartAvatar)

            if (profile.validate()) {
                render([redirect: request.getHeader('referer')] as JSON)
            } else {
                render ajaxResponseService.composeJsonResponse(profile)
            }
        }
    }

    def deleteProfile(String id) {
        if (params.isAjaxRequest) {
            BaseProfile profile = profileService.getProfile(ClubProfile.class, id)
            Boolean success = Boolean.FALSE
            String message = PROFILE_DELETION_FAILED

            if (profile) {
                Long currentUserId = userService.currentUserId

                if (profile.user.id == currentUserId) {
                    if (profileService.deleteProfile(profile)) {
                        profileHolder.profile = null
                        success = Boolean.TRUE
                        render([redirect: grailsLinkGenerator.link(controller: 'profile')] as JSON)
                    }
                }
            }

            if (!success) {
                render ajaxResponseService.renderMessage(success, message)
            }
        }
    }

    private updateProfile(command) {
        BaseProfile profile = profileService.updateProfile(profileHolder.profile, command.properties)

        if (profile.validate()) {
            render([redirect: request.getHeader('referer')] as JSON)
        } else {
            render ajaxResponseService.composeJsonResponse(profile)
        }
    }

    private profile(String id, Closure getProfile) {
        if (id) {
            BaseProfile profile = getProfile.call() as BaseProfile

            profile ? [profile: profile, id: profile.identifier, editAllowed: profileHolder.profile == profile] : [id: id, message: NO_SUCH_PROFILE, args: [id]]
        } else {
            User user = userService.currentUser
            UserProfile profile = user?.userProfile

            redirect(profile ? [action: 'userProfile', id: profile.identifier] : [controller: 'auth', action: 'index'])
        }
    }
}
