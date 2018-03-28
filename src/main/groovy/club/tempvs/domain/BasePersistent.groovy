package club.tempvs.domain

import grails.validation.Validateable
import groovy.transform.CompileStatic

/**
 * A class inherited by all domains.
 */
@CompileStatic
trait BasePersistent extends Validateable implements Serializable {

    Date dateCreated
    Date lastUpdated
}
