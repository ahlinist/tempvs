import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.mongodb.GridFSFactory
import club.tempvs.mongodb.MongoImageDAO
import club.tempvs.rest.ConnectionFactory
import club.tempvs.user.UserPasswordEncoderListener
import grails.plugin.springsecurity.SecurityTagLib
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

beans = {
    ajaxResponseHelper(AjaxResponseHelper) {
        validationTagLib = ref("validationTagLib")
    }

    imageDAO(MongoImageDAO) {
        gridFSFactory = ref("gridFSFactory")
    }

    connectionFactory(ConnectionFactory)
    gridFSFactory(GridFSFactory)
    securityTagLib(SecurityTagLib)
    validationTagLib(ValidationTagLib)
    passwordEncoder(BCryptPasswordEncoder)
    userPasswordEncoderListener(UserPasswordEncoderListener)
}
