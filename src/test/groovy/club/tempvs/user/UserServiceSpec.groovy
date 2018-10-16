package club.tempvs.user

import club.tempvs.ampq.AmqpSender
import club.tempvs.object.ObjectFactory
import club.tempvs.profile.ProfileDto
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class UserServiceSpec extends Specification implements ServiceUnitTest<UserService>, DomainUnitTest<User> {

    private static final Long LONG_ONE = 1L
    private static final String EMAIL = 'email'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String MESSAGE_PARTICIPANT_AMPQ_QUEUE = 'message.participant'

    def json = Mock JSON
    def user = Mock User
    def profile = Mock Profile
    def verifyService = Mock VerifyService
    def profileService = Mock ProfileService
    def emailVerification = Mock EmailVerification
    def springSecurityService = Mock SpringSecurityService
    def amqpSender = Mock AmqpSender
    def objectFactory = Mock ObjectFactory
    def profileDto = GroovyMock ProfileDto

    def setup() {
        service.verifyService = verifyService
        service.profileService = profileService
        service.springSecurityService = springSecurityService
        service.amqpSender = amqpSender
        service.objectFactory = objectFactory
    }

    def cleanup() {
    }

    void "Test register()"() {
        given:
        String profileString = "firstName lastName"
        String profileDtoAsJsonString = "profileDtoAsJsonString"

        when:
        def result = service.register(user)

        then:
        1 * user.save() >> user
        1 * verifyService.getRegistrationVerificationByUser(user) >> emailVerification
        1 * emailVerification.delete()
        1 * user.userProfile >> profile
        1 * profile.id >> LONG_ONE
        1 * profile.toString() >> profileString
        1 * objectFactory.getInstance(ProfileDto, [id: LONG_ONE, name: profileString]) >> profileDto
        1 * profileDto.asType(JSON) >> json
        1 * json.toString() >> profileDtoAsJsonString
        1 * amqpSender.send(MESSAGE_PARTICIPANT_AMPQ_QUEUE, profileDtoAsJsonString)
        0 * _

        and:
        result == user
    }

    void "Test editUserField()"() {
        when:
        def result = service.editUserField(user, EMAIL, FIELD_VALUE)

        then:
        1 * user.setEmail(FIELD_VALUE)
        1 * user.save() >> user
        0 * _

        and:
        result == user
    }

    void "Test isEmailUnique()"() {
        when:
        def result = service.isEmailUnique(EMAIL)

        then:
        1 * profileService.getProfilesByProfileEmail(EMAIL)
        0 * _

        and:
        result == Boolean.TRUE
    }
}
