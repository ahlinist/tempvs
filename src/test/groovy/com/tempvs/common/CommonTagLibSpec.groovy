package com.tempvs.common

import grails.testing.web.taglib.TagLibUnitTest
import spock.lang.Specification

class CommonTagLibSpec extends Specification implements TagLibUnitTest<CommonTagLib> {

    private static final String ID = 'id'
    private static final String SIZE = 'size'
    private static final String STYLES = 'styles'
    private static final String CLASSES = 'classes'

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
        String button = "<button class=\"btn btn-default ${CLASSES}\" style=\"${STYLES}\" data-toggle=\"modal\" data-target=\"#${ID}\">"
        String modalTemplate = '<div id="id" class="modal fade" role="dialog">'

        when:
        def template = applyTemplate("<tempvs:modalButton id=\"${ID}\" size=\"${SIZE}\" classes=\"${CLASSES}\" styles=\"${STYLES}\"/>")

        then:
        template.contains button
        template.contains modalTemplate
        template.contains CLASSES
    }
}
