package club.tempvs.message

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class MessageControllerSpec extends Specification implements ControllerUnitTest<MessageController> {

    def setup() {

    }

    def cleanup() {
    }

    void "test conversation()"() {
        when:
        controller.conversation()

        then:
        0 * _

        and:
        !controller.modelAndView
        !response.redirectedUrl
    }
}
