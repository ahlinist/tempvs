import com.tempvs.domain.hibernate.HibernateObjectDAO
import com.tempvs.mongodb.DBObjectFactory
import com.tempvs.mongodb.GridFSFactory
import com.tempvs.mongodb.MongoImageBeanDAO
import com.tempvs.mongodb.MongoImageBeanFactory
import com.tempvs.user.ProfileHolder
import com.tempvs.domain.ObjectFactory

beans = {
    gridFSFactory(GridFSFactory)
    dBObjectFactory(DBObjectFactory)
    imageBeanFactory(MongoImageBeanFactory)

    imageBeanDAO(MongoImageBeanDAO) {
        gridFSFactory = ref("gridFSFactory")
        dBObjectFactory = ref("dBObjectFactory")
        imageBeanFactory = ref("imageBeanFactory")
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
}
