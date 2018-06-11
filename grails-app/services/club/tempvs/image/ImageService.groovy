package club.tempvs.image

import club.tempvs.rest.RestCaller
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import org.springframework.http.HttpStatus
import org.springframework.web.multipart.MultipartFile

/**
 * A service that manages {@link ImageBean}-related operations.
 */
@GrailsCompileStatic
class ImageService {

    private static final String IMAGE_SERVICE_URL = System.getenv 'IMAGE_SERVICE_URL'
    private static final String IMAGE_SECURITY_TOKEN = System.getenv 'IMAGE_SECURITY_TOKEN'

    ImageDAO imageDAO
    RestCaller restCaller

    Image getImage(Long id) {
        Image.get id
    }

    Boolean deleteImage(Image image) {
        deleteImages([image])
    }

    Boolean deleteImages(List<Image> images) {
        String url = IMAGE_SERVICE_URL + '/api/delete'
        Map<String, String> headers = [token: IMAGE_SECURITY_TOKEN.encodeAsMD5() as String]
        JSON payload = [images: images] as JSON

        restCaller.doDelete(url, payload, headers)?.statusCode == HttpStatus.OK.value()
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
