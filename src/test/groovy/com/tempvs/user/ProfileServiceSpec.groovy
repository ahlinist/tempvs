package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.image.Image
import com.tempvs.image.ImageService
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

    def user = Mock User
    def image = Mock Image
    def objectDAO = Mock ObjectDAO
    def clubProfile = Mock ClubProfile
    def userProfile = Mock UserProfile
    def imageService = Mock ImageService

    def setup() {
        service.objectDAO = objectDAO
        service.imageService = imageService
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

    void "Test saveProfile()"() {
        when:
        def result = service.saveProfile(clubProfile)

        then:
        1 * clubProfile.save()
        0 * _

        and:
        result == clubProfile
    }

    void "Test deleteProfile()"() {
        when:
        def result = service.deleteProfile(clubProfile)

        then:
        1 * clubProfile.avatar >> image
        1 * imageService.deleteImage(image)
        1 * clubProfile.delete([failOnError: Boolean.TRUE])

        and:
        result == Boolean.TRUE
    }
}
