import com.tempvs.domain.ObjectFactory
import com.tempvs.domain.hibernate.HibernateObjectDAO
import com.tempvs.mongodb.GridFSFactory
import com.tempvs.mongodb.MongoImageDAO
import com.tempvs.user.ProfileHolder
import org.grails.plugins.web.taglib.ValidationTagLib

beans = {
    gridFSFactory(GridFSFactory)

    imageDAO(MongoImageDAO) {
        gridFSFactory = ref("gridFSFactory")
        objectFactory = ref("objectFactory")
    }

    profileHolder(ProfileHolder) { bean ->
        bean.scope = 'session'
        userService = ref("userService")
        objectDAO = ref("objectDAO")
    }

    objectDAO(HibernateObjectDAO) {
        sessionFactory = ref("sessionFactory")
    }

    objectFactory(ObjectFactory)
    validationTagLib(ValidationTagLib)
}
