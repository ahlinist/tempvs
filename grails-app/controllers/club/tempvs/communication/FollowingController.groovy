package club.tempvs.communication

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.user.ClubProfile
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import club.tempvs.user.UserProfile
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import org.springframework.security.access.annotation.Secured

/**
 * A controller that handles {@link Following}-related operations.
 */
@Secured('isAuthenticated()')
@GrailsCompileStatic
class FollowingController {

    private static final String NO_ACTION = 'none'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String DISPLAY_COUNTER = 'displayCounter'

    static allowedMethods = [
            index: 'GET',
            show: 'GET',
            follow: 'POST',
            unfollow: 'DELETE',
            getNewFollowingsCount: 'GET',
    ]

    ProfileService profileService
    PageRenderer groovyPageRenderer
    FollowingService followingService
    AjaxResponseHelper ajaxResponseHelper

    def index() {
        show(profileService.currentProfile)
    }

    def user(Long id) {
        show(profileService.getProfile(UserProfile, id))
    }

    def club(Long id) {
        show(profileService.getProfile(ClubProfile, id))
    }

    private show(Profile profile) {
        List<Following> followers = followingService.getFollowers(profile)
        List<Following> followings = followingService.getFollowings(profile)
        List<Following> newFollowers = followers.findAll {it.isNew}
        List<Following> newFollowings = followings.findAll {it.isNew}
        followingService.ageFollowings(newFollowers + newFollowings)

        Map model = [
                profile: profile,
                followerProfiles: profileService.getProfilesByFollowers(followers - newFollowers),
                newFollowerProfiles: profileService.getProfilesByFollowers(newFollowers),
                followingProfiles: profileService.getProfilesByFollowings(followings - newFollowings),
                newFollowingProfiles: profileService.getProfilesByFollowings(newFollowings),
        ]

        render view: '/following/show', model: model
    }

    def follow(String profileClassName, Long profileId) {
        Profile followerProfile = profileService.currentProfile
        Profile followingProfile = profileService.getProfile(Class.forName(profileClassName), profileId) as Profile

        if (!followingProfile) {
            return render([action: NO_ACTION] as JSON)
        }

        Following following = followingService.createFollowing(followerProfile, followingProfile)

        if (following.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(following))
        }

        Boolean mayBeFollowed = followingService.mayBeFollowed(followerProfile, followingProfile)
        Map model = [profile: followingProfile, mayBeFollowed: mayBeFollowed, isFollowed: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/profile/templates/followButton', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def unfollow(String profileClassName, Long profileId) {
        Profile followerProfile = profileService.currentProfile
        Profile followingProfile = profileService.getProfile(Class.forName(profileClassName), profileId) as Profile

        if (!followingProfile) {
            return render([action: NO_ACTION] as JSON)
        }

        followingService.deleteFollowing(followerProfile, followingProfile)
        Boolean mayBeFollowed = followingService.mayBeFollowed(followerProfile, followingProfile)
        Map model = [profile: followingProfile, mayBeFollowed: mayBeFollowed, isFollowed: Boolean.FALSE]
        String template = groovyPageRenderer.render(template: '/profile/templates/followButton', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def getNewFollowersCount() {
        Profile currentProfile = profileService.currentProfile
        Integer count = followingService.getNewFollowersCount(currentProfile)
        render([action: DISPLAY_COUNTER, count: count] as JSON)
    }
}
