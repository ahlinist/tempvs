package com.tempvs.user.verification

class EmailVerificationJob {
    static triggers = {
      simple repeatInterval: 3600000l //run hourly
    }

    def execute() {
        EmailVerification.executeUpdate("delete EmailVerification where date_created < :dateToDelete ", [dateToDelete: new Date() -1])
    }
}
