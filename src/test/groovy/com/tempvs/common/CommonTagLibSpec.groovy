package com.tempvs.common

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(CommonTagLib)
class CommonTagLibSpec extends Specification {

    private static final String ID = 'id'
    private static final String SIZE = 'size'
    private static final String MESSAGE = 'message'

    def setup() {
    }

    def cleanup() {
    }

    void "Test tempvs:formField"() {
        when:
        def template = applyTemplate('<tempvs:formField type="text" name="name" value="" label="label" />')

        then:
        template.contains '<input type="text" '
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
