package com.tempvs.periodization

import static grails.util.Holders.applicationContext

import groovy.transform.CompileStatic
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Enum that represents a historical period.
 */
@CompileStatic
enum Period {
    ANCIENT('periodization.ANCIENT.value', 'periodization.ancient.description'),
    ANTIQUITY('periodization.ANTIQUITY.value', 'periodization.antiquity.description'),
    MEDIEVAL('periodization.MEDIEVAL.value', 'periodization.medieval.description'),
    MODERN('periodization.MODERN.value', 'periodization.modern.description'),
    XIX('periodization.XIX.value', 'periodization.xixcentury.description'),
    CONTEMPORARY('periodization.CONTEMPORARY.value', 'periodization.contemporary.description'),
    OTHER('periodization.OTHER.value', 'periodization.other.description')

    String value
    String description

    Period(String value, String description) {
        this.value = value
        this.description = description
    }

    String getKey() {
        this.name()
    }

    String getId() {
        this.name().toLowerCase()
    }

    String getValue() {
        applicationContext.getMessage(this.value, null, this.name(), LocaleContextHolder.locale)
    }

    String getDescription() {
        applicationContext.getMessage(this.description, null, this.name(), LocaleContextHolder.locale)
    }
}
