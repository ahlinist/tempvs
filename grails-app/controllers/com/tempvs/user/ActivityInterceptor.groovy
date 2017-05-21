package com.tempvs.user

import groovy.transform.CompileStatic

/**
 * Interceptor that records the date of {@link com.tempvs.user.User} last activity.
 */
@CompileStatic
class ActivityInterceptor {
    UserService userService

    ActivityInterceptor() {
        matchAll().
                excludes(controller: 'auth').
                excludes(controller: 'verify').
                excludes(controller: 'image')
    }

    boolean before() {
        userService.updateLastActive()
        Boolean.TRUE
    }
}

