package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ProfileService)
@Mock([ClubProfile])
class ProfileServiceSpec extends Specification {

    private static final String ONE = '1'
    private static final String EMAIL = 'test@email.com'
    private static final Long LONG_ID = 1L
    private static final String FIRST_NAME = 'firstName'
    private static final String LAST_NAME = 'lastName'
    private static final String NICK_NAME = 'nickName'
    private static final String CLUB_NAME = 'clubName'

    def userService = Mock(UserService)
    def clubProfile = Mock(ClubProfile)
    def userProfile = Mock(UserProfile)
    def user = Mock(User)
    def objectDAO = Mock(ObjectDAO)
    def objectFactory = Mock(ObjectFactory)

    def setup() {
        service.userService = userService
        service.objectDAO = objectDAO
        service.objectFactory = objectFactory
    }

    def cleanup() {
    }

    void "Test getProfile()"() {
        given:
        Map clubProfileProps = [profileId: ONE]

        when:
        def result = service.getProfile(ClubProfile, ONE)

        then:
        1 * objectDAO.find(ClubProfile, clubProfileProps) >> clubProfile
        0 * _

        and:
        result == clubProfile

        when:
        result = service.getProfile(UserProfile, ONE)

        then:
        1 * objectDAO.find(UserProfile, clubProfileProps) >> null
        1 * objectDAO.get(UserProfile, ONE) >> userProfile
        0 * _

        and:
        result == userProfile
    }

    void "Test updateProfile()"() {
        given:
        Map params = [:]

        when:
        def result = service.updateProfile(userProfile, params)

        then:
        1 * userProfile.save() >> userProfile
        0 * _

        and:
        result == userProfile

        when:
        result = service.updateProfile(clubProfile, params)

        then:
        1 * clubProfile.save() >> clubProfile
        0 * _

        and:
        result == clubProfile
    }

    void "Test createClubProfile()"() {
        given:
        Map props = [
                firstName: FIRST_NAME,
                lastName: LAST_NAME,
                nickName: NICK_NAME,
                clubName: CLUB_NAME,
        ]

        when:
        def result = service.createClubProfile(props)

        then:
        1 * userService.currentUser >> user
        1 * objectFactory.create(ClubProfile) >> clubProfile
        1 * clubProfile.setFirstName(FIRST_NAME)
        1 * clubProfile.setLastName(LAST_NAME)
        1 * clubProfile.setNickName(NICK_NAME)
        1 * clubProfile.setClubName(CLUB_NAME)
        1 * user.addToClubProfiles(clubProfile) >> user
        1 * user.save()
        0 * _

        and:
        result == clubProfile
    }

    void "Test updateProfileEmail()"() {
        when:
        def result = service.updateProfileEmail(ClubProfile, LONG_ID, EMAIL)

        then:
        1 * objectDAO.get(ClubProfile, LONG_ID) >> clubProfile
        1 * clubProfile.setProfileEmail(EMAIL)
        1 * clubProfile.save()

        and:
        result == clubProfile
    }
}
