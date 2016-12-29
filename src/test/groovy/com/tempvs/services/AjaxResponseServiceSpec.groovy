package com.tempvs.services

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.tests.unit.UnitTestUtils
import grails.converters.JSON
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */

@TestFor(AjaxResponseService)
@Mock([User, UserProfile])
class AjaxResponseServiceSpec extends Specification {
    private static final SUCCESS_MESSAGE = 'Success Message'
    private static final FAIL_MESSAGE = 'Fail Message'

    def setup() {
        service.ajaxResponseFactory = [newInstance:{ instance, successMessage = null ->
            Boolean success = instance.validate()
            [success: success, messages: [success ? successMessage : FAIL_MESSAGE]]
        }]
    }

    def cleanup() {
    }

    void "Check JSON response for correct user"() {
        expect:"Service returned success for correct user"
        JSON response = service.composeJsonResponse(UnitTestUtils.createUser(), SUCCESS_MESSAGE)
        response.target.success == Boolean.TRUE
        response.target.messages == [SUCCESS_MESSAGE]
    }

    void "Check JSON response for incorrect user"() {
        expect:"Service returned fail for incorrect user"
        JSON response = service.composeJsonResponse(new User())
        response.target.success == Boolean.FALSE
        response.target.messages == [FAIL_MESSAGE]
    }
}
