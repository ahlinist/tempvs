package com.tempvs.image

import groovy.transform.CompileStatic

/**
 * Represents a DAO layer for {@link com.tempvs.image.Image} managing.
 */
@CompileStatic
interface ImageDAO {
    Image get(String collection, String id)
    Image create(InputStream inputStream, String collection)
    Image save(Image image, Map metaData)
    Boolean delete(String collection, String id)
}