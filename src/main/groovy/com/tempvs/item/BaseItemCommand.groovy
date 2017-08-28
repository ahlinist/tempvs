package com.tempvs.item

import com.tempvs.image.ImageUploadBean
import grails.validation.Validateable
import groovy.transform.CompileStatic

/**
 * A super class for {@link com.tempvs.item.ItemCommand} and
 * {@link com.tempvs.item.ItemImageUploadCommand}
 */
@CompileStatic
abstract class BaseItemCommand implements Validateable {
    List<ImageUploadBean> imageUploadBeans
}
