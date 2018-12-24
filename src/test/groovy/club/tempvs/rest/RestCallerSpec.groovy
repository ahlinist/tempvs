package club.tempvs.rest

import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import club.tempvs.user.User
import grails.converters.JSON
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class RestCallerSpec extends Specification {

    private static final Long LONG_ONE = 1L
    private static final String STRING_ONE = "1"
    private static final String URL = 'url'
    private static final String PROFILE = 'Profile'
    private static final String AUTHORIZATION = 'Authorization'
    private static final String ACCEPT_LANGUAGE = 'Accept-Language'
    private static final String ACCEPT_TIMEZONE = 'Accept-Timezone'
    private static final String TIMEZONE = 'Europe/Minsk'
    private static final String ENGLISH = 'en'
    private static final String TEST_JSON = 'test json'
    private static final String CONTENT_TYPE = 'Content-Type'
    private static final String JSON_CONTENT_TYPE = 'application/json; charset=UTF-8'

    def json = Mock JSON
    def user = Mock User
    def profile = Mock Profile
    def restHelper = Mock RestHelper
    def httpEntity = Mock HttpEntity
    def restTemplate = Mock RestTemplate
    def restResponse = Mock RestResponse
    def responseEntity = Mock ResponseEntity
    def profileService = Mock ProfileService

    RestCaller restCaller

    def setup() {
        restCaller = new RestCaller()
        restCaller.restHelper = restHelper
        restCaller.profileService = profileService
    }

    def cleanup() {
    }

    void "Test doGet()"() {
        given:
        Map<String, String> headers = [
                (PROFILE) : STRING_ONE,
                (AUTHORIZATION): AUTHORIZATION,
                (ACCEPT_LANGUAGE): ENGLISH,
                (ACCEPT_TIMEZONE): TIMEZONE,
        ]

        when:
        def result = restCaller.doGet(URL, AUTHORIZATION)

        then:
        1 * restHelper.newTemplate() >> restTemplate
        1 * profileService.currentProfile >> profile
        1 * profile.id >> LONG_ONE
        1 * profile.user >> user
        1 * user.timeZone >> TIMEZONE
        1 * restHelper.newHttpEntity(headers) >> httpEntity
        1 * restTemplate.exchange(URL, HttpMethod.GET, httpEntity, String.class) >> responseEntity
        1 * restHelper.newRestResponse(responseEntity) >> restResponse
        0 * _

        and:
        result == restResponse
    }

    void "Test doHead()"() {
        given:
        Map<String, String> headers = [
                (PROFILE) : STRING_ONE,
                (AUTHORIZATION): AUTHORIZATION,
                (ACCEPT_LANGUAGE): ENGLISH,
                (ACCEPT_TIMEZONE): TIMEZONE,
        ]

        when:
        def result = restCaller.doHead(URL, AUTHORIZATION)

        then:
        1 * restHelper.newTemplate() >> restTemplate
        1 * profileService.currentProfile >> profile
        1 * profile.id >> LONG_ONE
        1 * profile.user >> user
        1 * user.timeZone >> TIMEZONE
        1 * restHelper.newHttpEntity(headers) >> httpEntity
        1 * restTemplate.exchange(URL, HttpMethod.HEAD, httpEntity, String.class) >> responseEntity
        1 * restHelper.newRestResponse(responseEntity) >> restResponse
        0 * _

        and:
        result == restResponse
    }

    void "Test doPost()"() {
        given:
        Map<String, String> headers = [
                (PROFILE) : STRING_ONE,
                (AUTHORIZATION): AUTHORIZATION,
                (ACCEPT_LANGUAGE): ENGLISH,
                (ACCEPT_TIMEZONE): TIMEZONE,
                (CONTENT_TYPE): JSON_CONTENT_TYPE,
        ]

        when:
        def result = restCaller.doPost(URL, AUTHORIZATION, json)

        then:
        1 * restHelper.newTemplate() >> restTemplate
        1 * json.toString() >> TEST_JSON
        1 * profileService.currentProfile >> profile
        1 * profile.id >> LONG_ONE
        1 * profile.user >> user
        1 * user.timeZone >> TIMEZONE
        1 * restHelper.newHttpEntity(headers, TEST_JSON) >> httpEntity
        1 * restTemplate.exchange(URL, HttpMethod.POST, httpEntity, String.class) >> responseEntity
        1 * restHelper.newRestResponse(responseEntity) >> restResponse
        0 * _

        and:
        result instanceof RestResponse
    }

    void "Test doDelete()"() {
        given:
        Map<String, String> headers = [
                (PROFILE) : STRING_ONE,
                (AUTHORIZATION): AUTHORIZATION,
                (ACCEPT_LANGUAGE): ENGLISH,
                (ACCEPT_TIMEZONE): TIMEZONE,
        ]

        when:
        def result = restCaller.doDelete(URL, AUTHORIZATION)

        then:
        1 * restHelper.newTemplate() >> restTemplate
        1 * profileService.currentProfile >> profile
        1 * profile.id >> LONG_ONE
        1 * profile.user >> user
        1 * user.timeZone >> TIMEZONE
        1 * restHelper.newHttpEntity(headers) >> httpEntity
        1 * restTemplate.exchange(URL, HttpMethod.DELETE, httpEntity, String.class) >> responseEntity
        1 * restHelper.newRestResponse(responseEntity) >> restResponse
        0 * _

        and:
        result == restResponse
    }
}
