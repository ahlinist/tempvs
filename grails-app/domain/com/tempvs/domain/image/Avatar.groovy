package com.tempvs.domain.image

import com.tempvs.domain.user.UserProfile

class Avatar extends Image {
    static belongsTo = [userProfile: UserProfile]

    static constraints = {
    }
}
