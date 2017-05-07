package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.domain.BaseObject
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService

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

    SpringSecurityService springSecurityService
    AjaxResponseService ajaxResponseService
    ProfileHolder profileHolder
    ProfileService profileService
    VerifyService verifyService
    UserService userService

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
