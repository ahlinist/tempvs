package club.tempvs.image

import grails.validation.Validateable
import groovy.transform.CompileStatic

/**
 * A command object for binding {@link ImageUploadBean}.
 */
@CompileStatic
class ImageUploadCommand implements Validateable {
    List<ImageUploadBean> imageUploadBeans
}
