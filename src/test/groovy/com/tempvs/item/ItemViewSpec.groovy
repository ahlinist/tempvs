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
    private static final String ITEMS = 'items'
    private static final String PERIOD = 'period'
    private static final String SOURCE = 'source'
    private static final String IMAGES = 'images'
    private static final String IMAGE_INFO = 'imageInfo'
    private static final String DESCRIPTION = 'description'
    private static final String ITEM_TITLE = 'item.show.title'
    private static final String SOURCE_SERVICE = 'sourceService'
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
    def userProfile = Mock UserProfile
    def sourceService = Mock SourceService
    def period = Period.valueOf 'ANCIENT'
    def applicationContext = Mock ApplicationContext

    def setup() {
        user = Mock User
        itemGroup = Mock ItemGroup
        item = Mock Item
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
        3 * itemGroup.getProperty(NAME) >> NAME
        2 * itemGroup.getProperty(DESCRIPTION) >> DESCRIPTION
        3 * itemGroup.getProperty(ID) >> ONE
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
        List items = [item]
        Map model = [
                itemGroup: itemGroup,
                user: user,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
                applicationContext: applicationContext,
        ]

        when:
        String result = render view: '/item/group', model: model

        then:
        1 * applicationContext.containsBean(SOURCE_SERVICE) >> Boolean.TRUE
        1 * applicationContext.getBean(SOURCE_SERVICE) >> sourceService
        1 * sourceService.getSourcesByPeriod(null)
        1 * itemGroup.getProperty(NAME) >> NAME
        1 * itemGroup.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * itemGroup.getProperty(ID) >> ONE
        1 * itemGroup.getProperty(ITEMS) >> items
        3 * item.getProperty(ID) >> ONE
        2 * item.getProperty(NAME) >> NAME
        1 * item.getProperty(DESCRIPTION) >> DESCRIPTION
        0 * _

        and:
        result.contains title
        result.contains createItemButton
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

        Map model = [
                item: item,
                itemGroup: itemGroup,
                user: user,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
                applicationContext: applicationContext,
        ]

        when:
        String result = render view: '/item/show', model: model

        then:
        2 * item.getProperty(ID) >> ID
        3 * item.getProperty(NAME) >> NAME
        1 * item.getProperty(DESCRIPTION) >> DESCRIPTION
        2 * item.getProperty(IMAGES) >> [image]
        1 * item.getProperty(PERIOD) >> period
        1 * item.getProperty(SOURCE) >> source
        1 * source.getProperty(IMAGES) >> [image]
        1 * source.getProperty(NAME) >> NAME
        1 * source.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * source.getProperty(PERIOD) >> period
        0 * _

        and:
        result.contains title
        result.contains NAME
        result.contains DESCRIPTION
        result.contains carousel
    }

    void "Test /item/editItemPage with id"() {
        given:
        String title = '<title>Tempvs - Edit'

        Map model = [
                item: item,
                itemGroup: itemGroup,
                user: user,
                userProfile: userProfile,
                editAllowed: Boolean.TRUE,
                applicationContext: applicationContext,
        ]

        when:
        String result = render view: '/item/editItemPage', model: model

        then:
        1 * applicationContext.containsBean(SOURCE_SERVICE) >> Boolean.TRUE
        1 * applicationContext.getBean(SOURCE_SERVICE) >> sourceService
        1 * sourceService.getSourcesByPeriod(period) >> [source]
        1 * item.getProperty(ID) >> ID
        2 * item.getProperty(NAME) >> NAME
        1 * item.getProperty(DESCRIPTION) >> DESCRIPTION
        1 * item.getProperty(IMAGES) >> [image]
        2 * item.getProperty(PERIOD) >> period
        2 * item.getProperty(SOURCE) >> source
        2 * image.getProperty(ID) >> ID
        3 * image.getProperty(IMAGE_INFO) >> IMAGE_INFO
        1 * source.getProperty(NAME) >> NAME
        0 * _

        and:
        result.contains title
        result.contains NAME
        result.contains DESCRIPTION
    }
}
