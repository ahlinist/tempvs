package club.tempvs.item

import groovy.transform.CompileStatic
import org.springframework.context.i18n.LocaleContextHolder

import static grails.util.Holders.applicationContext

/**
 * Enum that represents item type.
 */
@CompileStatic
enum ItemType {
    CLOTHING,
    FOOTWEAR,
    HOUSEHOLD,
    WEAPON,
    ARMOR,
    OTHER

    String getKey() {
        this.name()
    }

    String getId() {
        this.name().toLowerCase()
    }

    String getValue() {
        applicationContext.getMessage("item.itemType.${this.name()}.value", null, this.name(), LocaleContextHolder.locale)
    }

    String getDescription() {
        applicationContext.getMessage("item.itemType.${this.name()}.description", null, this.name(), LocaleContextHolder.locale)
    }
}
