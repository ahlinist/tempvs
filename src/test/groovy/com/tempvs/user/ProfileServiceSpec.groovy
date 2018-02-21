package com.tempvs.user

import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.item.PassportService
import grails.orm.HibernateCriteriaBuilder
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class ProfileServiceSpec extends Specification implements ServiceUnitTest<ProfileService>, DomainUnitTest<UserProfile> {

    private static final String ONE = '1'
    private static final Long LONG_ONE = 1L
    private static final String USER = 'user'
    private static final String EMAIL = 'email'
    private static final String CLASS = 'class'
    private static final String FIRST_NAME = 'firstName'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String PROFILE_EMAIL = 'profileEmail'

    def user = Mock User
    def image = Mock Image
    def criteria = Mock HibernateCriteriaBuilder

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

    void "Test searchProfiles()"() {
        given:
        String query = 'query'
        Integer max = 10
        Integer offset = 0

        when:
        def result = service.searchProfiles(userProfile, query, offset)

        then:
        1 * userProfile.getProperty(CLASS) >> UserProfile
        1 * UserProfile.createCriteria() >> criteria
        1 * criteria.list(['max': max, 'offset': offset], _ as Closure) >> [userProfile]
        0 * _

        and:
        result == [userProfile]
    }

    void "Test setCurrentProfile()"() {
        when:
        service.setCurrentProfile(clubProfile)

        then:
        1 * clubProfile.user >> user
        1 * clubProfile.id >> LONG_ONE
        1 * user.setCurrentProfileClass(_ as Class)
        1 * user.setCurrentProfileId(LONG_ONE)
        1 * user.save([flush: true])
        0 * _
    }

    void "Test createProfile()"() {
        when:
        def result = service.createProfile(clubProfile, image)

        then:
        1 * clubProfile.profileEmail
        1 * clubProfile.setAvatar(image)
        1 * clubProfile.save() >> clubProfile
        0 * _

        and:
        result == clubProfile
    }

    void "Test deactivateProfile()"() {
        when:
        def result = service.deactivateProfile(clubProfile)

        then:
        1 * clubProfile.setActive(Boolean.FALSE)
        1 * clubProfile.save()
        0 * _

        and:
        result == clubProfile
    }

    void "Test activateProfile()"() {
        when:
        def result = service.activateProfile(clubProfile)

        then:
        1 * clubProfile.setActive(Boolean.TRUE)
        1 * clubProfile.save()
        0 * _

        and:
        result == clubProfile
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
        1 * userProfile.setAvatar(image)
        1 * userProfile.save() >> userProfile
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
