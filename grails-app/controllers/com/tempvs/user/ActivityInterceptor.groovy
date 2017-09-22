package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService

/**
 * Interceptor that records the date of {@link com.tempvs.user.User} last activity.
 */
@GrailsCompileStatic
class ActivityInterceptor {

    UserService userService
    SpringSecurityService springSecurityService

    ActivityInterceptor() {
        matchAll().
                excludes(controller: 'auth').
                excludes(controller: 'verify').
                excludes(controller: 'image')
    }

    boolean after() {
        if (springSecurityService.loggedIn) {
            User user = userService.currentUser

            if (user) {
                user.lastActive = new Date()
                userService.saveUser(user)
            }
        }

        Boolean.TRUE
    }
}
