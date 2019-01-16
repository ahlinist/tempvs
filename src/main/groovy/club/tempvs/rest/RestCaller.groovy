package club.tempvs.rest

import club.tempvs.object.ObjectFactory
import club.tempvs.user.Profile
import club.tempvs.user.User
import club.tempvs.user.UserService
import grails.converters.JSON
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import org.springframework.context.i18n.LocaleContextHolder

@Slf4j
@CompileStatic
class RestCaller {

    private static final String MOZILLA_USER_AGENT_VALUE = 'Mozilla/5.0'
    private static final String USER_AGENT_HEADER = 'User-Agent'
    private static final String USER_INFO_HEADER = 'User-Info'
    private static final String AUTHORIZATION_HEADER = 'Authorization'
    private static final String CONTENT_TYPE_HEADER = 'Content-Type'
    private static final String JSON_CONTENT_TYPE_VALUE = 'application/json; charset=UTF-8'

    UserService userService
    RestTemplate restTemplate
    ObjectFactory objectFactory

    RestResponse call(String url, HttpMethod httpMethod, String token = null, JSON payload = null) {
        Profile currentProfile = userService.currentProfile
        User user = currentProfile?.user
        List<String> roles = userService.roles

        JSON userInfoJson = [
                userId: user.id as String,
                profileId: currentProfile.id as String,
                timezone: user.timeZone,
                lang: LocaleContextHolder.locale.language,
                roles: roles
        ] as JSON

        HttpHeaders httpHeaders = objectFactory.getInstance(HttpHeaders)
        httpHeaders.set(USER_AGENT_HEADER, MOZILLA_USER_AGENT_VALUE)
        httpHeaders.set(USER_INFO_HEADER, userInfoJson.toString())
        httpHeaders.set(AUTHORIZATION_HEADER, token)

        if (httpMethod == HttpMethod.POST) {
            httpHeaders.set(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE_VALUE)
        }

        HttpEntity<String> httpEntity = objectFactory.getInstance(HttpEntity, payload?.toString(), httpHeaders)

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, httpEntity, String.class)
            objectFactory.getInstance(RestResponse, responseEntity.statusCode, responseEntity.body, responseEntity.headers)
        } catch(ResourceAccessException e) {
            log.error e.message
            return null
        } catch (HttpStatusCodeException e) {
            objectFactory.getInstance(RestResponse, e.statusCode, e.responseBodyAsString)
        }
    }
}
