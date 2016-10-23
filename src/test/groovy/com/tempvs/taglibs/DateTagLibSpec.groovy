package com.tempvs.taglibs

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.GroovyPageUnitTestMixin
import groovy.time.TimeCategory
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(DateTagLib)
@TestMixin(GroovyPageUnitTestMixin)
class DateTagLibSpec extends Specification {
    private static final String DATE_FORMAT = 'dd-MM-yyyy'
    private static final String RIGHT_NOW = 'date.rightNow'
    private static final String MINUTES_AGO = 'date.minutesAgo'
    private static final String HALF_HOUR_AGO = 'date.halfHourAgo'
    private static final String HOURS_AGO = 'date.hoursAgo'
    private static final String YESTERDAY = 'date.yesterday'

    def setup() {
    }

    def cleanup() {
    }

    void "test tempvs:dateFromNow tag"() {
        given: "populate messagesources"
        messageSource.addMessage RIGHT_NOW, request.locale, 'RIGHT_NOW'
        messageSource.addMessage MINUTES_AGO, request.locale, 'MINUTES_AGO'
        messageSource.addMessage HALF_HOUR_AGO, request.locale, 'HALF_HOUR_AGO'
        messageSource.addMessage HOURS_AGO, request.locale, 'HOURS_AGO'
        messageSource.addMessage YESTERDAY, request.locale, 'YESTERDAY'

        expect:
        applyTemplate('<tempvs:dateFromNow date="${date}" />', [date: date]).contains value

        where:
        date | value
        new Date() | 'RIGHT_NOW'
        calculateDate([number: 1, UoM: 'minutes']) | 'MINUTES_AGO'
        calculateDate([number: 20, UoM: 'minutes']) | 'MINUTES_AGO'
        calculateDate([number: 30, UoM: 'minutes']) | 'HALF_HOUR_AGO'
        calculateDate([number: 50, UoM: 'minutes']) | 'HALF_HOUR_AGO'
        calculateDate([number: 2, UoM: 'hour']) | '2 HOURS_AGO'
        calculateDate([number: 20, UoM: 'hour']) | '20 HOURS_AGO'
        calculateDate([number: 30, UoM: 'hour']) | 'YESTERDAY'
        calculateDate([number: 2, UoM: 'day']) | (new Date() -2).format(DATE_FORMAT)
    }

    private Date calculateDate(Map params) {
        use(TimeCategory) {
            (params.number).(params.UoM).ago
        }
    }
}
