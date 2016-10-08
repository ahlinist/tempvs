package com.tempvs.taglibs.form

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(FormToolsTagLib)
class FormToolsTagLibSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test tempvs:ajaxSubmitButton"() {
        when: "apply tempvs:ajaxSubmitButton"
        def template = applyTemplate('<tempvs:ajaxSubmitButton value="testMsg" />')

        then:
        template.contains 'testMsg'

        and:
        template.contains 'spinner.gif'
    }

    void "test tempvs:formField"() {
        when: ""
        def template = applyTemplate('<tempvs:formField type="text" name="name" value="" label="label" />')

        then:
        template.contains '<input'

        and:
        template.contains 'type="text"'
    }
}
