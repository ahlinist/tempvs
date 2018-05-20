package club.tempvs.library

import club.tempvs.user.Role
import club.tempvs.user.User
import club.tempvs.user.UserRole
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import org.springframework.security.access.prepost.PreAuthorize

@Transactional
@GrailsCompileStatic
class LibraryService {

    RoleRequest getRoleRequest(Long id) {
        RoleRequest.get id
    }

    RoleRequest loadRoleRequest(Long id) {
        RoleRequest.load id
    }

    @PreAuthorize('#user.email == authentication.name')
    RoleRequest createRoleRequest(User user, Role role) {
        RoleRequest roleRequest = new RoleRequest(user, role)
        roleRequest.save()
        roleRequest
    }

    @PreAuthorize('#user.email == authentication.name')
    void deleteRoleRequest(User user, Role role) {
        RoleRequest roleRequest = RoleRequest.findByUserAndRole(user, role)
        roleRequest.delete()
    }

    UserRole approveRoleRequest(RoleRequest roleRequest) {
        UserRole userRole = roleRequest.confirm()
        userRole.save()
        roleRequest.delete()
        return userRole
    }

    void rejectRoleRequest(RoleRequest roleRequest) {
        roleRequest.delete()
    }
}
