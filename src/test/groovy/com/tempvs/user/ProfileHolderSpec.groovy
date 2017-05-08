package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import grails.plugin.springsecurity.SpringSecurityService
import spock.lang.Specification

/**
 * {@link com.tempvs.user.ProfileHolder} unit-test suite.
 */
class ProfileHolderSpec extends Specification {

    Class clazz = Object.class
    Long id = 1L
    def springSecurityService = Mock(SpringSecurityService)
    def objectDAO = Mock(ObjectDAO)
    def user = Mock(User)
    def baseProfile = Mock(BaseProfile)
    def userProfile = Mock(UserProfile)
    def clubProfile = Mock(ClubProfile)

    ProfileHolder profileHolder = new ProfileHolder()

    def setup() {
        profileHolder.springSecurityService = springSecurityService
        profileHolder.objectDAO = objectDAO
    }

    def cleanup() {
        profileHolder = new ProfileHolder()
    }

    void "Test getProfile() when not logged in"() {
        when:
        def result = profileHolder.getProfile()

        then:
        1 * springSecurityService.currentUser >> null
        0 * _

        and:
        result == null
    }

    void "Test getProfile() when logged in and profile info not populated."() {
        given:
        profileHolder.clazz == null
        profileHolder.id == null

        when:
        def result = profileHolder.getProfile()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User.class) >> user
        2 * user.getUserProfile() >> userProfile
        1 * userProfile.getId() >> id
        0 * _

        and:
        result == userProfile
    }

    void "Test getProfile() when logged in and profile info populated. Profile missing in DB."() {
        setup:
        profileHolder.clazz = clazz
        profileHolder.id = id

        when:
        def result = profileHolder.getProfile()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User.class) >> user
        1 * objectDAO.get(clazz, id) >> null
        0 * _

        and:
        result == null
    }

    void "Test getProfile() when logged in and profile info populated. Profile present in DB and belongs to user."() {
        setup:
        profileHolder.clazz = clazz
        profileHolder.id = id

        when:
        def result = profileHolder.getProfile()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User.class) >> user
        1 * objectDAO.get(clazz, id) >> baseProfile
        1 * user.getUserProfile() >> userProfile
        1 * baseProfile.equals(userProfile)  >> Boolean.TRUE
        0 * _

        and:
        result == baseProfile
    }

    void "Test getProfile() when logged in and profile info populated. Profile present in DB and doesn't belong to user."() {
        setup:
        profileHolder.clazz = clazz
        profileHolder.id = id

        when:
        def result = profileHolder.getProfile()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User.class) >> user
        1 * objectDAO.get(clazz, id) >> baseProfile
        _ * user.getClubProfiles() >> [clubProfile]
        3 * user.getUserProfile() >> userProfile
        1 * baseProfile.equals(userProfile)  >> Boolean.FALSE
        1 * userProfile.getId()

        and:
        result == userProfile
    }

    void "Test setProfile()"() {
        when:
        profileHolder.setProfile(userProfile)

        then:
        1 * userProfile.getId() >> 1L
        0 * _

        and:
        profileHolder.id == 1L
    }
}
