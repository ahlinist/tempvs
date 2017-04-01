package com.tempvs.image

import com.tempvs.ajax.AjaxResponseService
import grails.converters.JSON
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.StreamUtils
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ImageController)
class ImageControllerSpec extends Specification {

    private static final String AVATAR = 'avatar'
    private static final String AVATAR_UPDATED_MESSAGE = 'user.profile.update.avatar.success.message'
    private static final String IMAGE_EMPTY = 'upload.image.empty'
    private static final String ID = '1'

    def imageService = Mock(ImageService)
    def ajaxResponseService = Mock(AjaxResponseService)
    def json = Mock(JSON)

    def setup() {
        controller.imageService = imageService
        controller.ajaxResponseService = ajaxResponseService
    }

    def cleanup() {
    }

    void "Check updateAvatar action"() {
        setup: 'Attaching file to request'
        controller.request.addFile(new MockMultipartFile(AVATAR, "1234567" as byte[]))

        when: 'Call updateAvatar() action'
        controller.updateAvatar()

        then: 'JSON response received'
        1 * imageService.updateAvatar(_ as ByteArrayInputStream)
        1 * ajaxResponseService.renderMessage(Boolean.TRUE, AVATAR_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updateAvatar action without file"() {
        setup: 'No file attached to request'
        controller.request.addFile(new MockMultipartFile(AVATAR, StreamUtils.emptyInput().bytes))

        when: 'Call updateAvatar() action'
        controller.updateAvatar()

        then: 'JSON response received'
        1 * ajaxResponseService.renderMessage(Boolean.FALSE, IMAGE_EMPTY) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check getAvatar action"() {
        when: 'Call getAvatar() action'
        params.id = ID
        controller.getAvatar()

        then: 'imageService.getOwnAvatar() invoked'
        1 * imageService.getAvatar(ID)
        0 * _
    }
}
