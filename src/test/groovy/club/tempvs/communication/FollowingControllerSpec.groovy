package club.tempvs.communication

import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
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
    def userProfile = Mock UserProfile
    def clubProfile = Mock ClubProfile
    def followerClubProfile = Mock ClubProfile
    def followingClubProfile = Mock ClubProfile

    def profileService = Mock ProfileService
    def groovyPageRenderer = Mock PageRenderer
    def followingService = Mock FollowingService

    def setup() {
        controller.profileService = profileService
        controller.followingService = followingService
        controller.groovyPageRenderer = groovyPageRenderer
    }

    def cleanup() {
    }

    void "Test index()"() {
        when:
        controller.index()

        then:
        1 * profileService.currentProfile >> userProfile
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
        view == '/following/show'
        model == [profile: userProfile, followerProfiles: [userProfile], newFollowerProfiles: [], followingProfiles: [userProfile], newFollowingProfiles: []]
    }

    void "Test user()"() {
        given:
        params.id = LONG_ONE

        when:
        controller.user()

        then:
        1 * profileService.getProfile(UserProfile, LONG_ONE) >> userProfile
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
        view == '/following/show'
        model == [profile: userProfile, followerProfiles: [userProfile], newFollowerProfiles: [], followingProfiles: [userProfile], newFollowingProfiles: []]
    }

    void "Test club()"() {
        given:
        params.id = LONG_ONE

        when:
        controller.club()

        then:
        1 * profileService.getProfile(ClubProfile, LONG_ONE) >> clubProfile
        1 * followingService.getFollowers(clubProfile) >> [following]
        1 * followingService.getFollowings(clubProfile) >> [following]
        2 * following.isNew >> Boolean.FALSE
        1 * profileService.getProfilesByFollowings([]) >> []
        1 * profileService.getProfilesByFollowers([]) >> []
        1 * profileService.getProfilesByFollowings([following]) >> [clubProfile]
        1 * profileService.getProfilesByFollowers([following]) >> [clubProfile]
        1 * followingService.ageFollowings([])
        0 * _

        and:
        view == '/following/show'
        model == [profile: clubProfile, followerProfiles: [clubProfile], newFollowerProfiles: [], followingProfiles: [clubProfile], newFollowingProfiles: []]
    }

    void "Test follow()"() {
        given:
        request.method = POST_METHOD
        params.profileClassName = ClubProfile.name
        params.profileId = LONG_ONE

        when:
        controller.follow()

        then:
        1 * profileService.currentProfile >> followerClubProfile
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
        1 * profileService.currentProfile >> followerClubProfile
        1 * profileService.getProfile(ClubProfile, LONG_ONE) >> followingClubProfile
        1 * followingClubProfile.asType(Profile) >> followingClubProfile
        1 * followingService.deleteFollowing(followerClubProfile, followingClubProfile)
        1 * followingService.mayBeFollowed(followerClubProfile, followingClubProfile) >> Boolean.TRUE
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
        1 * profileService.currentProfile >> userProfile
        1 * followingService.getNewFollowersCount(userProfile) >> 2
        0 * _

        and:
        response.json.action == DISPLAY_COUNTER
        response.json.count == 2
    }
}
