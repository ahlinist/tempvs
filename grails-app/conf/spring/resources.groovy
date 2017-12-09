import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.mongodb.GridFSFactory
import com.tempvs.mongodb.MongoImageDAO
import com.tempvs.user.ProfileHolder
import org.grails.plugins.web.taglib.ValidationTagLib

beans = {
    ajaxResponseHelper(AjaxResponseHelper) {
        validationTagLib = ref("validationTagLib")
    }

    imageDAO(MongoImageDAO) {
        gridFSFactory = ref("gridFSFactory")
    }

    profileHolder(ProfileHolder) { bean ->
        bean.scope = 'session'
        userService = ref("userService")
        profileService = ref("profileService")
    }

    gridFSFactory(GridFSFactory)
    validationTagLib(ValidationTagLib)
}
