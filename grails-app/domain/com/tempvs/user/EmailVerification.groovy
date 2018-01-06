package com.tempvs.user

import com.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic

/**
 * An object that represents a sent verification of email address.
 */
@GrailsCompileStatic
class EmailVerification implements BasePersistent {

    Long instanceId
    String email
    String action
    String verificationCode

    static constraints = {
        instanceId nullable: true, validator: { instanceId, EmailVerification verification ->
            if (verification.action == 'registration') {
                true
            } else {
                instanceId == null ? false : true
            }
        }
        email email: true, unique: ['action']
        action inList: ['registration', 'email', 'userProfile', 'clubProfile']
        verificationCode unique: true
    }
}
