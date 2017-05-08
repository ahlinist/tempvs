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
    private static final String USER = 'user'
    private static final String ID = 'id'
    private static final String CLASS = 'class'

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
        1 * userProfile.getProperty(USER) >> user
        1 * user.getProperty(ID) >> 1
        1 * userProfile.getProperty(ID) >> 1
        1 * userProfile.getProperty(CLASS) >> UserProfile.class
        0 * _

        and:
        result.contains AVATAR_URL
    }
}
