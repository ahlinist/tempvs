package com.tempvs.user

import com.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class EmailVerification extends BasePersistent {
    Long userId
    String email
    String action
    String verificationCode

    static constraints = {
        userId nullable: true, validator: { userId, EmailVerification verification ->
            if (verification.action == 'registration') {
                true
            } else {
                userId == null ? false : true
            }
        }
        email email: true, unique: ['action']
        action inList: ['registration', 'email', 'profileEmail']
        verificationCode unique: true
    }
}
