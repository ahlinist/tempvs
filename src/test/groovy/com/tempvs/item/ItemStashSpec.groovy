package com.tempvs.item

import com.tempvs.user.ClubProfile
import com.tempvs.user.User
import com.tempvs.user.UserProfile
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(ItemStash)
@Mock([User, UserProfile, ClubProfile])
class ItemStashSpec extends Specification {

    def user = Mock(User)

    def setup() {
    }

    def cleanup() {
    }

    void "Test stash creation without user"() {
        expect:
        !new ItemStash().validate()
    }

    void "Test stash saving with user"() {
        given:
        ItemStash itemStash = new ItemStash()
        itemStash.user = user

        expect:
        itemStash.validate()
    }
}
