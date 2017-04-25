import com.tempvs.hibernate.HibernateObjectDAO
import com.tempvs.mongodb.DBObjectFactory
import com.tempvs.mongodb.GridFSFactory
import com.tempvs.mongodb.MongoImageDAO
import com.tempvs.mongodb.MongoImageFactory
import com.tempvs.user.ProfileHolder

beans = {
    gridFSFactory(GridFSFactory)
    dBObjectFactory(DBObjectFactory)
    imageFactory(MongoImageFactory)

    imageDAO(MongoImageDAO) {
        gridFSFactory = ref("gridFSFactory")
        dBObjectFactory = ref("dBObjectFactory")
        imageFactory = ref("imageFactory")
    }

    profileHolder(ProfileHolder) { bean ->
        bean.scope = 'session'
        springSecurityService = ref("springSecurityService")
        objectDAO = ref("objectDAO")
    }

    objectDAO(HibernateObjectDAO) {
        sessionFactory = ref("sessionFactory")
    }
}
