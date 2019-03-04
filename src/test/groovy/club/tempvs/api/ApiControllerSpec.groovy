package club.tempvs.api

import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import com.netflix.discovery.EurekaClient
import grails.converters.JSON
import grails.testing.web.controllers.ControllerUnitTest
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Specification

class ApiControllerSpec extends Specification implements ControllerUnitTest<ApiController> {

    RestCaller restCaller = Mock RestCaller
    EurekaClient eurekaClient = Mock EurekaClient
    RestResponse restResponse = Mock RestResponse

    def setup() {
        controller.restCaller = restCaller
        controller.eurekaClient = eurekaClient
    }

    def cleanup() {
    }

    void "test call()"() {
        given:
        String service = 'service'
        String uri = 'uri'
        request.method = 'POST'
        HttpStatus httpStatus = HttpStatus.OK
        byte[] bytes = "".bytes

        when:
        controller.call(service, uri)

        then:
        1 * eurekaClient.getApplication(service)
        1 * restCaller.call(_ as String, HttpMethod.POST, _ as JSON) >> restResponse
        1 * restResponse.statusCode >> httpStatus
        1 * restResponse.headers
        1 * restResponse.image >> false
        1 * restResponse.responseBody >> bytes
        0 * _

        and:
        response.status == 200
    }
}
