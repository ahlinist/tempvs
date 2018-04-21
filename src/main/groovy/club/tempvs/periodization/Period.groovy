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
    EARLY_MIDDLE_AGES,
    HIGH_MIDDLE_AGES,
    LATE_MIDDLE_AGES,
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
        applicationContext.getMessage("periodization.${this.id}.value", null, this.name(), LocaleContextHolder.locale)
    }
}
