package club.tempvs.library

import club.tempvs.user.Role
import club.tempvs.user.User
import club.tempvs.user.UserRole
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@Mock(RoleRequest)
@TestFor(LibraryService)
class LibraryServiceSpec extends Specification {

    def user = Mock(User) {
        getAuthorities() >> []
    }

    def role = Mock Role
    def userRole = Mock UserRole
    def roleRequest = Mock RoleRequest

    def setup() {
        GroovySpy(RoleRequest, global:true)
    }

    def cleanup() {
    }

    void "Test createRoleRequest()"() {
        expect:
        service.createRoleRequest(user, role) instanceof RoleRequest
    }

    void "Test deleteRoleRequest()"() {
        when:
        service.deleteRoleRequest(user, role)

        then:
        1 * RoleRequest.findByUserAndRole(user, role) >> roleRequest
        1 * roleRequest.delete()
        0 * _
    }

    void "Test approveRoleRequest()"() {
        when:
        def result = service.approveRoleRequest(roleRequest)

        then:
        1 * roleRequest.confirm() >> userRole
        1 * userRole.save() >> userRole
        1 * roleRequest.delete()
        0 * _

        and:
        result == userRole
    }

    void "Test rejectRoleRequest()"() {
        when:
        service.rejectRoleRequest(roleRequest)

        then:
        1 * roleRequest.delete()
        0 * _
    }
}
