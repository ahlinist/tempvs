import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.image.ImageTagLib
import club.tempvs.mongodb.GridFSFactory
import club.tempvs.mongodb.MongoImageDAO
import club.tempvs.user.UserPasswordEncoderListener
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

beans = {
    ajaxResponseHelper(AjaxResponseHelper) {
        validationTagLib = ref("validationTagLib")
    }

    imageDAO(MongoImageDAO) {
        gridFSFactory = ref("gridFSFactory")
    }

    passwordEncoder(BCryptPasswordEncoder)
    imageTagLib(ImageTagLib)
    gridFSFactory(GridFSFactory)
    validationTagLib(ValidationTagLib)
    userPasswordEncoderListener(UserPasswordEncoderListener)
}
