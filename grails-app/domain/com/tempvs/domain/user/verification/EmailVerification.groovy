package com.tempvs.domain.user.verification

import com.tempvs.domain.BasePersistent

class EmailVerification extends BasePersistent {
    String email
    String destination
    String password
    String firstName
    String lastName
    String action
    String verificationCode

    static constraints = {
        email nullable: true
        action inList: ['registerUser', 'updateEmail', 'updateProfileEmail'], validator: { action, emailVerification ->
            if (action == 'registerUser') {
                emailVerification.password && emailVerification.firstName && emailVerification.lastName
            } else {
                true
            }
        }
        destination email: true, unique: ['action', 'email']
        firstName nullable: true
        lastName nullable: true
        password nullable: true
        verificationCode unique: true
    }
}
