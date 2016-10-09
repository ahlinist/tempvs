package com.tempvs.domain.image

import com.tempvs.domain.user.User
import com.tempvs.tests.unit.UnitTestUtils
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Avatar)
@Mock(User)
class AvatarSpec extends Specification {
    private static final String EMAIL = 'test@mail.com'

    def setup() {
    }

    def cleanup() {
    }

    void "avatar must belong to userProfile" () {
        when:"creating an empty avatar"
            new Avatar().save(flush:true)

        then:"avatar is not saved"
            Avatar.list().size() == 0
    }

    void "created user has avatar"() {
        when:"creating a user"
            UnitTestUtils.createUser(EMAIL)

        then:"user has avatar in it's profile"
            User.findByEmail(EMAIL).userProfile.avatar
    }
}
