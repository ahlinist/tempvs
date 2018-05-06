package club.tempvs.library

import club.tempvs.user.Role
import club.tempvs.user.User
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import org.springframework.security.access.prepost.PreAuthorize

@Transactional
@GrailsCompileStatic
class LibraryService {

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
}
