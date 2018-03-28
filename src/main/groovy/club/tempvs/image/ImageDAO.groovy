package club.tempvs.image

import groovy.transform.CompileStatic

/**
 * Represents a DAO layer for {@link ImageBean} managing.
 */
@CompileStatic
interface ImageDAO {
    ImageBean get(String collection, String objectId)
    ImageBean create(InputStream inputStream, String collection)
    Boolean delete(String collection, String objectId)
}
