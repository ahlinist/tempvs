package com.tempvs.user

import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.item.Passport
import com.tempvs.item.PassportService
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class ProfileServiceSpec extends Specification implements ServiceUnitTest<ProfileService>, DomainUnitTest<UserProfile> {

    private static final String ONE = '1'
    private static final String USER = 'user'
    private static final String EMAIL = 'email'
    private static final String CLASS = 'class'
    private static final String AVATAR = 'avatar'
    private static final String FIRST_NAME = 'firstName'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String PROFILE_EMAIL = 'profileEmail'

    def user = Mock User
    def image = Mock Image
    def passport = Mock Passport

    def userService = Mock UserService
    def clubProfile = Mock ClubProfile
    def userProfile = Mock UserProfile
    def imageService = Mock ImageService
    def passportService = Mock PassportService

    def setup() {
        GroovySpy(ClubProfile, global: true)
        GroovySpy(UserProfile, global: true)

        service.userService = userService
        service.imageService = imageService
        service.passportService = passportService
    }

    def cleanup() {
    }

    void "Test getProfile()"() {
        when:
        def result = service.getProfile(ClubProfile, ONE)

        then:
        1 * ClubProfile.findByProfileId(ONE) >> clubProfile
        0 * _

        and:
        result == clubProfile

        when:
        result = service.getProfile(UserProfile, ONE)

        then:
        1 * UserProfile.findByProfileId(ONE) >> null
        1 * UserProfile.get(ONE) >> userProfile
        0 * _

        and:
        result == userProfile
    }

    void "Test getProfileByProfileEmail()"() {
        when:
        def result = service.getProfileByProfileEmail(UserProfile, PROFILE_EMAIL)

        then:
        1 * UserProfile.findByProfileEmail(PROFILE_EMAIL) >> userProfile
        0 * _

        result == userProfile
    }

    void "Test createProfile()"() {
        when:
        def result = service.createProfile(clubProfile, image)

        then:
        1 * clubProfile.getProperty(PROFILE_EMAIL)
        1 * clubProfile.setProperty(AVATAR, image)
        1 * userService.currentUser >> user
        1 * clubProfile.setProperty(USER, user)
        1 * clubProfile.save() >> clubProfile
        0 * _

        and:
        result == clubProfile
    }

    void "Test deleteProfile()"() {
        when:
        service.deleteProfile(clubProfile)

        then:
        1 * clubProfile.getProperty(AVATAR) >> image
        1 * imageService.deleteImage(image)
        1 * clubProfile.delete()
        0 * _
    }

    void "Test editProfileField()"() {
        when:
        def result = service.editProfileField(userProfile, FIRST_NAME, FIELD_VALUE)

        then:
        1 * userProfile.setProperty(FIRST_NAME, FIELD_VALUE)
        1 * userProfile.save() >> userProfile
        0 * _

        and:
        result == userProfile
    }

    void "Test uploadAvatar()"() {
        when:
        def result = service.uploadAvatar(userProfile, image)

        then:
        1 * userProfile.setProperty(AVATAR, image)
        1 * userProfile.save() >> userProfile
        0 * _

        and:
        result == userProfile
    }

    void "Test deleteAvatar()"() {
        when:
        service.deleteAvatar(userProfile)

        then:
        1 * userProfile.getProperty(AVATAR) >> image
        1 * imageService.deleteImage(image)
        1 * userProfile.setProperty(AVATAR, null)
        1 * userProfile.save() >> userProfile
        0 * _
    }

    void "Test isProfileEmailUnique() for clubProfile"() {
        when:
        def result = service.isProfileEmailUnique(clubProfile, EMAIL)

        then:
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * clubProfile.getProperty(CLASS) >> ClubProfile
        1 * UserProfile.findByProfileEmail(EMAIL) >> userProfile
        1 * userProfile.getProperty(USER) >> user
        1 * clubProfile.getProperty(USER) >> user
        0 * _

        and:
        result == Boolean.TRUE
    }
}
