package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
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
    private static final String PROFILE_UPDATED_MESSAGE = 'profile.updated.message'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'profileEmail.verification.sent.message'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String AVATAR_UPDATED_MESSAGE = 'profile.update.avatar.success.message'
    private static final String AVATAR_UPDATE_FAILED_MESSAGE = 'profile.update.avatar.failed.message'
    private static final String IMAGE_EMPTY = 'image.empty'
    private static final String AVATAR_COLLECTION = 'avatar'
    private static final String AVATAR_IMAGE = 'avatarImage'

    SpringSecurityService springSecurityService
    AjaxResponseService ajaxResponseService
    ProfileHolder profileHolder
    ProfileService profileService
    VerifyService verifyService
    UserService userService
    ImageService imageService
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
            User user = springSecurityService.currentUser as User

            user ? (profileHolder.profile = user.userProfile) : (destination = [controller: 'auth', action: 'index'])
        }

        redirect destination
    }


    def edit() {
        BaseProfile profile = profileHolder.profile
        render view: "edit${profile.class.simpleName}", model: [profile: profile]
    }

    def list() {
        [user: springSecurityService.currentUser]
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
        Boolean success = Boolean.FALSE
        String message = AVATAR_UPDATE_FAILED_MESSAGE
	    MultipartFile multipartAvatar = ((MultipartHttpServletRequest) request).getFile(AVATAR_IMAGE)

        if (!multipartAvatar || multipartAvatar.empty) {
            message = IMAGE_EMPTY
            render ajaxResponseService.renderMessage(success, message)
        } else {
            BaseProfile profile = profileHolder.profile
            Map metaData = [userId: profile.user.id, properties: [profileClass: profileHolder.clazz.simpleName, profileId: profile.id]]
            InputStream inputStream = multipartAvatar.inputStream

            try {
                Image avatar = imageService.createImage(inputStream, AVATAR_COLLECTION, metaData)
                profile.avatar = avatar.id
                profile.save(flush: true)
                success = Boolean.TRUE
                message = AVATAR_UPDATED_MESSAGE
            } finally {
                inputStream?.close()
                render ajaxResponseService.renderMessage(success, message)
            }
        }
    }

    private updateProfile(command) {
        render ajaxResponseService.composeJsonResponse(profileService.updateProfile(profileHolder.profile, command.properties), PROFILE_UPDATED_MESSAGE)
    }

    private profile(String id, Closure getProfile) {
        if (id) {
            BaseProfile profile = getProfile() as BaseProfile

            profile ? [profile: profile, id: profile.identifier] : [id: id, message: NO_SUCH_PROFILE, args: [id]]
        } else {
            User user = springSecurityService.currentUser as User
            UserProfile profile = user?.userProfile

            redirect(profile ? [action: 'userProfile', id: profile.identifier] : [controller: 'auth', action: 'index'])
        }
    }
}
