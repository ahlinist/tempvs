package com.tempvs.image

import groovy.transform.CompileStatic

/**
 * Represents a DAO layer for {@link com.tempvs.image.ImageBean} managing.
 */
@CompileStatic
interface ImageBeanDAO {
    ImageBean get(String collection, String id)
    ImageBean create(InputStream inputStream, String collection)
    ImageBean save(ImageBean image, Map metaData)
    Boolean delete(String collection, Collection<String> objectIds)
}
