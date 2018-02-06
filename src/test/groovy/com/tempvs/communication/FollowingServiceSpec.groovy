package com.tempvs.communication

import com.tempvs.periodization.Period
import com.tempvs.user.ClubProfile
import com.tempvs.user.Profile
import com.tempvs.user.ProfileService
import com.tempvs.user.UserProfile
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class FollowingServiceSpec extends Specification implements ServiceUnitTest<FollowingService>, DataTest {

    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final String ID = 'id'
    private static final String CLASS = 'class'

    def following = Mock Following
    def period = GroovyMock Period
    def followerUserProfile = Mock UserProfile
    def followingUserProfile = Mock UserProfile
    def followerClubProfile = Mock ClubProfile
    def followingClubProfile = Mock ClubProfile

    def profileService = Mock ProfileService

    void setupSpec() {
        mockDomain Following
    }

    def setup() {
        service.profileService = profileService

        GroovySpy(Following, global: true)
    }

    def cleanup() {
    }

    void "Test createFollowing() for userProfiles"() {
        when:
        def result = service.createFollowing(followerUserProfile, followingUserProfile)

        then:
        1 * followerUserProfile.id >> LONG_ONE
        1 * followingUserProfile.id >> LONG_TWO

        and:
        result instanceof Following
    }

    void "Test createFollowing() for clubProfiles"() {
        when:
        def result = service.createFollowing(followerClubProfile, followingClubProfile)

        then:
        1 * followerClubProfile.id >> LONG_ONE
        1 * followingClubProfile.id >> LONG_TWO
        1 * followerClubProfile.period >> period
        1 * followingClubProfile.period >> period

        and:
        result instanceof Following
    }

    void "Test deleteFollowing()"() {
        when:
        service.deleteFollowing(followerClubProfile, followingClubProfile)

        then:
        2 * followerClubProfile.getProperty(CLASS) >> ClubProfile
        1 * followerClubProfile.getProperty(ID) >> LONG_ONE
        1 * followingClubProfile.getProperty(CLASS) >> ClubProfile
        1 * followingClubProfile.getProperty(ID) >> LONG_TWO
        1 * Following.findByProfileClassNameAndFollowerIdAndFollowingId(ClubProfile.name, LONG_ONE, LONG_TWO) >> following
        1 * following.delete()
        0 * _
    }

    void "Test getFollowing()"() {
        when:
        def result = service.getFollowing(followerClubProfile, followingClubProfile)

        then:
        2 * followerClubProfile.getProperty(CLASS) >> ClubProfile
        1 * followerClubProfile.getProperty(ID) >> LONG_ONE
        1 * followingClubProfile.getProperty(CLASS) >> ClubProfile
        1 * followingClubProfile.getProperty(ID) >> LONG_TWO
        1 * Following.findByProfileClassNameAndFollowerIdAndFollowingId(ClubProfile.name, LONG_ONE, LONG_TWO) >> following
        0 * _

        and:
        result == following
    }

    void "Test getNewFollowingsCount()"() {
        given:
        Integer newFollowingsCount = 3

        when:
        def result = service.getNewFollowingsCount()

        then:
        1 * profileService.currentProfile >> followingUserProfile
        1 * followingUserProfile.getProperty(CLASS) >> UserProfile
        1 * followingUserProfile.getProperty(ID) >> LONG_ONE
        1 * Following.countByProfileClassNameAndFollowingIdAndIsNew(UserProfile.name, LONG_ONE, Boolean.TRUE) >> newFollowingsCount
        0 * _

        and:
        result == newFollowingsCount
    }

    void "Test getFollowings()"() {
        when:
        def result = service.getFollowings(followerUserProfile)

        then:
        1 * followerUserProfile.getProperty(ID) >> LONG_ONE
        1 * followerUserProfile.getProperty(CLASS) >> UserProfile
        1 * Following.findAllByProfileClassNameAndFollowerId(UserProfile.name, LONG_ONE) >> [following]
        0 * _

        result instanceof List<Following>
    }

    void "Test getFollowers()"() {
        when:
        def result = service.getFollowers(followerClubProfile)

        then:
        1 * followerClubProfile.getProperty(ID) >> LONG_ONE
        1 * followerClubProfile.getProperty(CLASS) >> ClubProfile
        1 * Following.findAllByProfileClassNameAndFollowingId(ClubProfile.name, LONG_ONE) >> [following]
        0 * _

        result instanceof List<Following>
    }

    void "Test ageFollowings()"() {
        when:
        service.ageFollowings([following])

        then:
        1 * following.setIsNew(Boolean.FALSE)
        1 * following.save()
        0 * _
    }

    void "Test mayBeFollowed()"() {
        when:
        def result = service.mayBeFollowed(followerClubProfile, followingClubProfile)

        then:
        1 * followerClubProfile.id >> LONG_ONE
        1 * followingClubProfile.id >> LONG_TWO
        1 * followerClubProfile.period >> period
        1 * followingClubProfile.period >> period
        0 * _

        and:
        result == Boolean.TRUE
    }
}
