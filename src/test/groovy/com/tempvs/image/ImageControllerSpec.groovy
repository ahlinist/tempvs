package com.tempvs.image

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.user.ProfileHolder
import com.tempvs.user.User
import com.tempvs.user.UserProfile
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
    private static final String AVATAR_UPDATED_MESSAGE = 'userProfile.update.avatar.success.message'
    private static final String IMAGE_EMPTY = 'upload.image.empty'

    def imageService = Mock(ImageService)
    def ajaxResponseService = Mock(AjaxResponseService)
    def user = Mock(User)
    def userProfile = Mock(UserProfile)
    def json = Mock(JSON)
    def profileHolder = Mock(ProfileHolder)

    def setup() {
        controller.imageService = imageService
        controller.ajaxResponseService = ajaxResponseService
        controller.profileHolder = profileHolder
    }

    def cleanup() {
    }

    void "Check updateAvatar action"() {
        setup: 'Attaching file to request'
        controller.request.addFile(new MockMultipartFile(AVATAR, "1234567" as byte[]))

        when: 'Call updateAvatar() action'
        controller.updateAvatar()

        then: 'JSON response received'
        1 * profileHolder.profile >> userProfile
        1 * userProfile.user >> user
        1 * user.id >> 1
        //1 * userProfile.class >> UserProfile.class
        1 * userProfile.id >> 1
        1 * imageService.updateAvatar(_ as ByteArrayInputStream, _ as String) >> Boolean.TRUE
        1 * ajaxResponseService.renderMessage(Boolean.TRUE, AVATAR_UPDATED_MESSAGE) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check updateAvatar action without file"() {
        setup: 'No file attached to request'
        controller.request.addFile(new MockMultipartFile(AVATAR, StreamUtils.emptyInput().bytes))

        when:
        controller.updateAvatar()

        then:
        1 * ajaxResponseService.renderMessage(Boolean.FALSE, IMAGE_EMPTY) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Check getAvatar action"() {
        when:
        params.id = 1
        controller.getAvatar()

        then:
        1 * imageService.getAvatar('1')
        0 * _
    }
}
