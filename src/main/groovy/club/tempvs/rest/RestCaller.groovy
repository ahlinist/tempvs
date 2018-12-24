package club.tempvs.rest

import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import club.tempvs.user.User
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import groovy.util.logging.Slf4j
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import org.springframework.context.i18n.LocaleContextHolder

@Slf4j
@GrailsCompileStatic
class RestCaller {

    private static final String PROFILE = 'Profile'
    private static final String AUTHORIZATION = 'Authorization'
    private static final String ACCEPT_LANGUAGE = 'Accept-Language'
    private static final String ACCEPT_TIMEZONE = 'Accept-Timezone'
    private static final String CONTENT_TYPE = 'Content-Type'
    private static final String JSON_CONTENT_TYPE = 'application/json; charset=UTF-8'

    RestHelper restHelper
    ProfileService profileService

    RestResponse doGet(String url, String token = null) {
        Map<String,String> headers = buildHeaders(token)
        RestTemplate restTemplate = restHelper.newTemplate()
        HttpEntity<String> entity = restHelper.newHttpEntity(headers)
        execute(restTemplate, url, HttpMethod.GET, entity)
    }

    RestResponse doHead(String url, String token) {
        Map<String,String> headers = buildHeaders(token)
        RestTemplate restTemplate = restHelper.newTemplate()
        HttpEntity<String> entity = restHelper.newHttpEntity(headers)
        execute(restTemplate, url, HttpMethod.HEAD, entity)
    }

    RestResponse doPost(String url, String token, JSON payload) {
        Map<String,String> headers = buildHeaders(token)
        headers.put(CONTENT_TYPE, JSON_CONTENT_TYPE)
        RestTemplate restTemplate = restHelper.newTemplate()
        HttpEntity<String> entity = restHelper.newHttpEntity(headers, payload?.toString())
        execute(restTemplate, url, HttpMethod.POST, entity)
    }

    RestResponse doDelete(String url, String token) {
        Map<String,String> headers = buildHeaders(token)
        RestTemplate restTemplate = restHelper.newTemplate()
        HttpEntity<String> entity = restHelper.newHttpEntity(headers)
        execute(restTemplate, url, HttpMethod.DELETE, entity)
    }

    private RestResponse execute(RestTemplate restTemplate, String url, HttpMethod httpMethod, HttpEntity<String> httpEntity) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class)
            return restHelper.newRestResponse(responseEntity)
        } catch(ResourceAccessException e) {
            log.error e.message
            return null
        } catch (HttpStatusCodeException e) {
            return restHelper.newRestResponse(e)
        }
    }

    private String getLanguage() {
        LocaleContextHolder.locale.language
    }

    private Map<String, String> buildHeaders(String token) {
        Profile currentProfile = profileService.currentProfile
        User user = currentProfile?.user
        String timeZone = user?.timeZone

        [
                (PROFILE): currentProfile?.id?.toString(),
                (AUTHORIZATION): token,
                (ACCEPT_LANGUAGE): getLanguage(),
                (ACCEPT_TIMEZONE): timeZone,
        ]
    }
}
