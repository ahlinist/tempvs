package com.tempvs.image

import grails.transaction.Transactional
import groovy.transform.CompileStatic

/**
 * A service that manages {@link com.tempvs.image.Image}-related operations.
 */
@Transactional
@CompileStatic
class ImageService {

    ImageDAO imageDAO

    Image createImage(InputStream inputStream, String collection, Map metaData) {
        Image image = imageDAO.create(inputStream, collection)
        imageDAO.save(image, metaData)
    }

    Image getImage(String collection, String id) {
        imageDAO.get(collection, id)
    }

    Boolean deleteImage(String collection, String id) {
        if (id) {
            imageDAO.delete(collection, id)
        } else {
            Boolean.TRUE
        }
    }
}
