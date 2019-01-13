package club.tempvs.communication

import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import club.tempvs.user.UserService
import grails.gsp.PageRenderer
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class FollowingControllerSpec extends Specification implements ControllerUnitTest<FollowingController> {

    private static final Long LONG_ONE = 1L
    private static final String GET_METHOD = 'GET'
    private static final String POST_METHOD = 'POST'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String DISPLAY_COUNTER = 'displayCounter'

    def following = Mock Following
    def profile = Mock Profile
    def followerProfile = Mock Profile
    def followedProfile = Mock Profile

    def userService = Mock UserService
    def profileService = Mock ProfileService
    def groovyPageRenderer = Mock PageRenderer
    def followingService = Mock FollowingService

    def setup() {
        controller.userService = userService
        controller.profileService = profileService
        controller.followingService = followingService
        controller.groovyPageRenderer = groovyPageRenderer
    }

    def cleanup() {
    }

    void "Test show()"() {
        given:
        params.id = LONG_ONE

        when:
        def result = controller.show()

        then:
        1 * profileService.getProfile(LONG_ONE) >> profile
        1 * followingService.getFollowers(profile) >> [following]
        1 * followingService.getFollowings(profile) >> [following]
        2 * following.isNew >> Boolean.FALSE
        1 * following.getProperty('follower') >> profile
        1 * following.getProperty('followed') >> profile
        1 * followingService.ageFollowings([])
        0 * _

        and:
        result == [profile: profile, followerProfiles: [profile], newFollowerProfiles: [], followedProfiles: [profile], newFollowedProfiles: []]
    }

    void "Test follow()"() {
        given:
        request.method = POST_METHOD
        params.profileId = LONG_ONE

        when:
        controller.follow()

        then:
        1 * userService.currentProfile >> followerProfile
        1 * profileService.getProfile(LONG_ONE) >> followedProfile
        1 * followingService.createFollowing(followerProfile, followedProfile) >> following
        1 * following.hasErrors() >> Boolean.FALSE
        1 * followingService.mayBeFollowed(followerProfile, followedProfile) >> Boolean.TRUE
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }


    void "Test unfollow()"() {
        given:
        request.method = DELETE_METHOD
        params.profileId = LONG_ONE

        when:
        controller.unfollow()

        then:
        1 * userService.currentProfile >> followerProfile
        1 * profileService.getProfile(LONG_ONE) >> followedProfile
        1 * followingService.deleteFollowing(followerProfile, followedProfile)
        1 * followingService.mayBeFollowed(followerProfile, followedProfile) >> Boolean.TRUE
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test getNewFollowingsCount()"() {
        given:
        request.method = GET_METHOD

        when:
        controller.getNewFollowersCount()

        then:
        1 * userService.currentProfile >> profile
        1 * followingService.getNewFollowersCount(profile) >> 2
        0 * _

        and:
        response.json.action == DISPLAY_COUNTER
        response.json.count == 2
    }
}
