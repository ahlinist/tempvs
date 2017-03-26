package com.tempvs.image

import com.tempvs.image.ImageTagLib
import com.tempvs.user.User
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(ImageTagLib)
class ImageTagLibSpec extends Specification {
    private static final String AVATAR_URL = '/image/getAvatar'

    def springSecurityService = Mock(SpringSecurityService)
    def user = Mock(User)

    def setup() {
        tagLib.springSecurityService = springSecurityService
    }

    def cleanup() {
    }

    void "If user is not logged in - nothing is returned"() {
        when:
        def result = tagLib.userPic()

        then:
        1 * springSecurityService.currentUser
        0 * _

        and:
        !result
    }

    void "If user is logged in - userPic is queried"() {
        when:
        def result = tagLib.userPic()

        then:
        1 * springSecurityService.currentUser >> user
        0 * _

        and:
        result.toString().contains AVATAR_URL
    }
}
