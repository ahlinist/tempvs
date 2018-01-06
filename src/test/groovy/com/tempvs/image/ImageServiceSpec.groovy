package com.tempvs.image

import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

class ImageServiceSpec extends Specification implements ServiceUnitTest<ImageService>, DomainUnitTest<Image> {

    private static final String ID = 'id'
    private static final Long LONG_ID = 1L
    private static final String OBJECT_ID = 'objectId'
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String COLLECTION = 'collection'
    private static final List<Byte> BYTE_LIST = "test data".bytes

    def image = Mock Image
    def imageDAO = Mock ImageDAO
    def imageBean = Mock ImageBean
    def imageUploadBean = Mock ImageUploadBean
    def multipartFile = new MockMultipartFile('1234567', "1234567" as byte[])


    def setup() {
        GroovyMock(Image, global: true)

        service.imageDAO = imageDAO
    }

    def cleanup() {
    }

    void "Test getImage()"() {
        when:
        def result = service.getImage(LONG_ID)

        then:
        1 * Image.get(LONG_ID) >> image
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
        1 * image.objectId >> OBJECT_ID
        1 * imageDAO.delete(COLLECTION, OBJECT_ID) >> Boolean.TRUE
        0 * _
    }

    void "Test deleteImages()"() {
        given:
        List<Image> images = [image, image]

        when:
        service.deleteImages(images)

        then:
        2 * image.collection >> COLLECTION
        2 * image.objectId >> OBJECT_ID
        2 * imageDAO.delete(COLLECTION, OBJECT_ID)
        0 * _
    }

    void "Test uploadImage() with image"() {
        when:
        def result = service.uploadImage(imageUploadBean, COLLECTION, image)

        then:
        1 * imageUploadBean.image >> multipartFile
        1 * imageUploadBean.imageInfo >> IMAGE_INFO
        1 * image.objectId >> OBJECT_ID
        1 * imageDAO.delete(COLLECTION, OBJECT_ID)
        1 * imageDAO.create(_ as ByteArrayInputStream, COLLECTION) >> imageBean
        1 * imageBean.id >> ID
        1 * image.setObjectId(ID)
        1 * image.setCollection(COLLECTION)
        1 * image.setImageInfo(IMAGE_INFO)
        0 * _

        and:
        result == image
    }

    void "Test uploadImage()"() {
        when:
        def result = service.uploadImage(imageUploadBean, COLLECTION)

        then:
        1 * imageUploadBean.image >> multipartFile
        1 * new Image() >> image
        1 * imageDAO.create(_ as ByteArrayInputStream, COLLECTION) >> imageBean
        1 * imageBean.id >> ID
        1 * imageUploadBean.imageInfo >> IMAGE_INFO
        1 * image.setObjectId(ID)
        1 * image.setCollection(COLLECTION)
        1 * image.setImageInfo(IMAGE_INFO)
        0 * _

        and:
        result == image
    }

    void "Test uploadImages()"() {
        when:
        def result = service.uploadImages([imageUploadBean], COLLECTION)

        then:
        1 * imageUploadBean.image >> multipartFile
        1 * new Image() >> image
        1 * imageDAO.create(_ as ByteArrayInputStream, COLLECTION) >> imageBean
        1 * imageBean.id >> ID
        1 * imageUploadBean.imageInfo >> IMAGE_INFO
        1 * image.setObjectId(ID)
        1 * image.setCollection(COLLECTION)
        1 * image.setImageInfo(IMAGE_INFO)
        0 * _

        and:
        result == [image]
    }
}
