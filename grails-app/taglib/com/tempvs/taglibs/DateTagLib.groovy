package com.tempvs.taglibs

import groovy.time.TimeCategory

class DateTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = 'tempvs'
    private static final String DATE_FORMAT = 'dd-MM-yyyy'
    private static final String RIGHT_NOW = 'date.rightNow'
    private static final String MINUTES_AGO = 'date.minutesAgo'
    private static final String HALF_HOUR_AGO = 'date.halfHourAgo'
    private static final String HOURS_AGO = 'date.hoursAgo'
    private static final String YESTERDAY = 'date.yesterday'

    String dateFromNow = { attrs ->
        Map model = [:]
        Date currentDate = new Date()
        Date targetDate = attrs.date

        if (currentDate < targetDate) {

        } else {
            use (TimeCategory) {
                if (targetDate > 1.minute.ago) {
                    model = [classes:'incrementMinutes', minutes: 0, value: g.message(code: RIGHT_NOW)]
                } else if(targetDate > 30.minutes.ago) {
                    def minutes = (currentDate - targetDate).minutes
                    model = [classes:'incrementMinutes', minutes: minutes, value: "${minutes} ${g.message(code: MINUTES_AGO)}"]
                } else if (targetDate > 1.hour.ago) {
                    model = [value: g.message(code: HALF_HOUR_AGO)]
                } else if (targetDate > 1.day.ago) {
                    model = [value: "${(currentDate - targetDate).hours} ${g.message(code: HOURS_AGO)}"]
                } else if (targetDate > 2.days.ago) {
                    model = [value: g.message(code: YESTERDAY)]
                } else {
                    model = [value: targetDate.format(DATE_FORMAT)]
                }
            }
        }

        out << render(template: '/templates/date/dateHolder', model:model)
    }
}
