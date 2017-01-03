package com.tempvs.tests.utils.user

import com.tempvs.domain.user.User
import com.tempvs.tests.utils.TestingUtils

trait WithUser {
    private static User user

    static User getUser() {
        if (!this.user) {
            this.user = TestingUtils.createUser()
        }

        this.user.save(flush: true)

        return this.user
    }
}