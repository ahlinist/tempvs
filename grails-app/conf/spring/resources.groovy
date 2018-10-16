import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.ampq.AmqpSender
import club.tempvs.json.JsonConverter
import club.tempvs.message.MessageProxy
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestHelper
import club.tempvs.user.UserPasswordEncoderListener
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

beans = {
    ajaxResponseHelper(AjaxResponseHelper) {
        validationTagLib = ref "validationTagLib"
    }

    restCaller(RestCaller) {
        restHelper = ref "restHelper"
    }

    messageProxy(MessageProxy) {
        restCaller = ref "restCaller"
        objectFactory = ref "objectFactory"
    }

    objectFactory(ObjectFactory)
    restHelper(RestHelper)
    validationTagLib(ValidationTagLib)
    passwordEncoder(BCryptPasswordEncoder)
    userPasswordEncoderListener(UserPasswordEncoderListener)
    jsonConverter(JsonConverter)
    amqpSender(AmqpSender)
}
