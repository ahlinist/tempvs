package com.tempvs.image

import com.tempvs.domain.ObjectFactory
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.springframework.web.multipart.MultipartFile

/**
 * A service that manages {@link com.tempvs.image.ImageBean}-related operations.
 */
@Transactional
@GrailsCompileStatic
class ImageService {

    ImageDAO imageDAO
    ObjectFactory objectFactory

    byte[] getImageBytes(String collection, String objectId) {
        imageDAO.get(collection, objectId)?.bytes
    }

    Boolean deleteImage(String collection, String objectId) {
        imageDAO.delete(collection, objectId)
    }

    Boolean deleteImage(Image image) {
        deleteImage(image.collection, image.objectId)
    }

    Boolean deleteImages(Collection<Image> images) {
        images.findAll().each { Image image ->
            deleteImage(image.collection, image.objectId)
        }
    }

    String createImage(MultipartFile multipartFile, String collection) {
        if (!multipartFile.empty) {
            InputStream inputStream = multipartFile.inputStream

            try {
                imageDAO.create(inputStream, collection).id
            } finally {
                inputStream?.close()
            }
        }
    }

    String replaceImage(MultipartFile multipartFile, Image image = null) {
        deleteImage(image.collection, image.objectId)
        createImage(multipartFile, image.collection)
    }
}
