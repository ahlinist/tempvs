package com.tempvs.user

import groovy.transform.CompileStatic

@CompileStatic
class ActivityInterceptor {
    UserService userService

    ActivityInterceptor() {
        matchAll().
                excludes(controller: 'auth').
                excludes(controller: 'verify')
    }

    boolean before() {
        userService.updateLastActive()
        Boolean.TRUE
    }
}
