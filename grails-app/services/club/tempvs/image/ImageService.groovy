package club.tempvs.image

import grails.compiler.GrailsCompileStatic
import org.springframework.web.multipart.MultipartFile

/**
 * A service that manages {@link ImageBean}-related operations.
 */
@GrailsCompileStatic
class ImageService {

    ImageDAO imageDAO

    Image getImage(Long id) {
        Image.get id
    }

    byte[] getImageBytes(String collection, String objectId) {
        imageDAO.get(collection, objectId)?.bytes
    }

    void deleteImage(Image image) {
        imageDAO.delete(image?.collection, image?.objectId)
    }

    void deleteImages(List<Image> images) {
        images.each { deleteImage(it) }
    }

    Image uploadImage(ImageUploadBean imageUploadBean, String collection, Image image = null) {
        MultipartFile multipartFile = imageUploadBean.image

        if (!multipartFile.empty) {
            if (image) {
                imageDAO.delete(collection, image.objectId)
            } else {
                image = new Image()
            }

            InputStream inputStream = multipartFile.inputStream

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
        imageUploadBeans?.findResults { ImageUploadBean imageUploadBean ->
            uploadImage(imageUploadBean, collection)
        } as List<Image>
    }
}
