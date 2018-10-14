package club.tempvs.message

import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import grails.compiler.GrailsCompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

@GrailsCompileStatic
class MessageProxy {

    private static final String MESSAGE_SERVICE_URL = System.getenv('MESSAGE_SERVICE_URL')
    private static final String MESSAGE_SECURITY_TOKEN = System.getenv('MESSAGE_SECURITY_TOKEN')

    @Autowired
    RestCaller restCaller
    @Autowired
    JsonConverter jsonConverter
    @Autowired
    ObjectFactory objectFactory

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    ConversationsDto getConversations(Profile profile, int page, int size) {
        String url = "${MESSAGE_SERVICE_URL}/api/conversations?participant=${profile.id}&page=${page}&size=${size}"
        String token = MESSAGE_SECURITY_TOKEN.encodeAsMD5() as String

        RestResponse response = restCaller.doGet(url, token)

        if (response.statusCode == HttpStatus.OK) {
            return jsonConverter.convert(ConversationsDto, response.responseBody)
        } else {
            return objectFactory.getInstance(ConversationsDto)
        }
    }
}
