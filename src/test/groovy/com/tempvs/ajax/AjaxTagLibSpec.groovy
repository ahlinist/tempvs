package com.tempvs.ajax

import grails.testing.web.taglib.TagLibUnitTest
import spock.lang.Specification

class AjaxTagLibSpec extends Specification implements TagLibUnitTest<AjaxTagLib> {

    private static final String ID = 'id'
    private static final String ITEM = 'item'
    private static final String MESSAGE = 'message'
    private static final String SELECTOR = 'selector'
    private static final String DELETE_METHOD = 'DELETE'
    private static final String DELETE_ITEM = 'deleteItem'
    private static final String DELETE_ITEM_URL = '/item/deleteItem/id'

    def setup() {
    }

    def cleanup() {
    }

    void "Test tempvs:ajaxSubmitButton"() {
        given:
        String submitButton = '<button name="submit-button" class="btn btn-light submit-button">'

        when:
        def template = applyTemplate("<tempvs:ajaxSubmitButton value='${MESSAGE}' />")

        then:
        template.contains submitButton
        template.contains MESSAGE
    }

    void "Test tempvs:ajaxLink"() {
        given:
        String event = "<span onclick=\"sendAjaxRequest(this, '${DELETE_ITEM_URL}', '${DELETE_METHOD}', '${SELECTOR}', getActions());\">"

        when:
        def template = applyTemplate("<tempvs:ajaxLink controller=\"${ITEM}\" action=\"${DELETE_ITEM}\" id=\"${ID}\" method=\"${DELETE_METHOD}\" selector=\"${SELECTOR}\"/>")

        then:
        template.contains event
    }

    void "Test tempvs:ajaxForm"() {
        given:
        String controller = 'testController'
        String action = 'testAction'
        String ajaxForm = "<form action=\"/testController/testAction\" method=\"post\" onsubmit=\"submitAjaxForm"

        when:
        def template = applyTemplate("<tempvs:ajaxForm controller=\"${controller}\" action=\"${action}\" selector=\"${SELECTOR}\"/>")

        then:
        template.contains ajaxForm
    }
}
