package club.tempvs.library

import club.tempvs.item.ItemType
import club.tempvs.item.Source
import club.tempvs.item.SourceService
import club.tempvs.item.SourceType
import club.tempvs.periodization.Period
import club.tempvs.user.Role
import club.tempvs.user.User
import club.tempvs.user.UserRole
import club.tempvs.user.UserService
import grails.gsp.PageRenderer
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class LibraryControllerSpec extends Specification implements ControllerUnitTest<LibraryController>, DataTest {

    private static final String GET_METHOD = 'GET'
    private static final String POST_METHOD = 'POST'
    private static final String DELETE_METHOD = 'DELETE'

    def user = Mock User
    def role = Mock Role
    def source = Mock Source
    def period = Period.OTHER
    def userRole = Mock UserRole
    def roleRequest = Mock RoleRequest

    def userService = Mock UserService
    def sourceService = Mock SourceService
    def libraryService = Mock LibraryService
    def groovyPageRenderer = Mock PageRenderer

    Class<?>[] getDomainClassesToMock(){
        return [RoleRequest] as Class[]
    }

    def setup() {
        controller.userService = userService
        controller.sourceService = sourceService
        controller.libraryService = libraryService
        controller.groovyPageRenderer = groovyPageRenderer

        GroovySpy(Role, global:true)
        GroovySpy(RoleRequest, global:true)
    }

    def cleanup() {
    }

    void "Test index()"() {
        given:
        request.method = GET_METHOD

        when:
        def result = controller.index()

        then:
        1 * userService.isLoggedIn() >> Boolean.FALSE
        0 * _

        and:
        !response.redirectedUrl
        !controller.modelAndView

        result == [
                periods: Period.values(),
                contributorRequested: Boolean.FALSE,
                contributorObtained: Boolean.FALSE,
                scribeRequested: Boolean.FALSE,
                scribeObtained: Boolean.FALSE,
        ]
    }

    void "Test period()"() {
        given:
        params.id = period.id
        request.method = GET_METHOD
        List<Source> sources = [source]

        when:
        def result = controller.period()

        then:
        1 * sourceService.getSourcesByPeriod(period) >> sources
        0 * _

        and:
        result == [sources: sources, period: period, itemTypes: ItemType.values(), sourceTypes: SourceType.values()]
    }

    void "Test requestRole()"() {
        given:
        request.method = POST_METHOD

        when:
        controller.requestRole(LibraryRole.ROLE_CONTRIBUTOR.toString())

        then:
        1 * userService.currentUser >> user
        1 * Role.findByAuthority(LibraryRole.ROLE_CONTRIBUTOR.toString()) >> role
        1 * libraryService.createRoleRequest(user, role) >> roleRequest
        1 * roleRequest.hasErrors() >> Boolean.FALSE
        1 * groovyPageRenderer.render(_ as Map)
        0 * _
    }

    void "Test cancelRoleRequest()"() {
        given:
        request.method = DELETE_METHOD

        when:
        controller.cancelRoleRequest(LibraryRole.ROLE_CONTRIBUTOR.toString())

        then:
        1 * userService.currentUser >> user
        1 * Role.findByAuthority(LibraryRole.ROLE_CONTRIBUTOR.toString()) >> role
        1 * libraryService.deleteRoleRequest(user, role)
        1 * groovyPageRenderer.render(_ as Map)
        0 * _
    }

    void "Test admin()"() {
        given:
        request.method = GET_METHOD

        expect:
        controller.admin() == [roleRequests: []]
    }

    void "Test approveRoleRequest()"() {
        given:
        params.id = 1L
        request.method = POST_METHOD

        when:
        controller.approveRoleRequest()

        then:
        1 * libraryService.getRoleRequest(1L) >> roleRequest
        1 * libraryService.approveRoleRequest(roleRequest) >> userRole
        1 * userRole.hasErrors() >> Boolean.FALSE
        0 * _
    }

    void "Test rejectRoleRequest()"() {
        given:
        params.id = 1L
        request.method = DELETE_METHOD

        when:
        controller.rejectRoleRequest()

        then:
        1 * libraryService.loadRoleRequest(1L) >> roleRequest
        1 * libraryService.rejectRoleRequest(roleRequest)
        0 * _
    }
}
