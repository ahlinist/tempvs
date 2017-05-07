package com.tempvs.domain

import grails.validation.Validateable
import groovy.transform.CompileStatic

@CompileStatic
abstract class BaseObject implements Validateable {
    Object save() {

    }
}
