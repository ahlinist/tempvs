package club.tempvs.library

import club.tempvs.item.ItemType
import club.tempvs.item.Source
import club.tempvs.item.SourceService
import club.tempvs.item.SourceType
import club.tempvs.periodization.Period
import club.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import org.springframework.security.access.annotation.Secured

@GrailsCompileStatic
@Secured('isAuthenticated()')
class LibraryController {

    static allowedMethods = [
            index: 'GET',
            admin: 'GET',
            period: 'GET',
    ]

    UserService userService
    SourceService sourceService

    @Secured('permitAll')
    Map index() {

    }

    @Secured('permitAll')
    Map period(String id) {
        if (id) {
            Period period = Period.valueOf(id.toUpperCase())
            List<Source> sources = sourceService.getSourcesByPeriod(period)

            [
                    sources: sources,
                    period: period,
                    itemTypes: ItemType.values(),
                    sourceTypes: SourceType.values(),
            ]
        }
    }

    @Secured('ROLE_ARCHIVARIUS')
    def admin() {

    }
}
