import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.mongodb.GridFSFactory
import com.tempvs.mongodb.MongoImageDAO
import com.tempvs.user.UserInfoHelper
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

    gridFSFactory(GridFSFactory)
    validationTagLib(ValidationTagLib)
}
