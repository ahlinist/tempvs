package com.tempvs.image

import com.tempvs.domain.ObjectFactory
import grails.transaction.Transactional
import groovy.transform.CompileStatic

@Transactional
@CompileStatic
class ImageService {

    ImageDAO imageDAO
    ObjectFactory objectFactory

    Image createImage(InputStream inputStream, String collection, Map metaData) {
        Image image = imageDAO.create(inputStream, collection)
        imageDAO.save(image, metaData)
    }

    Image getImage(String collection, String id) {
        imageDAO.get(collection, id)
    }
}
