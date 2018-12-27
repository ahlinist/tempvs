package club.tempvs.rest

import club.tempvs.object.ObjectFactory
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import club.tempvs.user.User
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
    private static final String PROFILE_HEADER = 'Profile'
    private static final String AUTHORIZATION_HEADER = 'Authorization'
    private static final String ACCEPT_LANGUAGE = 'Accept-Language'
    private static final String ACCEPT_TIMEZONE = 'Accept-Timezone'
    private static final String CONTENT_TYPE_HEADER = 'Content-Type'
    private static final String JSON_CONTENT_TYPE_VALUE = 'application/json; charset=UTF-8'

    ProfileService profileService
    RestTemplate restTemplate
    ObjectFactory objectFactory

    RestResponse call(String url, HttpMethod httpMethod, String token = null, JSON payload = null) {
        Profile currentProfile = profileService.currentProfile
        User user = currentProfile?.user
        HttpHeaders httpHeaders = objectFactory.getInstance(HttpHeaders)
        httpHeaders.set(USER_AGENT_HEADER, MOZILLA_USER_AGENT_VALUE)
        httpHeaders.set(PROFILE_HEADER, currentProfile?.id?.toString())
        httpHeaders.set(AUTHORIZATION_HEADER, token)
        httpHeaders.set(ACCEPT_LANGUAGE, LocaleContextHolder.locale.language)
        httpHeaders.set(ACCEPT_TIMEZONE, user?.timeZone)

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
