import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.ampq.AmqpSender
import club.tempvs.json.JsonConverter
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.RestCaller
import club.tempvs.user.UserPasswordEncoderListener
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

beans = {
    ajaxResponseHelper(AjaxResponseHelper) {
        validationTagLib = ref "validationTagLib"
    }

    restCaller(RestCaller) {
        profileService = ref "profileService"
        restTemplate = ref "restTemplate"
        objectFactory = ref "objectFactory"
    }

    objectFactory(ObjectFactory)
    validationTagLib(ValidationTagLib)
    passwordEncoder(BCryptPasswordEncoder)
    userPasswordEncoderListener(UserPasswordEncoderListener)
    jsonConverter(JsonConverter)
    amqpSender(AmqpSender)
}
