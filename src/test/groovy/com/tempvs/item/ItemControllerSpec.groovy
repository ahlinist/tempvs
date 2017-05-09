package com.tempvs.item

import com.tempvs.ajax.AjaxResponseService
import com.tempvs.user.User
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ItemController)
class ItemControllerSpec extends Specification {

    private static final String ONE = '1'
    private static final String ID = 'id'
    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_STASH = 'itemStash'
    private static final String ITEM_GROUP_URI = '/item/group'

    def user = Mock(User)
    def springSecurityService = Mock(SpringSecurityService)
    def itemService = Mock(ItemService)
    def createItemGroupCommand = Mock(CreateItemGroupCommand)
    def ajaxResponseService = Mock(AjaxResponseService)
    def itemGroup = Mock(ItemGroup)
    def itemStash = Mock(ItemStash)
    def json = Mock(JSON)

    def setup() {
        controller.springSecurityService = springSecurityService
        controller.itemService = itemService
        controller.ajaxResponseService = ajaxResponseService
    }

    def cleanup() {
    }

    void "Test stash() without id"() {
        when:
        def result = controller.stash()

        then:
        1 * springSecurityService.currentUser >> user
        1 * user.getProperty(ITEM_STASH) >> itemStash
        0 * _

        and:
        result == [itemStash: itemStash]
    }

    void "Test stash() with id for existing stash"() {
        when:
        params.id = ONE
        def result = controller.stash()

        then:
        1 * itemService.getStash(ONE) >> itemStash
        0 * _

        and:
        result == [itemStash: itemStash]
    }

    void "Test stash() with id for non-existing stash"() {
        when:
        params.id = ONE
        def result = controller.stash()

        then:
        1 * itemService.getStash(ONE) >> null
        0 * _

        and:
        result == [itemStash: null]
    }

    void "Test createGroup() rendering"() {
        when:
        controller.createGroup()

        then:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test group creation failing"() {
        given:
        params.isAjaxRequest = Boolean.TRUE

        when: 'ItemGroup has not been created'
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(createItemGroupCommand) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _

        when:
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.TRUE
        1 * createItemGroupCommand.getProperty(NAME) >> NAME
        1 * createItemGroupCommand.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * itemService.createGroup(NAME, DESCRIPTION) >> itemGroup
        1 * itemGroup.validate() >> Boolean.FALSE
        1 * ajaxResponseService.composeJsonResponse(itemGroup) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test group creation"() {
        given:
        params.isAjaxRequest = Boolean.TRUE

        when:
        controller.createGroup(createItemGroupCommand)

        then:
        1 * createItemGroupCommand.validate() >> Boolean.TRUE
        1 * createItemGroupCommand.getProperty(NAME) >> NAME
        1 * createItemGroupCommand.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * itemService.createGroup(NAME, DESCRIPTION) >> itemGroup
        1 * itemGroup.validate() >> Boolean.TRUE
        1 * itemGroup.getProperty(ID) >> ONE
        0 * _

        and:
        response.json.redirect == "${ITEM_GROUP_URI}/${ONE}"
    }

    void "Test group() without id"() {
        when:
        controller.group()

        then:
        0 * _

        and:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test group() for non-existing id"() {
        when:
        params.id = ONE
        def result = controller.group()

        then:
        1 * itemService.getGroup(ONE) >> null
        0 * _

        and:
        controller.modelAndView == null
        !response.redirectedUrl
    }

    void "Test group() for existing id"() {
        when:
        params.id = ONE
        def result = controller.group()

        then:
        1 * itemService.getGroup(ONE) >> itemGroup
        0 * _

        and:
        result == [itemGroup: itemGroup]
    }
}
