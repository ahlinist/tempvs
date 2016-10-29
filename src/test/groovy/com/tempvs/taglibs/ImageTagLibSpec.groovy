package com.tempvs.taglibs

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.tests.unit.UnitTestUtils
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(ImageTagLib)
@Mock([User, UserProfile])
class ImageTagLibSpec extends Specification {
    def setup() {
        tagLib.springSecurityService = [currentUser: UnitTestUtils.createUser()]
    }

    def cleanup() {
    }

    void "if user has no avatar - the default one is shown"() {
        expect: "default avatar is shown"
        tagLib.userPic().toString().contains '/assets/defaultAvatar.jpg'
    }

    void "if user has avatar - it is queried"() {
        given: "assigning avatar to user"
        tagLib.springSecurityService.currentUser.userProfile.avatar.pathToFile = 'some_file_path'

        expect: "avatar is queried"
        tagLib.userPic().toString().contains '/user/getAvatar'
    }
}
