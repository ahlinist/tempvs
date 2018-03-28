import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.image.ImageTagLib
import club.tempvs.mongodb.GridFSFactory
import club.tempvs.mongodb.MongoImageDAO
import club.tempvs.user.UserInfoHelper
import club.tempvs.user.UserPasswordEncoderListener
import org.grails.plugins.web.taglib.ValidationTagLib

beans = {
    ajaxResponseHelper(AjaxResponseHelper) {
        validationTagLib = ref("validationTagLib")
    }

    imageDAO(MongoImageDAO) {
        gridFSFactory = ref("gridFSFactory")
    }

    userInfoHelper(UserInfoHelper) {
        profileService = ref("profileService")
    }

    imageTagLib(ImageTagLib)
    gridFSFactory(GridFSFactory)
    validationTagLib(ValidationTagLib)
    userPasswordEncoderListener(UserPasswordEncoderListener)
}
