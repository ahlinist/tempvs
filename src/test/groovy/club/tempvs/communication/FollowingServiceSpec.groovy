package club.tempvs.communication

import club.tempvs.object.ObjectFactory
import club.tempvs.periodization.Period
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import club.tempvs.user.ProfileType
import club.tempvs.user.User
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import org.springframework.validation.Errors
import spock.lang.Specification

class FollowingServiceSpec extends Specification implements ServiceUnitTest<FollowingService>, DataTest {

    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final String ID = 'id'
    private static final String CLASS = 'class'

    def user1 = Mock User
    def user2 = Mock User
    def errors = GroovyMock Errors
    def following = Mock Following
    def ancientPeriod = Period.ANCIENT
    def antiquityPeriod = Period.ANTIQUITY
    def objectFactory = Mock ObjectFactory
    def profileService = Mock ProfileService
    def followerProfile = Mock Profile
    def followedProfile = Mock Profile

    void setupSpec() {
        mockDomain Following
    }

    def setup() {
        service.objectFactory = objectFactory
        service.profileService = profileService

        GroovySpy(Following, global: true)
    }

    def cleanup() {
    }

    void "Test createFollowing()"() {
        when:
        def result = service.createFollowing(followerProfile, followedProfile)

        then:
        1 * objectFactory.getInstance(Following) >> following
        1 * following.setFollower(followerProfile)
        1 * following.setFollowed(followedProfile)
        1 * following.save()
        0 * _

        and:
        result == following
    }

    void "Test deleteFollowing()"() {
        when:
        service.deleteFollowing(followerProfile, followedProfile)

        then:
        1 * Following.findByFollowerAndFollowed(followerProfile, followedProfile) >> following
        1 * following.delete()
        0 * _
    }

    void "Test getFollowing()"() {
        when:
        def result = service.getFollowing(followerProfile, followedProfile)

        then:
        1 * Following.findByFollowerAndFollowed(followerProfile, followedProfile) >> following
        0 * _

        and:
        result == following
    }

    void "Test getNewFollowingsCount()"() {
        given:
        Integer newFollowingsCount = 3

        when:
        def result = service.getNewFollowersCount(followedProfile)

        then:
        1 * Following.countByFollowedAndIsNew(followedProfile, true) >> newFollowingsCount
        0 * _

        and:
        result == newFollowingsCount
    }

    void "Test getFollowings()"() {
        when:
        def result = service.getFollowings(followerProfile)

        then:
        1 * Following.findAllByFollower(followerProfile) >> [following]
        0 * _

        result == [following]
    }

    void "Test getFollowers()"() {
        when:
        def result = service.getFollowers(followedProfile)

        then:
        1 * Following.findAllByFollowed(followedProfile) >> [following]
        0 * _

        result == [following]
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
        def result = service.mayBeFollowed(followerProfile, followedProfile)

        then:
        1 * followerProfile.type >> ProfileType.USER
        1 * followedProfile.type >> ProfileType.USER
        1 * followerProfile.id >> LONG_ONE
        1 * followedProfile.id >> LONG_TWO
        1 * followerProfile.period >> ancientPeriod
        1 * followedProfile.period >> ancientPeriod
        1 * followerProfile.user >> user1
        1 * followedProfile.user >> user2
        0 * _

        and:
        result == Boolean.TRUE
    }
}
