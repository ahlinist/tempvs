package com.tempvs.image

import com.tempvs.domain.ObjectFactory
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional

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

    Boolean deleteImage(Image image) {
        imageDAO.delete(image?.collection, image?.objectId)
    }

    Boolean deleteImages(Collection<Image> images) {
        images.findAll().each { Image image ->
            deleteImage(image)
        }
    }

    Image createImage(ImageUploadBean imageUploadBean, String collection) {
        Image image = objectFactory.create(Image)

        if (!imageUploadBean.image.empty) {
            InputStream inputStream = imageUploadBean.image.inputStream

            try {
                image.objectId = imageDAO.create(inputStream, collection).id
            } finally {
                inputStream?.close()
            }

            image.collection = collection
            image.imageInfo = imageUploadBean.imageInfo
        }

        image
    }

    Image extractImage(ImageUploadBean imageUploadBean, String collection, Image image = null) {
        if (!imageUploadBean.image.empty) {
            if (image) {
                return replaceImage(imageUploadBean, image)
            } else {
                return createImage(imageUploadBean, collection)
            }
        }
    }

    Set<Image> extractImages(List<ImageUploadBean> imageUploadBeans, String collection) {
        List<ImageUploadBean> validBeans = imageUploadBeans.findAll { ImageUploadBean bean -> !bean.image.empty }

        validBeans.collect { ImageUploadBean validBean ->
            createImage(validBean, collection)
        } as Set
    }

    Image replaceImage(ImageUploadBean imageUploadBean, Image image) {
        deleteImage(image)
        createImage(imageUploadBean, image.collection)
    }
}
