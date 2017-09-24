package com.tempvs.image

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ImageService)
class ImageServiceSpec extends Specification {

    private static final String ID = 'id'
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String COLLECTION = 'collection'
    private static final List<Byte> BYTE_LIST = "test data".bytes

    def image = Mock Image
    def imageDAO = Mock ImageDAO
    def objectDAO = Mock ObjectDAO
    def imageBean = Mock ImageBean
    def objectFactory = Mock ObjectFactory
    def imageUploadBean = Mock ImageUploadBean
    def multipartFile = new MockMultipartFile('1234567', "1234567" as byte[])


    def setup() {
        service.imageDAO = imageDAO
        service.objectDAO = objectDAO
        service.objectFactory = objectFactory
    }

    def cleanup() {
    }

    void "Test getImage()"() {
        when:
        def result = service.getImage(ID)

        then:
        1 * objectDAO.get(Image, ID) >> image
        0 * _

        and:
        result == image
    }

    void "Test getImageBytes()"() {
        when:
        def result = service.getImageBytes(COLLECTION, ID)

        then:
        1 * imageDAO.get(COLLECTION, ID) >> imageBean
        1 * imageBean.bytes >> BYTE_LIST
        0 * _

        and:
        result == BYTE_LIST
    }

    void "Test deleteImage()"() {
        when:
        service.deleteImage(image)

        then:
        1 * image.collection >> COLLECTION
        1 * image.objectId >> ID
        1 * imageDAO.delete(COLLECTION, ID) >> Boolean.TRUE
        0 * _
    }

    void "Test deleteImages()"() {
        given:
        List<Image> images = [image, image]

        when:
        service.deleteImages(images)

        then:
        2 * image.collection >> COLLECTION
        2 * image.objectId >> ID
        2 * imageDAO.delete(COLLECTION, ID)
        0 * _
    }

    void "Test updateImage()"() {
        when:
        def result = service.updateImage(imageUploadBean, COLLECTION, image)

        then:
        2 * imageUploadBean.image >> multipartFile
        1 * imageUploadBean.imageInfo >> IMAGE_INFO
        1 * image.objectId >> ID
        1 * imageDAO.delete(COLLECTION, ID)
        1 * imageDAO.create(_ as ByteArrayInputStream, COLLECTION) >> imageBean
        1 * imageBean.id >> ID
        1 * image.setObjectId(ID)
        1 * image.setCollection(COLLECTION)
        1 * image.setImageInfo(IMAGE_INFO)
        0 * _

        and:
        result == image
    }

    void "Test updateImages()"() {
        given:
        List<ImageUploadBean> imageUploadBeans = [imageUploadBean]

        when:
        def result = service.uploadImages(imageUploadBeans, COLLECTION)

        then:
        1 * imageUploadBean.image >> multipartFile
        2 * imageUploadBean.image >> multipartFile
        1 * objectFactory.create(Image) >> image
        1 * imageDAO.create(_ as ByteArrayInputStream, COLLECTION) >> imageBean
        1 * imageBean.id >> ID
        1 * image.setObjectId(ID)
        1 * imageUploadBean.imageInfo >> IMAGE_INFO
        1 * image.setCollection(COLLECTION)
        1 * image.setImageInfo(IMAGE_INFO)
        0 * _

        and:
        result == [image]
    }
}
