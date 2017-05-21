package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.domain.BaseObject
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import org.springframework.web.multipart.MultipartFile

/**
 * Controller for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
class ProfileController {

    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'
    private static final String PROFILE_UPDATED_MESSAGE = 'profile.updated.message'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'profileEmail.verification.sent.message'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String AVATAR_UPDATED_MESSAGE = 'userProfile.update.avatar.success.message'
    private static final String AVATAR_UPDATE_FAILED_MESSAGE = 'userProfile.update.avatar.failed.message'
    private static final String AVATAR_COLLECTION = 'avatar'

    SpringSecurityService springSecurityService
    AjaxResponseService ajaxResponseService
    ProfileHolder profileHolder
    ProfileService profileService
    VerifyService verifyService
    UserService userService
    ImageService imageService

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
            User user = springSecurityService.currentUser

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
                    render([redirect: g.createLink(controller: 'profile', action: 'edit')] as JSON)
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
            render([messages: [g.message(code: EMAIL_UPDATE_DUPLICATE)]] as JSON)
        } else {
            if (!userService.isEmailUnique(profileEmail)) {
                render([messages: [g.message(code: EMAIL_USED)]] as JSON)
            } else {
                Map props = [instanceId: profile.id,
                             email: profileEmail,
                             action: profile.class.simpleName.toLowerCase()]
                render ajaxResponseService.composeJsonResponse(verifyService.createEmailVerification(props), UPDATE_PROFILE_EMAIL_MESSAGE_SENT)
            }
        }
    }
    
    def updateAvatar(ProfileAvatarCommand command) {
        if (command.validate()) {
            Boolean success = Boolean.FALSE
            String message = AVATAR_UPDATE_FAILED_MESSAGE
            BaseProfile profile = profileHolder.profile
            MultipartFile multipartAvatar = command.avatarImage
            Map metaData = [userId: profile.user.id, properties: [profileClass: profileHolder.clazz.simpleName, profileId: profile.id]]

            if (!multipartAvatar?.empty) {
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
        } else {
            render ajaxResponseService.composeJsonResponse(command)
        }
    }

    private updateProfile(command) {
        render ajaxResponseService.composeJsonResponse(profileService.updateProfile(profileHolder.profile, command.properties), PROFILE_UPDATED_MESSAGE)
    }

    private profile(String id, Closure getProfile) {
        if (id) {
            BaseProfile profile = getProfile()

            profile ? [profile: profile, id: profile.identifier] : [id: id, message: NO_SUCH_PROFILE, args: [id]]
        } else {
            UserProfile profile = springSecurityService.currentUser?.userProfile

            redirect(profile ? [action: 'userProfile', id: profile.identifier] : [controller: 'auth', action: 'index'])
        }
    }
}

@GrailsCompileStatic
class UserProfileCommand extends BaseObject {
    String firstName
    String lastName
    String location
    String profileId

    static constraints = {
        location nullable: true
        profileId shared: "profileId"
    }
}

@GrailsCompileStatic
class ClubProfileCommand extends BaseObject {
    String firstName
    String lastName
    String nickName
    String clubName
    String location
    String profileId

    static constraints = {
        lastName nullable: true
        nickName nullable: true
        clubName nullable: true
        location nullable: true
        profileId shared: "profileId"
    }
}

@GrailsCompileStatic
class ProfileAvatarCommand extends BaseObject {
    MultipartFile avatarImage

    static constraints = {
    }
}
