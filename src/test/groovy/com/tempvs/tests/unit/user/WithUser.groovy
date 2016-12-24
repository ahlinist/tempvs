package com.tempvs.tests.unit.user

import com.tempvs.domain.user.User
import com.tempvs.tests.unit.UnitTestUtils

trait WithUser {
    private static User mockedUser

    static User getUser() {
        if (!this.mockedUser) {
            this.mockedUser = UnitTestUtils.createUser()
        }

        return this.mockedUser
    }
}