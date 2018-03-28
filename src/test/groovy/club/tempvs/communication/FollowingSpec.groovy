package club.tempvs.communication

import club.tempvs.periodization.Period
import club.tempvs.user.ClubProfile
import club.tempvs.user.UserProfile
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class FollowingSpec extends Specification implements DomainUnitTest<Following> {

    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L

    def period = GroovyMock Period

    def setup() {
    }

    def cleanup() {
    }

    void "Test userProfile following creation"() {
        expect:
        !domain.validate()

        when:
        domain.profileClassName = UserProfile

        then:
        !domain.validate()

        when:
        domain.followerId = LONG_ONE

        then:
        !domain.validate()

        when:
        domain.followingId = LONG_TWO

        then:
        domain.validate()
    }

    void "Test clubProfile following creation"() {
        expect:
        !domain.validate()

        when:
        domain.profileClassName = ClubProfile.name

        then:
        !domain.validate()

        when:
        domain.followerId = LONG_ONE

        then:
        !domain.validate()

        when:
        domain.followingId = LONG_TWO

        then:
        !domain.validate()

        when:
        domain.period = period

        then:
        domain.validate()
    }

    void "Test self-following"() {
        when:
        domain.profileClassName = ClubProfile
        domain.followerId = LONG_ONE
        domain.followingId = LONG_ONE

        then:
        !domain.validate()
    }
}
