package com.tempvs.image

import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ImageService)
class ImageServiceSpec extends Specification {

    private static final String COLLECTION = 'collection'
    private static final String ID = 'id'
    private static final Long ONE_LONG = 1L
    private static final String AVATAR = 'avatar'

    def imageBeanDAO = Mock(ImageBeanDAO)
    def imageBean = Mock(ImageBean)
    def multipartFile = new MockMultipartFile(AVATAR, "1234567" as byte[])

    def setup() {
        service.imageBeanDAO = imageBeanDAO
    }

    def cleanup() {
    }

    void "Test createImage()"() {
        given:
        Map metaData = [
                userId: ONE_LONG,
                properties: [itemGroupId: ONE_LONG]
        ]

        when:
        def result = service.createImageBean(multipartFile, COLLECTION, metaData)

        then:
        1 * imageBeanDAO.create(_ as ByteArrayInputStream, COLLECTION) >> imageBean
        1 * imageBeanDAO.save(imageBean, metaData) >> imageBean
        0 * _

        and:
        result == imageBean
    }

    void "Test getImage()"() {
        when:
        def result = service.getImageBean(COLLECTION, ID)

        then:
        1 * imageBeanDAO.get(COLLECTION, ID) >> imageBean
        0 * _

        and:
        result == imageBean
    }

    void "Test deleteImage()"() {
        when:
        def result = service.deleteImageBeans(COLLECTION, [ID, ID])

        then:
        1 * imageBeanDAO.delete(COLLECTION, [ID, ID]) >> Boolean.TRUE
        0 * _

        and:
        result == Boolean.TRUE

        when:
        result = service.deleteImageBeans(COLLECTION, null)

        then:
        0 * _

        and:
        result == Boolean.TRUE
    }

    void "Test replaceImage()"() {
        given:
        Map metaData = [
                userId: ONE_LONG,
                properties: [itemGroupId: ONE_LONG]
        ]

        when:
        def result = service.replaceImageBeans(multipartFile, COLLECTION, metaData, ID)

        then:
        1 * imageBeanDAO.delete(COLLECTION, [ID]) >> Boolean.TRUE
        1 * imageBeanDAO.create(_ as ByteArrayInputStream, COLLECTION) >> imageBean
        1 * imageBeanDAO.save(imageBean, metaData) >> imageBean
        0 * _

        and:
        result == imageBean
    }
}
