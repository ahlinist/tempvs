package com.tempvs.item

import com.tempvs.user.User
import com.tempvs.user.UserProfile
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
    private static final String CREATE_ITEM = 'item.createItem.title'
    private static final String ITEM_GROUP_TITLE = 'item.group.title'
    private static final String ITEM_TITLE = 'item.show.title'
    private static final String ITEM_GROUPS = 'itemGroups'
    private static final String ITEM_GROUP = 'itemGroup'
    private static final String ITEM_STASH = 'itemStash'
    private static final String USER = 'user'
    private static final String USER_PROFILE = 'userProfile'
    private static final String STASH_NOT_FOUND = 'item.stash.notFound.message'
    private static final String GROUP_NOT_FOUND = 'item.group.notFound.message'
    private static final String ITEM_NOT_FOUND = 'item.item.notFound.message'
    private static final String ID = 'id'
    private static final String ONE = '1'
    private static final String NAME = 'name'
    private static final String ITEMS = 'items'
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_IMAGE_ID = 'itemImageId'
    private static final String SOURCE_IMAGE_ID = 'sourceImageId'

    def user
    def item
    def itemStash
    def itemGroup
    def userProfile = Mock(UserProfile)

    def setup() {
        user = Mock(User)
        itemStash = Mock(ItemStash)
        itemGroup = Mock(ItemGroup)
        item = Mock(Item)
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
        String createButton = '<button type="button" class="btn btn-default" data-toggle="modal" data-target="#createGroup">'
        String itemGroupLink = '<a href="/item/group/1" class="list-group-item">'
        Map model = [
                itemStash: itemStash,
                userProfile: userProfile,
                ownStash: Boolean.TRUE,
        ]

        when:
        String result = render view: '/item/stash', model: model

        then:
        1 * itemStash.getProperty(ITEM_GROUPS) >> [itemGroup]
        1 * itemStash.getProperty(ID) >> ID
        1 * itemGroup.getProperty(NAME) >> NAME
        1 * itemGroup.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * itemGroup.getProperty(ID) >> ONE
        1 * userProfile.toString()
        0 * _

        and:
        result.contains title
        result.contains createButton
        result.contains itemGroupLink
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
        String createItemButton = '<button type="button" class="btn btn-default" data-toggle="modal" data-target="#itemForm">'
        List items = [item]
        Map model = [
                itemGroup: itemGroup,
                itemStash: itemStash,
                userProfile: userProfile,
                ownGroup: Boolean.TRUE,
        ]

        when:
        String result = render view: '/item/group', model: model

        then:
        1 * itemStash.getProperty(USER) >> user
        1 * itemStash.getProperty(ID) >> ID
        3 * itemGroup.getProperty(NAME) >> NAME
        1 * itemGroup.getProperty(DESCRIPTION) >> DESCRIPTION
        2 * itemGroup.getProperty(ID) >> ID
        1 * itemGroup.getProperty(ITEMS) >> items
        1 * itemGroup.getProperty(ITEM_STASH) >> itemStash
        1 * item.getProperty(ID) >> ID
        1 * item.getProperty(NAME) >> NAME
        1 * user.getProperty(USER_PROFILE) >> userProfile
        1 * userProfile.toString()
        0 * _

        and:
        result.contains title
        result.contains createItemButton
    }

    void "Test /item/show without id"() {
        given:
        String title = "<title>Tempvs - ${ITEM_TITLE}</title>"
        String notFoundMessage = ITEM_NOT_FOUND
        Map model = [:]

        when:
        String result = render view: '/item/show', model: model

        then:
        result.contains title
        result.contains notFoundMessage
    }

    void "Test /item/show with id"() {
        given:
        String title = "<title>Tempvs - ${NAME}</title>"
        String itemImage = "<tempvs:image objectId=\"${ITEM_IMAGE_ID}\" collection=\"item\"/>"
        String sourceImage = "<tempvs:image objectId=\"${SOURCE_IMAGE_ID}\" collection=\"source\"/>"
        Map model = [
                item: item,
                itemGroup: itemGroup,
                itemStash: itemStash,
                userProfile: userProfile,
                ownItem: Boolean.TRUE,
        ]

        when:
        String result = render view: '/item/show', model: model

        then:
        2 * item.getProperty(ID) >> ID
        5 * item.getProperty(NAME) >> NAME
        2 * item.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * item.getProperty(ITEM_IMAGE_ID) >> ITEM_IMAGE_ID
        1 * item.getProperty(SOURCE_IMAGE_ID) >> SOURCE_IMAGE_ID
        1 * item.getProperty(ITEM_GROUP) >> itemGroup
        1 * itemGroup.getProperty(ID) >> ID
        1 * itemGroup.getProperty(NAME) >> NAME
        1 * itemGroup.getProperty(ITEM_STASH) >> itemStash
        1 * itemStash.getProperty(USER) >> user
        1 * itemStash.getProperty(ID) >> ID
        1 * user.getProperty(USER_PROFILE) >> userProfile
        0 * _

        and:
        result.contains title
        result.contains NAME
        result.contains DESCRIPTION
        result.contains itemImage
        result.contains sourceImage
    }
}
