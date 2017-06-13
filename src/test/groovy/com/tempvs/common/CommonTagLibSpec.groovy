package com.tempvs.common

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(CommonTagLib)
class CommonTagLibSpec extends Specification {

    private static final String ID = 'id'
    private static final String URL = "'/item/deleteItem/id'"
    private static final String ITEM = 'item'
    private static final String SIZE = 'size'
    private static final String MESSAGE = 'message'
    private static final String DELETE_ITEM = 'deleteItem'

    def setup() {
    }

    def cleanup() {
    }

    void "Test tempvs:ajaxLink"() {
        given:
        String event = "<span onclick=\"sendAjaxRequest(this, ${URL});\">"
        String spinner = '<asset:image class="ajaxSpinner" style="display: none" src="spinner.gif"/>'

        when:
        def template = applyTemplate("<tempvs:ajaxLink message=\"${MESSAGE}\" controller=\"${ITEM}\" action=\"${DELETE_ITEM}\" id=\"${ID}\"/>")

        then:
        template.contains spinner
        template.contains event
    }

    void "Test tempvs:modalButton"() {
        given:
        String button = "<button type=\"button\" class=\"btn btn-default\" data-toggle=\"modal\" data-target=\"#${ID}\">"
        String modalTemplate = '<div id="id" class="modal fade" role="dialog">'

        when:
        def template = applyTemplate("<tempvs:modalButton id=\"${ID}\" size=\"${SIZE}\" message=\"${MESSAGE}\"/>")

        then:
        template.contains button
        template.contains modalTemplate
        template.contains MESSAGE
    }
}
