package com.tempvs.domain.image

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.tests.utils.user.WithUser
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Avatar)
@Mock([User, UserProfile])
class AvatarSpec extends Specification implements WithUser {

    def setup() {
    }

    def cleanup() {
    }

    void "Avatar must belong to userProfile" () {
        expect:"Isolated Avatar is not saved"
        !new Avatar().save(flush:true)
    }

    void "Created user has avatar"() {
        expect: 'Created user has avatar'
        user.userProfile.avatar
    }
}
