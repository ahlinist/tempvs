package com.tempvs.user

import grails.compiler.GrailsCompileStatic
import grails.plugin.springsecurity.SpringSecurityService

/**
 * Interceptor that records the date of {@link com.tempvs.user.User} last activity.
 */
@GrailsCompileStatic
class ActivityInterceptor {

    private static String LAST_ACTIVE = 'lastActive'

    UserService userService
    SpringSecurityService springSecurityService

    ActivityInterceptor() {
        matchAll().
                excludes(controller: 'auth').
                excludes(controller: 'verify').
                excludes(controller: 'image')
    }

    boolean before() {
        if (springSecurityService.loggedIn) {
            User user = userService.currentUser

            if (user) {
                userService.editUserField(user, LAST_ACTIVE, new Date())
            }
        }

        Boolean.TRUE
    }
}
