package club.tempvs.periodization

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
    RENAISSANCE,
    MODERN,
    WWI,
    WWII,
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

    String getShortDescription() {
        applicationContext.getMessage("periodization.${this.name()}.short.description", null, this.name(), LocaleContextHolder.locale)
    }

    String getLongDescription() {
        applicationContext.getMessage("periodization.${this.name()}.long.description", null, this.name(), LocaleContextHolder.locale)
    }
}
