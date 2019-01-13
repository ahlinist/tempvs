package club.tempvs.communication

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import club.tempvs.user.UserService
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

    static defaultAction = 'show'

    static allowedMethods = [
            index: 'GET',
            show: 'GET',
            follow: 'POST',
            unfollow: 'DELETE',
            getNewFollowingsCount: 'GET',
    ]

    UserService userService
    ProfileService profileService
    PageRenderer groovyPageRenderer
    FollowingService followingService
    AjaxResponseHelper ajaxResponseHelper

    def show(Long id) {
        Profile profile = id ? profileService.getProfile(id) : userService.currentProfile
        List<Following> followerList = followingService.getFollowers(profile)
        List<Following> followedList = followingService.getFollowings(profile)
        List<Following> newFollowerList = followerList.findAll {it.isNew}
        List<Following> newFollowedList = followedList.findAll {it.isNew}
        followingService.ageFollowings(newFollowerList + newFollowedList)

        [
                profile: profile,
                followerProfiles: (followerList - newFollowerList)*.follower,
                newFollowerProfiles: newFollowerList*.follower,
                followedProfiles: (followedList - newFollowedList)*.followed,
                newFollowedProfiles: newFollowedList*.followed,
        ]
    }

    def follow(Long profileId) {
        Profile followerProfile = userService.currentProfile
        Profile followingProfile = profileService.getProfile(profileId)

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

    def unfollow(Long profileId) {
        Profile followerProfile = userService.currentProfile
        Profile followingProfile = profileService.getProfile(profileId)

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
        Profile currentProfile = userService.currentProfile
        Integer count = followingService.getNewFollowersCount(currentProfile)
        render([action: DISPLAY_COUNTER, count: count] as JSON)
    }
}
