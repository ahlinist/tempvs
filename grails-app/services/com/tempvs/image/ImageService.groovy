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

    void deleteImage(Image image) {
        imageDAO.delete(image?.collection, image?.objectId)
        image?.delete()
    }

    void deleteImages(Collection<Image> images) {
        images.each { deleteImage(it) }
    }

    Image updateImage(ImageUploadBean imageUploadBean, String collection, Image image = null) {
        if (!imageUploadBean.image.empty) {
            if (image) {
                imageDAO.delete(collection, image.objectId)
            } else {
                image = objectFactory.create(Image)
            }

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

    List<Image> uploadImages(List<ImageUploadBean> imageUploadBeans, String collection) {
        List<ImageUploadBean> validBeans = imageUploadBeans.findAll { ImageUploadBean bean -> !bean.image.empty }

        validBeans.collect { ImageUploadBean validBean ->
            updateImage(validBean, collection)
        }
    }
}
