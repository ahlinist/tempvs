import com.tempvs.mongodb.MongoImageDAO
import com.tempvs.mongodb.DBObjectFactory
import com.tempvs.mongodb.GridFSFactory
import com.tempvs.mongodb.MongoImageFactory

beans = {
    gridFSFactory(GridFSFactory)
    dBObjectFactory(DBObjectFactory)
    imageFactory(MongoImageFactory)

    imageDAO(MongoImageDAO) {
        gridFSFactory = ref("gridFSFactory")
        dBObjectFactory = ref("dBObjectFactory")
        imageFactory = ref("imageFactory")
    }
}
