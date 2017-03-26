package com.tempvs.image

import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(ImageTagLib)
class ImageTagLibSpec extends Specification {
    private static final String AVATAR_URL = '/image/getAvatar'

    def setup() {
    }

    def cleanup() {
    }

    void "UserPic is queried"() {
        expect:
        tagLib.loggedInUserPic().contains AVATAR_URL
    }
}
