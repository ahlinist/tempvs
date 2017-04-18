package com.tempvs.image

import com.tempvs.user.User
import com.tempvs.user.UserProfile
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(ImageTagLib)
class ImageTagLibSpec extends Specification {

    private static final String AVATAR_URL = '/image/getAvatar/1'

    def userProfile = Mock(UserProfile)
    def user = Mock(User)

    def setup() {
    }

    def cleanup() {
    }

    void "UserPic is queried"() {
        given:
        Map properties = [profile: userProfile]

        when:
        String result = tagLib.avatar(properties)

        then:
        1 * userProfile.user >> user
        1 * userProfile.id >> 1
        //1 * userProfile.class >> UserProfile.class
        1 * user.id >> 1

        and:
        result.contains AVATAR_URL
    }
}
