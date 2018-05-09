package club.tempvs.library

import club.tempvs.domain.BasePersistent
import club.tempvs.user.Role
import club.tempvs.user.User
import club.tempvs.user.UserRole
import grails.compiler.GrailsCompileStatic

/**
 * Instance of this entity is created and persisted when a {@link club.tempvs.user.User} has requested a role.
 */
@GrailsCompileStatic
class RoleRequest implements BasePersistent {

    User user
    Role role

    RoleRequest(User user, Role role) {
        this.user = user
        this.role = role
    }

    UserRole confirm() {
        UserRole.create(user, role)
    }

    static constraints = {
        role unique: ['user']
        user validator: { User user, RoleRequest roleRequest ->
            !user.authorities.contains(roleRequest.role)
        }
    }
}
