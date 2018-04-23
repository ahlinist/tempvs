package club.tempvs.common

import grails.testing.web.taglib.TagLibUnitTest
import spock.lang.Specification

class CommonTagLibSpec extends Specification implements TagLibUnitTest<CommonTagLib> {

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
}
