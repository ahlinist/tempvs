package com.tempvs.interceptors

import groovy.transform.CompileStatic

@CompileStatic
class SecurityInterceptor {
    SecurityInterceptor() {
        matchAll().excludes(controller: "user", action: ~/(login|register|show|logout)/)
    }

    boolean before() {
        if (session['user']) {
            true
        } else {
            redirect controller: 'user', action: "login"
        }
    }
}
