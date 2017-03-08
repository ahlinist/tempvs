package com.tempvs.domain.user.verification

import com.tempvs.domain.BasePersistent

class EmailVerification extends BasePersistent {
    Long userId
    String email
    String action
    String verificationCode

    static constraints = {
        userId nullable: true, validator: { userId, verification ->
            if (verification.action == 'register') {
                true
            } else {
                userId == null ? false : true
            }
        }
        email email: true, unique: ['action']
        action inList: ['register', 'updateEmail', 'updateProfileEmail']
        verificationCode unique: true
    }
}
