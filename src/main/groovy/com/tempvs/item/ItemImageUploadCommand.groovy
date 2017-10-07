package com.tempvs.item

import com.tempvs.image.ImageUploadBean
import grails.validation.Validateable
import groovy.transform.CompileStatic

/**
 * A command for {@link com.tempvs.item.Item}-related {@link com.tempvs.image.Image} upload.
 */
@CompileStatic
class ItemImageUploadCommand implements Validateable {

    Item item
    List<ImageUploadBean> imageUploadBeans
}
