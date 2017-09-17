package com.tempvs.user

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import grails.compiler.GrailsCompileStatic
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException

/**
 * Controller for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
class ProfileController {

    private static final String AVATAR_COLLECTION = 'avatar'
    private static final String EMAIL_USED = 'user.email.used'
    private static final String EMAIL_EMPTY = 'profile.email.empty'
    private static final String NO_SUCH_PROFILE = 'profile.noSuchProfile.message'
    private static final String EDIT_EMAIL_DUPLICATE = 'user.edit.email.duplicate'
    private static final String PROFILE_DELETION_FAILED = 'profile.delete.failed.message'
    private static final String EDIT_PROFILE_EMAIL_MESSAGE_SENT = 'profileEmail.verification.sent.message'
    
    static allowedMethods = [index: 'GET', userProfile: 'GET', clubProfile: 'GET', switchProfile: 'GET', list: 'GET', createClubProfile: 'POST', editUserProfile: 'POST', editClubProfile: 'POST', editProfileEmail: 'POST', deleteProfile: 'DELETE', deleteAvatar: 'DELETE', uploadAvatar: 'POST']

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
        profile(id) {
            profileService.getProfile(UserProfile, id)
        }
    }

    def clubProfile(String id) {
        profile(id) {
            profileService.getProfile(ClubProfile, id)
        }
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

    def list() {
        User user = userService.currentUser
        [userProfile: user.userProfile, clubProfiles: user.clubProfiles]
    }

    def createClubProfile(ClubProfileCommand command) {
        if (command.validate()) {
            Image avatar = imageService.updateImage(command.avatarBean, AVATAR_COLLECTION)
            Map properties = command.properties + [avatar: avatar, user: userService.currentUser]
            BaseProfile profile = properties as ClubProfile

            if (profile.validate()) {
                BaseProfile persistedProfile = profileService.saveProfile(profile)
                profileHolder.profile = persistedProfile
                render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'profile', action: 'clubProfile', id: persistedProfile.id))
            } else {
                render ajaxResponseService.renderValidationResponse(profile)                }
        } else {
            render ajaxResponseService.renderValidationResponse(command)
        }
    }

    def editUserProfile(UserProfileCommand command) {
        editProfile(command)
    }

    def editClubProfile(ClubProfileCommand command) {
        editProfile(command)
    }

    def editProfileEmail(String email) {
        if (email) {
            BaseProfile profile = profileHolder.profile

            if (email == profile.profileEmail) {
                render ajaxResponseService.renderFormMessage(Boolean.FALSE, EDIT_EMAIL_DUPLICATE)
            } else {
                if (!userService.isEmailUnique(email)) {
                    render ajaxResponseService.renderFormMessage(Boolean.FALSE, EMAIL_USED)
                } else {
                    Map properties = [
                            instanceId: profile.id,
                            email: email,
                            action: profile.class.simpleName.uncapitalize(),
                    ]

                    EmailVerification emailVerification = verifyService.createEmailVerification(properties)

                    if (!emailVerification.hasErrors()) {
                        verifyService.sendEmailVerification(emailVerification)
                    }

                    render ajaxResponseService.renderValidationResponse(emailVerification, EDIT_PROFILE_EMAIL_MESSAGE_SENT)
                }
            }
        } else {
            render ajaxResponseService.renderFormMessage(Boolean.FALSE, EMAIL_EMPTY)
        }
    }

    def deleteProfile(String id) {
        BaseProfile profile = profileService.getProfile(ClubProfile.class, id)

        if (profile && (profile.user.id == userService.currentUserId)) {
            if (profileService.deleteProfile(profile)) {
                profileHolder.profile = null
                return render(ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'profile')))
            }
        }

        render ajaxResponseService.renderFormMessage(Boolean.FALSE, PROFILE_DELETION_FAILED)
    }

    def deleteAvatar() {
        BaseProfile profile = profileService.getProfile(params.profileClass as Class, params.profileId)
        profileService.deleteAvatar(profile)
        render ajaxResponseService.renderRedirect(request.getHeader('referer'))
    }

    def uploadAvatar(ImageUploadBean imageUploadBean) {
        BaseProfile profile = profileService.getProfile(params.profileClass as Class, params.profileId)

        if (profile) {
            BaseProfile editedProfile = profileService.editAvatar(profile, imageUploadBean)

            if (editedProfile.validate()) {
                render ajaxResponseService.renderRedirect(request.getHeader('referer'))
            } else {
                render ajaxResponseService.renderValidationResponse(editedProfile)
            }
        } else {
            render ajaxResponseService.renderFormMessage(Boolean.FALSE, NO_SUCH_PROFILE)
        }
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseService.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect grailsLinkGenerator.link(controller: 'auth')
        }
    }

    private editProfile(command) {
        BaseProfile profile = profileService.editProfile(profileHolder.profile, command.properties)

        if (profile.validate()) {
            render ajaxResponseService.renderRedirect(request.getHeader('referer'))
        } else {
            render ajaxResponseService.renderValidationResponse(profile)
        }
    }

    private profile(String id, Closure renderProfile) {
        if (id) {
            BaseProfile profile = renderProfile() as BaseProfile
            profile ? [profile: profile, id: profile.identifier, editAllowed: profileHolder.profile == profile] : [id: id, notFoundMessage: NO_SUCH_PROFILE, args: [id]]
        } else {
            User user = userService.currentUser
            UserProfile profile = user?.userProfile

            redirect(profile ? [action: 'userProfile', id: profile.identifier] : [controller: 'auth', action: 'index'])
        }
    }
}
