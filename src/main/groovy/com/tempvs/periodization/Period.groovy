package com.tempvs.periodization

import static grails.util.Holders.applicationContext

import groovy.transform.CompileStatic
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Enum that represents a historical period.
 */
@CompileStatic
enum Period {
    ANCIENT,
    ANTIQUITY,
    MEDIEVAL,
    MODERN,
    XIX,
    CONTEMPORARY,
    OTHER

    String getKey() {
        this.name()
    }

    String getId() {
        this.name().toLowerCase()
    }

    String getValue() {
        applicationContext.getMessage("periodization.${this.name()}.value", null, this.name(), LocaleContextHolder.locale)
    }

    String getDescription() {
        applicationContext.getMessage("periodization.${this.name()}.description", null, this.name(), LocaleContextHolder.locale)
    }
}
