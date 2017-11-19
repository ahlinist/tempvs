package com.tempvs.item

import com.tempvs.image.Image
import com.tempvs.periodization.Period
import com.tempvs.user.User
import com.tempvs.user.UserProfile
import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import org.springframework.context.ApplicationContext
import spock.lang.Specification

/**
 * Unit-test suite for testing {@link com.tempvs.item.Item}-related views.
 */
@Mock([Source])
@TestMixin(GroovyPageUnitTestMixin)
class ItemViewSpec extends Specification {

    private static final String ID = 'id'
    private static final String ONE = '1'
    private static final String NAME = 'name'
    private static final String PERIOD = 'period'
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_TITLE = 'item.show.title'
    private static final String ITEM_STASH_TITLE = 'item.stash.title'
    private static final String ITEM_GROUP_TITLE = 'item.group.title'
    private static final String ITEM_NOT_FOUND = 'item.notFound.message'
    private static final String STASH_NOT_FOUND = 'item.stash.notFound.message'
    private static final String GROUP_NOT_FOUND = 'item.group.notFound.message'

    def user
    def item
    def itemGroup
    def image = Mock Image
    def source = Mock Source
    def period = Period.ANCIENT
    def userProfile = Mock UserProfile
    def applicationContext = Mock ApplicationContext

    def setup() {
        user = Mock User
        item = Mock Item
        itemGroup = Mock ItemGroup
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
        String createButton = '<tempvs:modalButton id="createGroup" classes="glyphicon glyphicon-plus">'
        String itemGroupLink = '<a href="/item/group/1" class="btn btn-default col-sm-3" data-toggle="tooltip" data-placement="bottom" title="description">'
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
        String createItemButton = '<tempvs:modalButton id="itemForm" classes="glyphicon glyphicon-plus">'
        Set items = [item]
        String smartForm = '<tempvs:ajaxSmartForm type="text" action="editItemGroupField"'

        Map model = [
                itemGroup: itemGroup,
                user: user,
                items: items,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
        ]

        when:
        String result = render view: '/item/group', model: model

        then:
        1 * itemGroup.getProperty(NAME) >> NAME
        1 * itemGroup.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * itemGroup.getProperty(ID) >> ONE
        1 * item.getProperty(ID) >> ONE
        1 * item.getProperty(NAME) >> NAME
        1 * item.getProperty(DESCRIPTION) >> DESCRIPTION
        0 * _

        and:
        result.contains title
        result.contains createItemButton
        result.contains smartForm
    }

    void "Test /item/show without id"() {
        given:
        String title = "<title>Tempvs - ${ITEM_TITLE}</title>"
        Map model = [:]

        when:
        String result = render view: '/item/show', model: model

        then:
        result.contains title
        result.contains ITEM_NOT_FOUND
    }

    void "Test /item/show with id"() {
        given:
        String title = "<title>Tempvs - ${NAME}</title>"
        String carousel = "<tempvs:carousel images="
        String smartForm = '<tempvs:ajaxSmartForm type="text" action="editItemField"'

        Map model = [
                item: item,
                itemGroup: itemGroup,
                sources: [source],
                images: [image],
                user: user,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
                applicationContext: applicationContext,
        ]

        when:
        String result = render view: '/item/show', model: model

        then:
        1 * item.getProperty(ID) >> ID
        2 * item.getProperty(NAME) >> NAME
        1 * item.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * item.getProperty(PERIOD) >> period
        2 * source.getProperty(ID) >> ID
        2 * source.getProperty(NAME) >> NAME
        1 * image.getProperty(ID) >> ID
        1 * image.getProperty(IMAGE_INFO) >> IMAGE_INFO
        0 * _

        and:
        result.contains title
        result.contains NAME
        result.contains DESCRIPTION
        result.contains carousel
        result.contains smartForm
    }
}
