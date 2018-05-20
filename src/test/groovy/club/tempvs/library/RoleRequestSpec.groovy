package club.tempvs.library

import club.tempvs.user.Role
import club.tempvs.user.User
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class RoleRequestSpec extends Specification implements DomainUnitTest<RoleRequest> {

    def user = Mock(User) {
        getAuthorities() >> []
    }

    def role = Mock Role

    def setup() {
    }

    def cleanup() {
    }

    void "Test RoleRequest creation()"() {
        expect:
        !new RoleRequest().validate()

        and:
        !new RoleRequest(user: user).validate()

        and:
        !new RoleRequest(role: role).validate()

        and:
        new RoleRequest(user: user, role: role).validate()
    }
}
