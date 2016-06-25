package com.tempvs

import groovy.time.TimeCategory

class DateTagLib {
    static defaultEncodeAs = [taglib:'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]
    static namespace = 'tempvs'

    String dateFromNow = { attrs ->
        Date currentDate = new Date()
        Date targetDate = attrs.date
        String DATE_FORMAT = 'dd-MM-yyyy'
        def result

        if (currentDate < targetDate) {
            result = ''
        } else if (currentDate - targetDate > 1) {
            result = targetDate.format 'dd-MM-yyyy'
        } else {
            use (TimeCategory) {
                if (targetDate > 1.minute.ago) {
                    result = "<span class='incrementMinutes' minutes='0'>${g.message(code: 'date.rightNow')}</span>"
                } else if(targetDate > 30.minutes.ago) {
                    def minutes = (currentDate - targetDate).minutes
                    result = "<span class='incrementMinutes' minutes='${minutes}'>${minutes} ${g.message(code: 'date.minutesAgo')}</span>"
                } else if (targetDate > 1.hour.ago) {
                    result = "<span>${g.message(code: 'date.halfHourAgo')}</span>"
                } else if (targetDate > 1.day.ago) {
                    def hours = (currentDate - targetDate).hours
                    result = "<span>${hours} ${g.message(code: 'date.hoursAgo')}</span>"
                } else if (targetDate > 2.days.ago) {
                    result = "<span>${g.message(code: 'date.yesterday')}</span>"
                } else {
                    result = "<span>${targetDate.format(DATE_FORMAT)}</span>"
                }
            }
        }

        out << result
    }
}
