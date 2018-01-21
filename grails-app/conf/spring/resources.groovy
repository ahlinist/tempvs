import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.mongodb.GridFSFactory
import com.tempvs.mongodb.MongoImageDAO
import org.grails.plugins.web.taglib.ValidationTagLib

beans = {
    ajaxResponseHelper(AjaxResponseHelper) {
        validationTagLib = ref("validationTagLib")
    }

    imageDAO(MongoImageDAO) {
        gridFSFactory = ref("gridFSFactory")
    }

    gridFSFactory(GridFSFactory)
    validationTagLib(ValidationTagLib)
}
