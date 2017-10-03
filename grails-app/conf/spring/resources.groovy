import com.tempvs.mongodb.GridFSFactory
import com.tempvs.mongodb.MongoImageDAO
import com.tempvs.user.ProfileHolder
import org.grails.plugins.web.taglib.ValidationTagLib

beans = {
    gridFSFactory(GridFSFactory)

    imageDAO(MongoImageDAO) {
        gridFSFactory = ref("gridFSFactory")
        objectDAOService = ref("objectDAOService")
    }

    profileHolder(ProfileHolder) { bean ->
        bean.scope = 'session'
        userService = ref("userService")
        objectDAOService = ref("objectDAOService")
    }

    validationTagLib(ValidationTagLib)
}
