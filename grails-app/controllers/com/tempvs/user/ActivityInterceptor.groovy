package com.tempvs.user

import groovy.transform.CompileStatic

@CompileStatic
class ActivityInterceptor {
    UserService userService

    ActivityInterceptor() {
        matchAll().
                excludes(controller: 'auth').
                excludes(controller: 'image', action: 'getAvatar').
                excludes(controller: 'user', action: ~/(index|register)/).
                excludes(controller: 'userProfile', action: 'show').
                excludes(controller: 'verify')
    }

    boolean before() {
        userService.updateLastActive() as Boolean
    }
}
