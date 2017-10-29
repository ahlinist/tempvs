package com.tempvs.user

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
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
    private static final String EMAIL = 'email'
    private static final String FIRST_NAME = 'firstName'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String AVATAR_COLLECTION = 'avatar'
    private static final String PROFILE_EMAIL = 'profileEmail'

    def user = Mock User
    def image = Mock Image
    def userService = Mock UserService
    def clubProfile = Mock ClubProfile
    def userProfile = Mock UserProfile
    def imageService = Mock ImageService
    def imageUploadBean = Mock ImageUploadBean
    def objectDAOService = Mock ObjectDAOService

    def setup() {
        service.userService = userService
        service.imageService = imageService
        service.objectDAOService = objectDAOService
    }

    def cleanup() {
    }

    void "Test getProfile()"() {
        given:
        Map clubProfileProps = [profileId: ONE]

        when:
        def result = service.getProfile(ClubProfile, ONE)

        then:
        1 * objectDAOService.find(ClubProfile, clubProfileProps) >> clubProfile
        0 * _

        and:
        result == clubProfile

        when:
        result = service.getProfile(UserProfile, ONE)

        then:
        1 * objectDAOService.find(UserProfile, clubProfileProps) >> null
        1 * objectDAOService.get(UserProfile, ONE) >> userProfile
        0 * _

        and:
        result == userProfile
    }

    void "Test getProfileByProfileEmail()"() {
        given:
        Map restrictions = [profileEmail: PROFILE_EMAIL]

        when:
        def result = service.getProfileByProfileEmail(UserProfile, PROFILE_EMAIL)

        then:
        1 * objectDAOService.find(UserProfile, restrictions) >> userProfile
        0 * _

        result == userProfile
    }

    void "Test createProfile()"() {
        when:
        def result = service.createProfile(clubProfile, imageUploadBean)

        then:
        1 * imageService.updateImage(imageUploadBean, AVATAR_COLLECTION) >> image
        1 * clubProfile.setAvatar(image)
        1 * userService.currentUser >> user
        1 * clubProfile.setUser(user)
        1 * objectDAOService.save(clubProfile) >> clubProfile
        0 * _

        and:
        result == clubProfile
    }

    void "Test deleteProfile()"() {
        when:
        service.deleteProfile(clubProfile)

        then:
        1 * clubProfile.avatar >> image
        1 * imageService.deleteImage(image)
        1 * clubProfile.delete()
        0 * _
    }

    void "Test editProfileField()"() {
        when:
        def result = service.editProfileField(userProfile, FIRST_NAME, FIELD_VALUE)

        then:
        1 * userProfile.setFirstName(FIELD_VALUE)
        1 * objectDAOService.save(userProfile) >> userProfile
        0 * _

        and:
        result == userProfile
    }

    void "Test uploadAvatar()"() {
        when:
        def result = service.uploadAvatar(userProfile, imageUploadBean)

        then:
        1 * userProfile.avatar >> image
        1 * imageService.updateImage(imageUploadBean, AVATAR_COLLECTION, image) >> image
        1 * userProfile.setAvatar(image)
        1 * objectDAOService.save(userProfile) >> userProfile
        0 * _

        and:
        result == userProfile
    }

    void "Test deleteAvatar()"() {
        when:
        service.deleteAvatar(userProfile)

        then:
        1 * userProfile.avatar >> image
        1 * imageService.deleteImage(image)
        1 * userProfile.setAvatar(null)
        1 * objectDAOService.save(userProfile) >> userProfile
        0 * _
    }

    void "Test isProfileEmailUnique() for userProfile"() {
        when:
        def result = service.isProfileEmailUnique(userProfile, EMAIL)

        then:
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * objectDAOService.find(ClubProfile, [profileEmail: EMAIL])
        1 * userProfile.user >> user
        0 * _

        and:
        result == Boolean.TRUE
    }

    void "Test isProfileEmailUnique() for clubProfile"() {
        when:
        def result = service.isProfileEmailUnique(clubProfile, EMAIL)

        then:
        1 * userService.getUserByEmail(EMAIL)
        1 * objectDAOService.find(UserProfile, [profileEmail: EMAIL]) >> userProfile
        1 * userProfile.user >> user
        1 * clubProfile.user >> user
        0 * _

        and:
        result == Boolean.TRUE
    }
}
