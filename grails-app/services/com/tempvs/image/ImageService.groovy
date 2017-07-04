package com.tempvs.image

import grails.transaction.Transactional
import groovy.transform.CompileStatic
import org.springframework.web.multipart.MultipartFile

/**
 * A service that manages {@link com.tempvs.image.ImageBean}-related operations.
 */
@Transactional
@CompileStatic
class ImageService {

    ImageBeanDAO imageBeanDAO

    ImageBean createImageBean(MultipartFile multipartFile, String collection, Map metaData) {
        if (!multipartFile.empty) {
            InputStream inputStream = multipartFile.inputStream

            try {
                ImageBean imageBean = imageBeanDAO.create(inputStream, collection)
                imageBeanDAO.save(imageBean, metaData)
            } finally {
                inputStream?.close()
            }
        }
    }

    ImageBean getImageBean(String collection, String objectId) {
        imageBeanDAO.get(collection, objectId)
    }

    Boolean deleteImageBeans(String collection, Collection<String> objectIds) {
        if (objectIds?.findAll()) {
            imageBeanDAO.delete(collection, objectIds)
        } else {
            Boolean.TRUE
        }
    }

    ImageBean replaceImageBeans(MultipartFile multipartFile, String collection, Map metaData, String objectId = null) {
        deleteImageBeans(collection, [objectId])
        createImageBean(multipartFile, collection, metaData)
    }
}
