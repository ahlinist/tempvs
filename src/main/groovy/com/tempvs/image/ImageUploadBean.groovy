package com.tempvs.image

import groovy.transform.CompileStatic
import org.springframework.web.multipart.MultipartFile

/**
 * A bean for uploading multipartFiles along with their text descriptions.
 */
@CompileStatic
class ImageUploadBean {

    MultipartFile image
    String imageInfo
}
