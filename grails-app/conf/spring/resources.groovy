import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.ampq.AmqpProcessor
import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.RestCaller
import club.tempvs.user.RoleRefreshJob
import club.tempvs.user.UserPasswordEncoderListener
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

beans = {
    ajaxResponseHelper(AjaxResponseHelper) {
        validationTagLib = ref "validationTagLib"
    }

    restCaller(RestCaller) {
        restTemplate = ref "restTemplate"
    }

    amqpProcessor(AmqpProcessor) {
        amqpConnectionFactory = ref "amqpConnectionFactory"
    }

    roleRefresher(RoleRefreshJob) {
        amqpProcessor = ref "amqpProcessor"
        jsonConverter = ref "jsonConverter"
    }

    objectFactory(ObjectFactory)
    validationTagLib(ValidationTagLib)
    passwordEncoder(BCryptPasswordEncoder)
    userPasswordEncoderListener(UserPasswordEncoderListener)
    jsonConverter(JsonConverter)
}
