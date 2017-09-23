package com.tempvs.domain

import grails.validation.Validateable
import groovy.transform.CompileStatic

/**
 * A class inherited by all domains.
 */
@CompileStatic
abstract class BasePersistent implements Validateable {

    Date dateCreated
    Date lastUpdated

    public <T> T save() {

    }

    void delete() {

    }
}
