package com.tempvs.domain.user

import com.tempvs.domain.BasePersistent

class User extends BasePersistent{
    String firstName
    String lastName
    String email
    String password

    static hasOne = [userProfile: UserProfile]

    static constraints = {
        email email: true, unique: true, blank: false
        password blank:false
    }
}
