package club.tempvs.library

import club.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import org.springframework.security.access.annotation.Secured

@GrailsCompileStatic
@Secured('permitAll')
class LibraryController {

    static allowedMethods = [
            index: 'GET',
            admin: 'GET',
            period: 'GET',
            source: 'GET',
    ]

    Map index() {

    }

    Map period(String id) {
        if (id) {
            [period: Period.valueOf(id.toUpperCase())]
        }
    }

    @Secured('ROLE_ARCHIVARIUS')
    def admin() {

    }

    def source(Long id) {
        [sourceId: id]
    }
}
