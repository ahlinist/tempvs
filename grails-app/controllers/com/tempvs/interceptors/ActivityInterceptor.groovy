package com.tempvs.interceptors

import com.tempvs.domain.user.User
import com.tempvs.services.UserService
import grails.plugin.springsecurity.SpringSecurityService
import groovy.transform.CompileStatic

@CompileStatic
class ActivityInterceptor {
    UserService userService
    SpringSecurityService springSecurityService

    ActivityInterceptor() {
        matchAll().excludes(controller: "user", action: ~/(login|register)/)
    }

    boolean before() {
        User currentUser = (User) springSecurityService.currentUser

        if (currentUser) {
            userService.updateLastActive(currentUser.id)
        }

        true
    }
}
