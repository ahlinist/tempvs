package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.user.User
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(ItemService)
class ItemServiceSpec extends Specification {

    private static final String ID = 'id'
    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def springSecurityService = Mock(SpringSecurityService)
    def itemGroup = Mock(ItemGroup)
    def user = Mock(User)
    def itemStash = Mock(ItemStash)
    def objectFactory = Mock(ObjectFactory)
    def objectDAO = Mock(ObjectDAO)

    def setup() {
        GroovySpy(ItemStash, global: true)

        service.springSecurityService = springSecurityService
        service.objectFactory = objectFactory
        service.objectDAO = objectDAO
    }

    def cleanup() {
    }

    void "Test createGroup() for not being logged in"() {
        when:
        def result = service.createGroup(NAME, DESCRIPTION)

        then:
        1 * springSecurityService.currentUser >> null
        0 * _

        and:
        !result
    }

    void "Test createGroup() with fail"() {
        when:
        def result = service.createGroup(NAME, DESCRIPTION)

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User.class) >> user
        1 * objectFactory.create(ItemGroup.class) >> itemGroup
        1 * itemGroup.asType(ItemGroup.class) >> itemGroup
        1 * itemGroup.setName(NAME)
        1 * itemGroup.setDescription(DESCRIPTION)
        1 * user.getItemStash() >> itemStash
        1 * itemStash.addToItemGroups(itemGroup) >> itemStash
        1 * itemStash.save() >> null
        0 * _

        and:
        result == itemGroup
    }

    void "Test createGroup() with success"() {
        when:
        def result = service.createGroup(NAME, DESCRIPTION)

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.asType(User.class) >> user
        1 * objectFactory.create(ItemGroup.class) >> itemGroup
        1 * itemGroup.asType(ItemGroup.class) >> itemGroup
        1 * itemGroup.setName(NAME)
        1 * itemGroup.setDescription(DESCRIPTION)
        1 * user.getItemStash() >> itemStash
        1 * itemStash.addToItemGroups(itemGroup) >> itemStash
        1 * itemStash.save() >> itemStash
        0 * _

        and:
        result == itemGroup
    }

    void "Test getStash()"() {
        when:
        def result = service.getStash(ID)

        then:
        1 * objectDAO.get(ItemStash.class, ID) >> itemStash
        1 * itemStash.asType(ItemStash.class) >> itemStash
        0 * _

        and:
        result == itemStash
    }

    void "Test getGroup()"() {
        when:
        def result = service.getGroup(ID)

        then:
        1 * objectDAO.get(ItemGroup.class, ID) >> itemGroup
        1 * itemGroup.asType(ItemGroup.class) >> itemGroup
        0 * _

        and:
        result == itemGroup
    }
}
