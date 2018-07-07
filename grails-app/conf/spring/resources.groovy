import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.object.ObjectFactory
import club.tempvs.rest.ConnectionFactory
import club.tempvs.rest.RestCaller
import club.tempvs.user.UserPasswordEncoderListener
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

beans = {
    ajaxResponseHelper(AjaxResponseHelper) {
        validationTagLib = ref "validationTagLib"
    }

    restCaller(RestCaller) {
        connectionFactory = ref "connectionFactory"
    }

    objectFactory(ObjectFactory)
    connectionFactory(ConnectionFactory)
    validationTagLib(ValidationTagLib)
    passwordEncoder(BCryptPasswordEncoder)
    userPasswordEncoderListener(UserPasswordEncoderListener)
}
