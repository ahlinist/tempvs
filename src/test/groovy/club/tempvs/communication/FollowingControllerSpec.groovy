package club.tempvs.communication

import club.tempvs.user.ClubProfile
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import club.tempvs.user.UserInfoHelper
import club.tempvs.user.UserProfile
import grails.gsp.PageRenderer
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletRequest
import spock.lang.Specification

class FollowingControllerSpec extends Specification implements ControllerUnitTest<FollowingController> {

    private static final Long LONG_ONE = 1L
    private static final String POST_METHOD = 'POST'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String REPLACE_ACTION = 'replaceElement'

    def following = Mock Following
    def userProfile = Mock UserProfile
    def followerClubProfile = Mock ClubProfile
    def followingClubProfile = Mock ClubProfile

    def userInfoHelper = Mock UserInfoHelper
    def profileService = Mock ProfileService
    def groovyPageRenderer = Mock PageRenderer
    def followingService = Mock FollowingService

    def setup() {
        controller.userInfoHelper = userInfoHelper
        controller.profileService = profileService
        controller.followingService = followingService
        controller.groovyPageRenderer = groovyPageRenderer
    }

    def cleanup() {
    }

    void "Test show()"() {
        given:
        params.profileClass = UserProfile.name
        params.profileId = LONG_ONE

        when:
        def result = controller.show()

        then:
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> userProfile
        1 * followingService.getFollowers(userProfile) >> [following]
        1 * followingService.getFollowings(userProfile) >> [following]
        2 * following.isNew >> Boolean.FALSE
        1 * profileService.getProfilesByFollowings([]) >> []
        1 * profileService.getProfilesByFollowers([]) >> []
        1 * profileService.getProfilesByFollowings([following]) >> [userProfile]
        1 * profileService.getProfilesByFollowers([following]) >> [userProfile]
        1 * followingService.ageFollowings([])
        0 * _

        and:
        result == [profile: userProfile, followerProfiles: [userProfile], newFollowerProfiles: [], followingProfiles: [userProfile], newFollowingProfiles: []]
    }

    void "Test follow()"() {
        given:
        request.method = POST_METHOD
        params.profileClassName = ClubProfile.name
        params.profileId = LONG_ONE

        when:
        controller.follow()

        then:
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> followerClubProfile
        1 * profileService.getProfile(ClubProfile, LONG_ONE) >> followingClubProfile
        1 * followingClubProfile.asType(Profile) >> followingClubProfile
        1 * followingService.createFollowing(followerClubProfile, followingClubProfile) >> following
        1 * following.hasErrors() >> Boolean.FALSE
        1 * followingService.mayBeFollowed(followerClubProfile, followingClubProfile) >> Boolean.TRUE
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }


    void "Test unfollow()"() {
        given:
        request.method = DELETE_METHOD
        params.profileClassName = ClubProfile.name
        params.profileId = LONG_ONE

        when:
        controller.unfollow()

        then:
        1 * userInfoHelper.getCurrentProfile(_ as GrailsMockHttpServletRequest) >> followerClubProfile
        1 * profileService.getProfile(ClubProfile, LONG_ONE) >> followingClubProfile
        1 * followingClubProfile.asType(Profile) >> followingClubProfile
        1 * followingService.deleteFollowing(followerClubProfile, followingClubProfile)
        1 * followingService.mayBeFollowed(followerClubProfile, followingClubProfile) >> Boolean.TRUE
        1 * groovyPageRenderer.render(_ as Map)
        0 * _
    }
}
