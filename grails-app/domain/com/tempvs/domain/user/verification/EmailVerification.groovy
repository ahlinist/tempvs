package com.tempvs.domain.user.verification

import com.tempvs.domain.BasePersistent

class EmailVerification extends BasePersistent {
    Long userId
    String email
    String destination
    String password
    String firstName
    String lastName
    String action
    String verificationCode

    static constraints = {
        userId nullable: true, validator: { userId, verification ->
            if (verification.action == 'registerUser') {
                true
            } else {
                userId == null ? false : true
            }
        }
        email nullable: true, email: true, unique: ['destination']
        action inList: ['registerUser', 'updateEmail', 'updateProfileEmail'], validator: { action, verification ->
            if (action == 'registerUser') {
                verification.email && verification.password
            } else {
                true
            }
        }
        destination email: true, unique: ['action', 'userId']
        firstName nullable: true
        lastName nullable: true
        password nullable: true
        verificationCode unique: true
    }
}
