package club.tempvs.image

import groovy.transform.CompileStatic

/**
 * Represents an image object.
 */
@CompileStatic
interface ImageBean {
    ImageBean save()
    void setMetaData(Map metaData)
    String getId()
}