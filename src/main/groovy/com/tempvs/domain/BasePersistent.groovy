package com.tempvs.domain

import grails.validation.Validateable
import groovy.transform.CompileStatic

/**
 * Class that is inherited by all domains.
 */
@CompileStatic
abstract class BasePersistent implements Validateable {
    Date dateCreated
    Date lastUpdated
}
