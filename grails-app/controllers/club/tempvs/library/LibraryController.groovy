package club.tempvs.library

import club.tempvs.ajax.AjaxResponseHelper
import club.tempvs.item.ItemType
import club.tempvs.item.Source
import club.tempvs.item.SourceService
import club.tempvs.item.SourceType
import club.tempvs.periodization.Period
import club.tempvs.user.Role
import club.tempvs.user.User
import club.tempvs.user.UserService
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gsp.PageRenderer
import org.springframework.security.access.annotation.Secured

@GrailsCompileStatic
@Secured('isAuthenticated()')
class LibraryController {

    private static final String REPLACE_ACTION = 'replaceElement'
    private static final String OPERATION_NOT_SUPPORTED = 'library.wrong.role.message'

    static allowedMethods = [
            index: 'GET',
            period: 'GET',
            requestRole: 'POST',
            cancelRoleRequest: 'DELETE',
    ]

    UserService userService
    SourceService sourceService
    LibraryService libraryService
    PageRenderer groovyPageRenderer
    AjaxResponseHelper ajaxResponseHelper

    @Secured('permitAll')
    Map index() {
        List<String> requestedAuthorities = []

        if (userService.loggedIn) {
            List<RoleRequest> roleRequests = RoleRequest.withCriteria {
                eq("user.id", userService.currentUserId)
            } as List<RoleRequest>

            requestedAuthorities = roleRequests.role.authority
        }

        [
                periods: Period.values(),
                contributorRequested: requestedAuthorities.contains(LibraryRole.ROLE_CONTRIBUTOR.toString()),
                scribeRequested: requestedAuthorities.contains(LibraryRole.ROLE_SCRIBE.toString()),
        ]
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

    def requestRole(String id) {
        if (!LibraryRole.values().toString().contains(id)) {
            RoleRequest failedRoleRequest = new RoleRequest()
            failedRoleRequest.errors.rejectValue('role', OPERATION_NOT_SUPPORTED, null, OPERATION_NOT_SUPPORTED)
            return render(ajaxResponseHelper.renderValidationResponse(failedRoleRequest))
        }

        User user = userService.currentUser
        Role role = Role.findByAuthority(id)
        RoleRequest roleRequest = libraryService.createRoleRequest(user, role)

        if (roleRequest.hasErrors()) {
            return render(ajaxResponseHelper.renderValidationResponse(roleRequest))
        }

        Map model = [contributorRequested: Boolean.TRUE, scribeRequested: Boolean.TRUE]
        String template = groovyPageRenderer.render(template: '/library/templates/welcome', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

    def cancelRoleRequest(String id) {
        if (!LibraryRole.values().toString().contains(id)) {
            RoleRequest failedRoleRequest = new RoleRequest()
            failedRoleRequest.errors.rejectValue('role', OPERATION_NOT_SUPPORTED, null, OPERATION_NOT_SUPPORTED)
            return render(ajaxResponseHelper.renderValidationResponse(failedRoleRequest))
        }

        User user = userService.currentUser
        Role role = Role.findByAuthority(id)
        libraryService.deleteRoleRequest(user, role)

        Map model = [contributorRequested: Boolean.FALSE, scribeRequested: Boolean.FALSE]
        String template = groovyPageRenderer.render(template: '/library/templates/welcome', model: model)
        render([action: REPLACE_ACTION, template: template] as JSON)
    }

}
