package com.tempvs.user

import grails.converters.JSON

class UserProfileController {

    private static final String NO_SUCH_USER = 'userProfile.noSuchUser.message'
    private static final String UPDATE_PROFILE_EMAIL_MESSAGE_SENT = 'userProfile.edit.profileEmail.verification.sent.message'
    private static final String USER_PROFILE_UPDATED_MESSAGE = 'user.userProfile.updated'
    private static final String EMAIL_UPDATE_DUPLICATE = 'user.edit.email.duplicate'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String UPDATE_PROFILE_EMAIL_ACTION = 'profileEmail'

    def springSecurityService
    def ajaxResponseService
    def userService
    def userProfileService
    def verifyService

    static defaultAction = 'show'

    def show(String id) {
        UserProfile currentUserProfile = springSecurityService.currentUser?.userProfile
        String profileId = currentUserProfile?.profileId

        if (id) {
            if (profileId == id || currentUserProfile?.id as String == id) {
                [profile: currentUserProfile, id: profileId ?: currentUserProfile.id]
            } else {
                UserProfile profile = userProfileService.getUserProfile(id)

                if (profile) {
                    [profile: profile, id: profileId ?: profile.id]
                } else {
                    [id: id, message: NO_SUCH_USER, args: [id]]
                }
            }
        } else {
            if (currentUserProfile) {
                redirect action: 'show', id: profileId ?: currentUserProfile.id
            } else {
                redirect controller: 'auth', action: 'index'
            }
        }
    }

    def edit() {
        [profile: springSecurityService.currentUser.userProfile]
    }

    def updateUserProfile(UserProfileCommand upc) {
        render ajaxResponseService.composeJsonResponse(userProfileService.updateUserProfile(upc.properties), USER_PROFILE_UPDATED_MESSAGE)
    }

    def updateProfileEmail(String profileEmail) {
        User currentUser = springSecurityService.currentUser

        if (profileEmail == currentUser.userProfile.profileEmail) {
            render([messages: [g.message(code: EMAIL_UPDATE_DUPLICATE)]] as JSON)
        } else {
            if (userService.getUserByEmail(profileEmail) && currentUser.email != profileEmail ||
                    (userProfileService.getProfileByProfileEmail(profileEmail))) {
                render([messages: [g.message(code: EMAIL_USED)]] as JSON)
            } else {
                Map props = [userId: currentUser.id,
                             email: profileEmail,
                             action: UPDATE_PROFILE_EMAIL_ACTION]
                render ajaxResponseService.composeJsonResponse(verifyService.createEmailVerification(props), UPDATE_PROFILE_EMAIL_MESSAGE_SENT)
            }
        }
    }
}

class UserProfileCommand {
    String firstName
    String lastName
    String location
    String profileId

    static constraints = {
        importFrom UserProfile
    }
}
