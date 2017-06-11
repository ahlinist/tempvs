package com.tempvs.image

import grails.transaction.Transactional
import groovy.transform.CompileStatic
import org.springframework.web.multipart.MultipartFile

/**
 * A service that manages {@link com.tempvs.image.Image}-related operations.
 */
@Transactional
@CompileStatic
class ImageService {

    ImageDAO imageDAO

    Image createImage(MultipartFile multipartFile, String collection, Map metaData) {
        if (!multipartFile.empty) {
            InputStream inputStream = multipartFile.inputStream

            try {
                Image image = imageDAO.create(inputStream, collection)
                imageDAO.save(image, metaData)
            } finally {
                inputStream?.close()
            }
        }
    }

    Image getImage(String collection, String objectId) {
        imageDAO.get(collection, objectId)
    }

    Boolean deleteImages(String collection, Collection<String> objectIds) {
        if (objectIds?.findAll()) {
            imageDAO.delete(collection, objectIds)
        } else {
            Boolean.TRUE
        }
    }

    Image replaceImage(String collection, String objectId, MultipartFile multipartFile, Map metaData) {
        deleteImages(collection, [objectId])
        createImage(multipartFile, collection, metaData)
    }
}
