package club.tempvs.communication

import club.tempvs.periodization.Period
import club.tempvs.user.Profile
import club.tempvs.user.ProfileType
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class FollowingSpec extends Specification implements DomainUnitTest<Following> {

    def follower = GroovyMock(Profile)
    def followed = GroovyMock(Profile)

    def setup() {
    }

    def cleanup() {
    }

    void "Test Following creation without both followed and follower"() {
        expect:
        !domain.validate()
    }

    void "Test Following creation without followed"() {
        given:
        domain.follower = follower

        expect:
        !domain.validate()
    }

    void "Test Following creation without follower"() {
        given:
        domain.followed = followed

        expect:
        !domain.validate()
    }

    void "Test successful Following creation"() {
        given:
        domain.follower = follower
        domain.followed = followed

        expect:
        domain.validate()
    }

    void "Test self-following"() {
        given:
        domain.follower = follower
        domain.follower = follower

        expect:
        !domain.validate()
    }

    void "Test different periods for profiles involved"() {
        given:
        def follower = GroovyMock(Profile) {
            getPeriod() >> Period.ANTIQUITY
        }

        def followed = GroovyMock(Profile) {
            getPeriod() >> Period.HIGH_MIDDLE_AGES
        }


        when:
        domain.follower = follower
        domain.follower = followed

        then:
        !domain.validate()
    }

    void "Test different types for profiles involved"() {
        given:
        def follower = GroovyMock(Profile) {
            getType() >> ProfileType.USER
        }

        def followed = GroovyMock(Profile) {
            getType() >> ProfileType.CLUB
        }


        when:
        domain.follower = follower
        domain.follower = followed

        then:
        !domain.validate()
    }
}
