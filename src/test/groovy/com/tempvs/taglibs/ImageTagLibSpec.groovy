package com.tempvs.taglibs

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.tests.utils.user.WithUser
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(ImageTagLib)
@Mock([User, UserProfile])
class ImageTagLibSpec extends Specification implements WithUser {
    private static final String AVATAR_URL = '/user/getAvatar'

    def setup() {
    }

    def cleanup() {
    }

    void "If user is not logged in - nothing is returned"() {
        given: "Setting up the user"
        tagLib.springSecurityService = [currentUser: null]

        expect: "Nothing returned"
        !tagLib.userPic()
    }

    void "If user is logged in - userPic is queried"() {
        given: "Setting up the user"
        tagLib.springSecurityService = [currentUser: user]

        expect: "UserPic is queried"
        tagLib.userPic().toString().contains AVATAR_URL
    }
}
