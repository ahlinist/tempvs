package com.tempvs.item

import com.tempvs.ajax.AjaxResponseHelper
import com.tempvs.periodization.Period
import com.tempvs.user.ClubProfile
import com.tempvs.user.ProfileHolder
import com.tempvs.user.User
import grails.converters.JSON
import grails.gsp.PageRenderer
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import spock.lang.Specification

class PassportControllerSpec extends Specification implements ControllerUnitTest<PassportController> {

    private static final String ONE = '1'
    private static final Long LONG_ONE = 1L
    private static final Long LONG_TWO = 2L
    private static final Long LONG_THREE = 3L
    private static final String NAME = 'name'
    private static final String GET_METHOD = 'GET'
    private static final String POST_METHOD = 'POST'
    private static final String PROFILE_URL = '/profile'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String SUCCESS_ACTION = 'success'
    private static final String FIELD_VALUE = 'fieldValue'
    private static final String CLUB_PROFILE = 'clubProfile'
    private static final String DELETE_ACTION = 'deleteElement'
    private static final String REPLACE_ACTION = 'replaceElement'

    def json = Mock JSON
    def user = Mock User
    def item = Mock Item
    def type = GroovyMock Type
    def passport = Mock Passport
    def period = GroovyMock Period
    def clubProfile = Mock ClubProfile
    def item2Passport = Mock Item2Passport

    def itemService = Mock ItemService
    def profileHolder = Mock ProfileHolder
    def passportService = Mock PassportService
    def groovyPageRenderer = Mock PageRenderer
    def ajaxResponseHelper = Mock AjaxResponseHelper


    def setup() {
        controller.itemService = itemService
        controller.profileHolder = profileHolder
        controller.passportService = passportService
        controller.ajaxResponseHelper = ajaxResponseHelper
        controller.groovyPageRenderer = groovyPageRenderer
    }

    def cleanup() {
    }

    void "Test index()"() {
        when:
        controller.index()

        then:
        0 * _

        response.redirectedUrl == PROFILE_URL
    }

    void "Test createPassport()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.createPassport(passport)

        then:
        1 * profileHolder.profile >> clubProfile
        1 * clubProfile.asType(ClubProfile) >> clubProfile
        1 * passport.setClubProfile(clubProfile)
        1 * passportService.createPassport(passport) >> passport
        1 * passport.hasErrors() >> Boolean.FALSE
        1 * passport.id >> LONG_ONE
        1 * ajaxResponseHelper.renderRedirect(_) >> json
        1 * json.render(_ as GrailsMockHttpServletResponse)
        0 * _
    }

    void "Test show()"() {
        given:
        request.method = GET_METHOD

        when:
        def result = controller.show(ONE)

        then:
        1 * passportService.getPassport(ONE) >> passport
        1 * passport.clubProfile >> clubProfile
        1 * clubProfile.period >> period
        1 * passportService.getItem2PassportRelations(passport) >> [item2Passport]
        1 * item2Passport.item >> item
        1 * item.type >> type
        1 * itemService.getItemsByPeriod(period) >> [item, item]
        1 * profileHolder.profile >> clubProfile
        0 * _

        and:
        result == [
                clubProfile: clubProfile,
                passport: passport,
                itemMap: [(type): [item2Passport]],
                availableItems: [item, item],
                editAllowed: Boolean.TRUE,
        ]
    }

    void "Test editPassportField()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.editPassportField(LONG_ONE, NAME, FIELD_VALUE)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * passportService.editPassportField(passport, NAME, FIELD_VALUE) >> passport
        1 * passport.hasErrors() >> Boolean.FALSE
        0 * _

        and:
        response.json.action == SUCCESS_ACTION
    }

    void "Test addItem()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.addItem(LONG_ONE, LONG_TWO, LONG_THREE)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * itemService.getItem(LONG_TWO) >> item
        1 * passportService.addItem(passport, item, LONG_THREE) >> item2Passport
        1 * item2Passport.hasErrors() >> Boolean.FALSE
        1 * passportService.getItem2PassportRelations(passport) >> [item2Passport]
        1 * item2Passport.item >> item
        1 * item.type >> type
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test removeItem()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.removeItem(LONG_ONE, LONG_TWO)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * itemService.getItem(LONG_TWO) >> item
        1 * passportService.removeItem(passport, item)
        1 * passportService.getItem2PassportRelations(passport) >> [item2Passport]
        1 * item2Passport.item >> item
        1 * item.type >> type
        1 * groovyPageRenderer.render(_ as Map)
        0 * _

        and:
        response.json.action == REPLACE_ACTION
    }

    void "Test deletePassport()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.deletePassport(LONG_ONE)

        then:
        1 * passportService.getPassport(LONG_ONE) >> passport
        1 * passportService.deletePassport(passport)
        0 * _

        and:
        response.json.action == DELETE_ACTION
    }
}
