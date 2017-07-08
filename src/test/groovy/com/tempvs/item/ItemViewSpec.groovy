package com.tempvs.item

import com.tempvs.image.Image
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

    private static final String ID = 'id'
    private static final String ONE = '1'
    private static final String NAME = 'name'
    private static final String ITEMS = 'items'
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String ITEM_IMAGE = 'itemImage'
    private static final String DESCRIPTION = 'description'
    private static final String SOURCE_IMAGE = 'sourceImage'
    private static final String ITEM_TITLE = 'item.show.title'
    private static final String ITEM_STASH_TITLE = 'item.stash.title'
    private static final String ITEM_GROUP_TITLE = 'item.group.title'
    private static final String ITEM_NOT_FOUND = 'item.item.notFound.message'
    private static final String STASH_NOT_FOUND = 'item.stash.notFound.message'
    private static final String GROUP_NOT_FOUND = 'item.group.notFound.message'

    def user
    def item
    def itemGroup
    def image = Mock(Image)
    def userProfile = Mock(UserProfile)

    def setup() {
        user = Mock(User)
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
        String createButton = '<tempvs:modalButton id="createGroup" message="item.createGroup.link">'
        String itemGroupLink = '<a href="/item/group/1" class="list-group-item">'
        Map model = [
                user: user,
                itemGroups: [itemGroup],
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
        ]

        when:
        String result = render view: '/item/stash', model: model

        then:
        1 * itemGroup.getProperty(NAME) >> NAME
        1 * itemGroup.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * itemGroup.getProperty(ID) >> ONE
        1 * user.getProperty(ID) >> ONE
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
        String createItemButton = '<tempvs:modalButton id="itemForm" message="item.createItem.link">'
        List items = [item]
        Map model = [
                itemGroup: itemGroup,
                user: user,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
        ]

        when:
        String result = render view: '/item/group', model: model

        then:
        3 * itemGroup.getProperty(NAME) >> NAME
        1 * itemGroup.getProperty(DESCRIPTION) >> DESCRIPTION
        2 * itemGroup.getProperty(ID) >> ID
        1 * itemGroup.getProperty(ITEMS) >> items
        1 * item.getProperty(ID) >> ID
        1 * item.getProperty(NAME) >> NAME
        1 * user.getProperty(ID) >> ONE
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
        String itemImage = "<tempvs:image image=\"${image}\"/>"
        String sourceImage = "<tempvs:image image=\"${image}\"/>"
        String updateItemImageForm = '<tempvs:ajaxForm action="updateItemImage">'
        Map model = [
                item: item,
                itemGroup: itemGroup,
                user: user,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
        ]

        when:
        String result = render view: '/item/show', model: model

        then:
        2 * item.getProperty(ID) >> ID
        5 * item.getProperty(NAME) >> NAME
        2 * item.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * item.getProperty(ITEM_IMAGE) >> image
        1 * item.getProperty(SOURCE_IMAGE) >> image
        2 * image.getProperty(IMAGE_INFO)
        1 * itemGroup.getProperty(ID) >> ID
        1 * itemGroup.getProperty(NAME) >> NAME
        1 * user.getProperty(ID) >> ID
        0 * _

        and:
        result.contains title
        result.contains NAME
        result.contains DESCRIPTION
        result.contains itemImage
        result.contains sourceImage
        result .contains updateItemImageForm
    }
}
