package com.tempvs.interceptors

import com.tempvs.services.UserService
import groovy.transform.CompileStatic

@CompileStatic
class ActivityInterceptor {
    UserService userService

    ActivityInterceptor() {
        matchAll().excludes(controller: ~/(auth|image)/)
    }

    boolean before() {
        userService.updateLastActive()
        true
    }
}
