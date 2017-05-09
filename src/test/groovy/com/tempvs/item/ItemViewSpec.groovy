package com.tempvs.item

import com.tempvs.user.User
import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import spock.lang.Specification

/**
 * Unit-test suite for testing {@link com.tempvs.item.Item}-related views.
 */
@TestMixin(GroovyPageUnitTestMixin)
class ItemViewSpec extends Specification {

    private static final String ITEM_STASH_TITLE = 'item.stash.title'
    private static final String CREATE_GROUP = 'item.createGroup.title'
    private static final String ITEM_GROUP_TITLE = 'item.group.title'
    private static final String ITEM_GROUPS = 'itemGroups'
    private static final String STASH_NOT_FOUND = 'item.stash.notFound.message'
    private static final String GROUP_NOT_FOUND = 'item.group.notFound.message'
    private static final String ID = 'id'
    private static final String ONE = '1'
    private static final String NAME = 'name'
    private static final String DESCRIPTION = 'description'

    def user
    def itemStash
    def itemGroup

    def setup() {
        user = Mock(User)
        itemStash = Mock(ItemStash)
        itemGroup = Mock(ItemGroup)
    }

    def cleanup() {

    }

    void "Test /item/stash view without id"() {
        given:
        String title = "<title>Tempvs - ${ITEM_STASH_TITLE}</title>"
        String notFoundMessage = STASH_NOT_FOUND
        Map model = [:]

        when:
        String result = render view: '/item/stash', model: model

        then:
        result.contains title
        result.contains notFoundMessage
        0 * _
    }

    void "Test /item/stash view with id"() {
        given:
        String title = "<title>Tempvs - ${ITEM_STASH_TITLE}</title>"
        String createButton = '<a href="/item/createGroup" class="btn btn-default">'
        String itemGroupLink = '<a href="/item/group/1" class="list-group-item">'
        Map model = [itemStash: itemStash]

        when:
        String result = render view: '/item/stash', model: model

        then:
        1 * itemStash.getProperty(ITEM_GROUPS) >> [itemGroup]
        1 * itemGroup.getProperty(NAME) >> NAME
        1 * itemGroup.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * itemGroup.getProperty(ID) >> ONE
        0 * _

        and:
        result.contains title
        result.contains createButton
        result.contains itemGroupLink
    }

    void "Test /item/createGroup view"() {
        given:
        String title = "<title>Tempvs - ${CREATE_GROUP}</title>"
        String createGroupForm = '<tempvs:ajaxForm action="createGroup">'

        when:
        String result = render view: '/item/createGroup'

        then:
        result.contains title
        result.contains createGroupForm
    }

    void "Test /item/group view without id"() {
        given:
        String title = "<title>Tempvs - ${ITEM_GROUP_TITLE}</title>"
        String notFoundMessage = GROUP_NOT_FOUND
        Map model = [:]

        when:
        String result = render view: '/item/group', model: model

        then:
        result.contains title
        result.contains notFoundMessage
        0 * _
    }

    void "Test /item/group view with id"() {
        given:
        String title = "<title>Tempvs - ${ITEM_GROUP_TITLE}</title>"
        String createItemButton = '<a href="/item/createItem" class="btn btn-default disabled">'
        Map model = [itemGroup: itemGroup]

        when:
        String result = render view: '/item/group', model: model

        then:
        1 * itemGroup.getProperty(NAME) >> NAME
        1 * itemGroup.getProperty(DESCRIPTION) >> DESCRIPTION
        0 * _

        and:
        result.contains title
        result.contains createItemButton
    }
}
