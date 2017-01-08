package com.tempvs.interceptors

import com.tempvs.services.UserService
import groovy.transform.CompileStatic

@CompileStatic
class ActivityInterceptor {
    UserService userService

    ActivityInterceptor() {
        matchAll().excludes(controller: "auth", action: ~/(login|register)/)
    }

    boolean before() {
        userService.updateLastActive()
        true
    }
}
