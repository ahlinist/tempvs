package com.tempvs.form

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(FormToolsTagLib)
class FormToolsTagLibSpec extends Specification {

    private static final String MESSAGE = 'message'

    def setup() {
    }

    def cleanup() {
    }

    void "Test tempvs:ajaxSubmitButton"() {
        given:
        String submitButton = "<button class=\"btn btn-primary submit-button\">"
        String spinner = '<asset:image class="ajaxSpinner" style="display: none" src="spinner.gif"/>'

        when:
        def template = applyTemplate("<tempvs:ajaxSubmitButton value='${MESSAGE}' />")

        then:
        template.contains submitButton
        template.contains MESSAGE
        template.contains spinner
    }

    void "Test tempvs:formField"() {
        when:
        def template = applyTemplate('<tempvs:formField type="text" name="name" value="" label="label" />')

        then:
        template.contains '<input type="text" '
    }

    void "Test tempvs:ajaxForm"() {
        given:
        String controller = 'testController'
        String action = 'testAction'
        String ajaxForm = '<form action="/testController/testAction" method="post" onsubmit="submitAjaxForm(this); return false;" >'

        when:
        def template = applyTemplate("<tempvs:ajaxForm controller=\"${controller}\" action=\"${action}\"/>")

        then:
        template.contains ajaxForm
    }
}
