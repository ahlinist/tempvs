package com.tempvs.image

import groovy.transform.CompileStatic
import org.springframework.web.multipart.MultipartFile
import grails.validation.Validateable

/**
 * A bean for uploading multipartFiles along with their text descriptions.
 */
@CompileStatic
class ImageUploadBean  implements Validateable{

    MultipartFile image
    String imageInfo
}
