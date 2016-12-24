package com.tempvs.taglibs

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.tests.unit.UnitTestUtils
import com.tempvs.tests.unit.user.WithUser
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(ImageTagLib)
@Mock([User, UserProfile])
class ImageTagLibSpec extends Specification implements WithUser {
    private static final String FILE_PATH = 'some_file_path'
    private static final String AVATAR_URL = '/user/getAvatar'
    private static final String DEFAULT_AVATAR_URL = '/assets/defaultAvatar.jpg'

    def setup() {
        tagLib.springSecurityService = [currentUser: user]
    }

    def cleanup() {
    }

    void "If user has no avatar - the default one is shown"() {
        expect: "Default avatar is shown"
        tagLib.userPic().toString().contains DEFAULT_AVATAR_URL
    }

    void "If user has avatar - it is queried"() {
        given: "Assigning avatar to user"
        user.userProfile.avatar.pathToFile = FILE_PATH

        expect: "Avatar is queried"
        tagLib.userPic().toString().contains AVATAR_URL
    }
}
