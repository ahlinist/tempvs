import com.tempvs.mongodb.GridFSFactory
import com.tempvs.mongodb.MongoImageDAO
import com.tempvs.user.ProfileHolder
import org.grails.plugins.web.taglib.ValidationTagLib

beans = {
    gridFSFactory(GridFSFactory)

    imageDAO(MongoImageDAO) {
        gridFSFactory = ref("gridFSFactory")
    }

    profileHolder(ProfileHolder) { bean ->
        bean.scope = 'session'
        userService = ref("userService")
        profileService = ref("profileService")
    }

    validationTagLib(ValidationTagLib)
}
