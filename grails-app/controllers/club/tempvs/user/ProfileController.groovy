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
import org.springframework.security.access.annotation.Secured
import grails.web.mapping.LinkGenerator
import org.springframework.security.access.AccessDeniedException

/**
 * Controller for managing {@link UserProfile} and
 * {@link ClubProfile}.
 */
@Secured('isAuthenticated()')
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
            search: 'GET',
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
    PageRenderer groovyPageRenderer
    UserInfoHelper userInfoHelper
    FollowingService followingService
    LinkGenerator grailsLinkGenerator
    AjaxResponseHelper ajaxResponseHelper

    def index() {
        Profile profile = userInfoHelper.getCurrentProfile(request)
        redirect action: "${profile.class.simpleName - 'Profile'}", id: profile.identifier
    }

    def search(String query, Integer offset) {
        List<Profile> profiles = profileService.searchProfiles(userInfoHelper.getCurrentProfile(request), query, offset)
        Map model = [profiles: profiles]
        String template = groovyPageRenderer.render(template: '/profile/templates/profileSearchResult', model: model)
        render([action: profiles ? REPLACE_ACTION : NO_ACTION, template: template] as JSON)
    }

    @Secured('permitAll')
    def user(String id) {
        if (!id) {
            UserProfile profile = userInfoHelper.getCurrentUser(request)?.userProfile
            return redirect(profile ? [action: 'userProfile', id: profile.identifier] : [controller: 'auth', action: 'index'])
        }

        UserProfile userProfile = profileService.getProfile(UserProfile, id)

        if (!userProfile) {
            return [id: id, notFoundMessage: NO_SUCH_PROFILE]
        }

        Profile profile = userInfoHelper.getCurrentProfile(request)

        [
                profile: userProfile,
                user: userProfile.user,
                id: userProfile.identifier,
                editAllowed: profile == userProfile,
                mayBeFollowed: followingService.mayBeFollowed(profile, userProfile),
                isFollowed: followingService.getFollowing(profile, userProfile) as Boolean,
        ]
    }

    @Secured('permitAll')
    def club(String id) {
        if (!id) {
            return redirect(controller: 'auth', action: 'index')
        }

        ClubProfile clubProfile = profileService.getProfile(ClubProfile, id)

        if (!clubProfile) {
            return [id: id, notFoundMessage: NO_SUCH_PROFILE]
        }

        Profile profile = userInfoHelper.getCurrentProfile(request)

        [
                profile: clubProfile,
                user: clubProfile.user,
                id: clubProfile.identifier,
                passports: clubProfile.passports,
                editAllowed: profile == clubProfile,
                mayBeFollowed: followingService.mayBeFollowed(profile, clubProfile),
                isFollowed: followingService.getFollowing(profile, clubProfile) as Boolean,
        ]
    }

    def switchProfile(Long id) {
        Profile profile = id ? profileService.getProfile(ClubProfile, id) : userInfoHelper.getCurrentUser(request)?.userProfile

        if (profile.active) {
            profileService.setCurrentProfile(profile)
        }

        redirect uri: request.getHeader(REFERER)
    }

    def createClubProfile(ClubProfile clubProfile, ImageUploadBean imageUploadBean) {
        if (!imageUploadBean.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBean))
        }

        User user = userInfoHelper.getCurrentUser(request)
        clubProfile = profileService.validateClubProfile(clubProfile, user)

        if (clubProfile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(clubProfile))
        }

        Image avatar = imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION)
        clubProfile = profileService.createClubProfile(clubProfile, avatar)

        if (clubProfile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(clubProfile))
        }

        profileService.setCurrentProfile(clubProfile)
        render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'profile', action: 'club', id: clubProfile.id))
    }

    def editProfileField() {
        Profile currentProfile = userInfoHelper.getCurrentProfile(request)
        Profile profile = profileService.editProfileField(currentProfile, params.fieldName as String, params.fieldValue as String)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        render([action: SUCCESS_ACTION] as JSON)
    }

    def editProfileEmail() {
        String email = params.fieldValue
        Profile profile = userInfoHelper.getCurrentProfile(request)

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

        if (userInfoHelper.getCurrentProfile(request) == clubProfile) {
            profileService.setCurrentProfile(null)
        }

        Profile profile = profileService.deactivateProfile(clubProfile)

        if (profile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(profile))
        }

        User user = clubProfile.user
        List<ClubProfile> clubProfiles = user.clubProfiles

        Map model = [
                activeProfiles: clubProfiles.findAll {it.active},
                inactiveProfiles: clubProfiles.findAll {!it.active},
                availablePeriods: Period.values(),
                editAllowed: Boolean.TRUE,
        ]

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
        if (!imageUploadBean.validate()) {
            return render(ajaxResponseHelper.renderValidationResponse(imageUploadBean))
        }

        Profile profile = userInfoHelper.getCurrentProfile(request)
        Image avatar = imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION, profile.avatar)
        Profile persistedProfile = profileService.uploadAvatar(profile, avatar)

        if (persistedProfile.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(persistedProfile))
        }

        render ajaxResponseHelper.renderRedirect(request.getHeader(REFERER))
    }

    def list() {
        User user = userInfoHelper.getCurrentUser(request)
        List<ClubProfile> clubProfiles = user.clubProfiles

        [
                userProfile: user.userProfile,
                activeProfiles: clubProfiles.findAll { it.active },
                inactiveProfiles: clubProfiles.findAll { !it.active },
                availablePeriods: Period.values(),
        ]
    }

    def accessDeniedThrown(AccessDeniedException exception) {
        if (request.xhr) {
            render ajaxResponseHelper.renderRedirect(grailsLinkGenerator.link(controller: 'auth'))
        } else {
            redirect grailsLinkGenerator.link(controller: 'auth')
        }
    }
}
