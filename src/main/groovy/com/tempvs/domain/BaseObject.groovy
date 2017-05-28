package com.tempvs.domain

import grails.validation.Validateable
import groovy.transform.CompileStatic

/**
 * Base object that is inherited by Command objects and Domain classes.
 * Includes save() and validate() methods.
 */
@CompileStatic
abstract class BaseObject implements Validateable {
}
