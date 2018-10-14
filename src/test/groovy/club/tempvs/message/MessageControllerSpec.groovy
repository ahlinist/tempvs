package club.tempvs.message

import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class MessageControllerSpec extends Specification implements ControllerUnitTest<MessageController> {

    def profile = Mock Profile
    def conversationsDto = Mock ConversationsDto
    def profileService = Mock ProfileService
    def messageProxy = Mock MessageProxy

    def setup() {
        controller.profileService = profileService
        controller.messageProxy = messageProxy
    }

    def cleanup() {
    }

    void "test index()"() {
        given:
        int page = 0
        int size = 20

        when:
        def result = controller.index()

        then:
        1 * profileService.currentProfile >> profile
        1 * messageProxy.getConversations(profile, page, size) >> conversationsDto
        0 * _

        and:
        result == [conversationsDto: conversationsDto]
    }
}
