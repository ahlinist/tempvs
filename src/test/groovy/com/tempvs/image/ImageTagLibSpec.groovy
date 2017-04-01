package com.tempvs.image

import com.tempvs.user.User
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(ImageTagLib)
class ImageTagLibSpec extends Specification {
    private static final String AVATAR_URL = '/image/getAvatar/1'

    def user = Mock(User) {
        getProperty('id') >> 1
    }

    def setup() {
    }

    def cleanup() {
    }

    void "UserPic is queried"() {
        given:
        Map properties = [user: user]

        expect:
        tagLib.avatar(properties).contains AVATAR_URL
    }
}
