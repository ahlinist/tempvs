package com.tempvs.interceptors

import com.tempvs.domain.user.User
import com.tempvs.services.UserService
import groovy.transform.CompileStatic

@CompileStatic
class ActivityInterceptor {
    UserService userService

    ActivityInterceptor() {
        matchAll().excludes(controller: "user", action: ~/(login|register)/)
    }

    boolean before() {
        if (session) {
            User user = (User) session['user']

            if (user) {
                userService.updateLastActive(user.id)
            }

            true
        }
    }
}
