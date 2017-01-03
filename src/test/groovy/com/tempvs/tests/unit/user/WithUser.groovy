package com.tempvs.tests.unit.user

import com.tempvs.domain.user.User
import com.tempvs.tests.unit.UnitTestUtils

trait WithUser {
    private static User user

    static User getUser() {
        if (!this.user) {
            this.user = UnitTestUtils.createUser()
        }

        this.user.save(flush: true)

        return this.user
    }
}