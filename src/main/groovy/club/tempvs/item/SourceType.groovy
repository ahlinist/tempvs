package club.tempvs.item

import groovy.transform.CompileStatic
import org.springframework.context.i18n.LocaleContextHolder

import static grails.util.Holders.getApplicationContext

@CompileStatic
enum SourceType {
    WRITTEN,
    GRAPHIC,
    ARCHAEOLOGICAL,
    OTHER

    String getKey() {
        this.name()
    }

    String getId() {
        this.name().toLowerCase()
    }

    String getValue() {
        applicationContext.getMessage("source.sourceType.${this.id}.value", null, this.name(), LocaleContextHolder.locale)
    }
}