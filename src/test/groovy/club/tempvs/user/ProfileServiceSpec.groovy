package club.tempvs.user

import club.tempvs.image.Image
import club.tempvs.image.ImageService
import grails.orm.HibernateCriteriaBuilder
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class ProfileServiceSpec extends Specification implements ServiceUnitTest<ProfileService>, DomainUnitTest<Profile> {

    private static final Long LONG_ONE = 1L
    private static final String EMAIL = 'email'
    private static final String FIRST_NAME = 'firstName'
    private static final String FIELD_VALUE = 'fieldValue'

    def user = Mock User
    def image = Mock Image
    def criteria = Mock HibernateCriteriaBuilder

    def userService = Mock UserService
    def profile = Mock Profile
    def imageService = Mock ImageService

    def setup() {
        GroovySpy(Profile, global: true)

        service.userService = userService
        service.imageService = imageService
    }

    def cleanup() {
    }

    void "Test searchProfiles()"() {
        given:
        String query = 'query'
        Integer max = 10
        Integer offset = 0

        when:
        def result = service.searchProfiles(profile, query, offset)

        then:
        1 * Profile.createCriteria() >> criteria
        1 * criteria.list(['max': max, 'offset': offset], _ as Closure) >> [profile]
        0 * _

        and:
        result == [profile]
    }

    void "Test setCurrentProfile()"() {
        when:
        service.setCurrentProfile(user, profile)

        then:
        1 * profile.id >> LONG_ONE
        1 * user.setCurrentProfileId(LONG_ONE)
        1 * user.save([flush: true])
        0 * _
    }

    void "Test createProfile()"() {
        when:
        def result = service.createProfile(user, profile, image)

        then:
        1 * profile.setUser(user)
        1 * profile.setAvatar(image)
        1 * user.addToProfiles(profile)
        1 * profile.profileEmail
        1 * profile.save() >> profile
        0 * _

        and:
        result == profile
    }

    void "Test deactivateProfile()"() {
        when:
        def result = service.deactivateProfile(profile)

        then:
        1 * profile.setActive(Boolean.FALSE)
        1 * profile.save()
        0 * _

        and:
        result == profile
    }

    void "Test activateProfile()"() {
        when:
        def result = service.activateProfile(profile)

        then:
        1 * profile.setActive(Boolean.TRUE)
        1 * profile.save()
        0 * _

        and:
        result == profile
    }

    void "Test editProfileField()"() {
        when:
        def result = service.editProfileField(profile, FIRST_NAME, FIELD_VALUE)

        then:
        1 * profile.setFirstName(FIELD_VALUE)
        1 * profile.save() >> profile
        0 * _

        and:
        result == profile
    }

    void "Test uploadAvatar()"() {
        when:
        def result = service.uploadAvatar(profile, image)

        then:
        1 * profile.avatar >> image
        1 * imageService.deleteImage(image)
        1 * profile.setAvatar(image)
        1 * profile.save() >> profile
        0 * _

        and:
        result == profile
    }

    void "Test deleteAvatar()"() {
        when:
        service.deleteAvatar(profile)

        then:
        1 * profile.avatar >> image
        1 * imageService.deleteImage(image)
        1 * profile.setAvatar(null)
        1 * profile.save() >> profile
        0 * _
    }

    void "Test isProfileEmailUnique()"() {
        when:
        def result = service.isProfileEmailUnique(profile, EMAIL)

        then:
        1 * profile.user >> user
        1 * userService.getUserByEmail(EMAIL) >> user
        1 * Profile.findAllByProfileEmail(EMAIL) >> [profile]
        1 * profile.user >> user
        0 * _

        and:
        result == Boolean.TRUE
    }

    void "Test getProfileDropdown()"() {
        when:
        def result = service.getProfileDropdown(user)

        then:
        1 * user.userProfile >> profile
        1 * user.clubProfiles >> [profile]
        1 * profile.active >> true
        1 * user.currentProfile >> profile
        1 * profile.id >> LONG_ONE
        3 * profile.toString() >> "I'm a profile"
        0 * _

        and:
        result == [
                current: ["I'm a profile"],
                user   : ["I'm a profile"],
                club   : [[id: 1, name: "I'm a profile"]],
        ]
    }
}
