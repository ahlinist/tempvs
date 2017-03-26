package com.tempvs.form

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(FormToolsTagLib)
class FormToolsTagLibSpec extends Specification {
    private static final String TEST_MESSAGE = 'testMsg'
    private static final String SPINNER = 'spinner.gif'

    def setup() {
    }

    def cleanup() {
    }

    void "Test tempvs:ajaxSubmitButton"() {
        when: 'Applying tempvs:ajaxSubmitButton'
        def template = applyTemplate("<tempvs:ajaxSubmitButton value='${TEST_MESSAGE}' />")

        then: 'Rendered template contains test message'
        template.contains TEST_MESSAGE

        and: 'Rendered template contains spinner'
        template.contains SPINNER
    }

    void "Test tempvs:formField"() {
        when: 'Applying tempvs:formField'
        def template = applyTemplate('<tempvs:formField type="text" name="name" value="" label="label" />')

        then: 'Rendered template contains input fields'
        template.contains '<input'

        and: 'Rendered template contains attributes of text type'
        template.contains 'type="text"'
    }
}
