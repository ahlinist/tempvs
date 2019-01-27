package club.tempvs.rest

import club.tempvs.object.ObjectFactory
import club.tempvs.user.Profile
import club.tempvs.user.User
import club.tempvs.user.UserService
import grails.converters.JSON
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

@TestMixin(ControllerUnitTestMixin)
class RestCallerSpec extends Specification {

    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final String URL = 'url'
    private static final String TOKEN = 'token'
    private static final String TIMEZONE = 'Europe/Minsk'
    private static final String MOZILLA_USER_AGENT_VALUE = 'Mozilla/5.0'
    private static final String USER_AGENT_HEADER = 'User-Agent'
    private static final String USER_INFO_HEADER = 'User-Info'
    private static final String AUTHORIZATION_HEADER = 'Authorization'
    private static final String CONTENT_TYPE_HEADER = 'Content-Type'
    private static final String JSON_CONTENT_TYPE_VALUE = 'application/json; charset=UTF-8'

    def json = Mock JSON
    def user = Mock User
    def userService = Mock UserService
    def profile = Mock Profile
    def httpEntity = Mock HttpEntity
    def restTemplate = Mock RestTemplate
    def restResponse = Mock RestResponse
    def responseEntity = Mock ResponseEntity
    def objectFactory = Mock ObjectFactory
    def httpHeaders = Mock HttpHeaders

    RestCaller restCaller

    def setup() {
        restCaller = new RestCaller()
        restCaller.userService = userService
        restCaller.objectFactory = objectFactory
        restCaller.restTemplate = restTemplate
    }

    def cleanup() {
    }

    void "Test call() for POST"() {
        given:
        String body = "body"
        String role = "role"
        List<String> roles = [role, role]

        when:
        def result = restCaller.call(URL, HttpMethod.POST, TOKEN, json)

        then:
        1 * userService.currentProfile >> profile
        1 * userService.roles >> roles
        1 * profile.user >> user
        2 * profile.id >> LONG_ONE
        1 * user.id >> LONG_TWO
        1 * user.userProfile >> profile
        1 * profile.toString()
        1 * user.timeZone >> TIMEZONE
        1 * objectFactory.getInstance(HttpHeaders) >> httpHeaders
        1 * httpHeaders.set(USER_AGENT_HEADER, MOZILLA_USER_AGENT_VALUE)
        1 * httpHeaders.set(USER_INFO_HEADER, _ as String)
        1 * httpHeaders.set(AUTHORIZATION_HEADER, TOKEN)
        1 * httpHeaders.set(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE_VALUE)
        1 * objectFactory.getInstance(HttpEntity, _, httpHeaders) >> httpEntity
        1 * restTemplate.exchange(URL, HttpMethod.POST, httpEntity, String.class) >> responseEntity
        1 * responseEntity.statusCode >> HttpStatus.OK
        1 * responseEntity.body >> body
        1 * responseEntity.headers >> httpHeaders
        1 * objectFactory.getInstance(RestResponse, HttpStatus.OK, body, httpHeaders) >> restResponse
        0 * _

        and:
        result == restResponse
    }

    void "Test call() for GET"() {
        given:
        String body = "body"
        String role = "role"
        List<String> roles = [role, role]

        when:
        def result = restCaller.call(URL, HttpMethod.GET, TOKEN, json)

        then:
        1 * userService.currentProfile >> profile
        1 * userService.roles >> roles
        1 * profile.user >> user
        2 * profile.id >> LONG_ONE
        1 * user.id >> LONG_TWO
        1 * user.userProfile >> profile
        1 * profile.toString()
        1 * user.timeZone >> TIMEZONE
        1 * objectFactory.getInstance(HttpHeaders) >> httpHeaders
        1 * httpHeaders.set(USER_AGENT_HEADER, MOZILLA_USER_AGENT_VALUE)
        1 * httpHeaders.set(USER_INFO_HEADER, _ as String)
        1 * httpHeaders.set(AUTHORIZATION_HEADER, TOKEN)
        1 * objectFactory.getInstance(HttpEntity, _, httpHeaders) >> httpEntity
        1 * restTemplate.exchange(URL, HttpMethod.GET, httpEntity, String.class) >> responseEntity
        1 * responseEntity.statusCode >> HttpStatus.OK
        1 * responseEntity.body >> body
        1 * responseEntity.headers >> httpHeaders
        1 * objectFactory.getInstance(RestResponse, HttpStatus.OK, body, httpHeaders) >> restResponse
        0 * _

        and:
        result == restResponse
    }
}
