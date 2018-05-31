import club.tempvs.user.Role
import club.tempvs.user.User
import club.tempvs.user.UserProfile
import club.tempvs.user.UserRole

class BootStrap {

    private static final String ROLE_ADMIN = 'ROLE_ADMIN'
    private static final String ROLE_SCRIBE = 'ROLE_SCRIBE'
    private static final String ROLE_ARCHIVARIUS = 'ROLE_ARCHIVARIUS'
    private static final String ROLE_CONTRIBUTOR = 'ROLE_CONTRIBUTOR'
    private static final String ADMIN_EMAIL = 'admin@tempvs.club'
    private static final String ADMIN_FIRST_NAME = 'Tempvs'
    private static final String ADMIN_LAST_NAME = 'Admin'
    private static final String ADMIN_PASSWORD = System.getenv('ADMIN_PASSWORD') ?: 'adminPassword'

    def restCallService

    def init = { servletContext ->
        String emailServiceUrl = System.getenv("EMAIL_SERVICE_URL")

        createRoles()
        createAdminUser()

        if (emailServiceUrl) {
            restCallService.doGet(emailServiceUrl + "/api/ping")
        }
    }

    def destroy = {
    }

    private void createRoles() {
        Role archivarius = Role.findByAuthority(ROLE_ARCHIVARIUS)
        Role scribe = Role.findByAuthority(ROLE_SCRIBE)
        Role admin = Role.findByAuthority(ROLE_ADMIN)
        Role contributor = Role.findByAuthority(ROLE_CONTRIBUTOR)

        if (!admin) {
            new Role(authority: ROLE_ADMIN).save(flush: true)
        }

        if (!archivarius) {
            new Role(authority: ROLE_ARCHIVARIUS).save()
        }

        if (!scribe) {
            new Role(authority: ROLE_SCRIBE).save()
        }

        if (!contributor) {
            new Role(authority: ROLE_CONTRIBUTOR).save()
        }
    }

    private void createAdminUser() {
        Role admin = Role.findByAuthority(ROLE_ADMIN)
        User adminUser = User.findByEmail(ADMIN_EMAIL)
        UserRole adminRole2adminUser = UserRole.findByUserAndRole(adminUser, admin)

        if (!adminUser) {
            UserProfile adminUserProfile = new UserProfile(firstName: ADMIN_FIRST_NAME, lastName: ADMIN_LAST_NAME, active: Boolean.FALSE)
            adminUser = new User(email: ADMIN_EMAIL, password: ADMIN_PASSWORD, userProfile: adminUserProfile)
            adminUser.save()
        }

        if (!adminRole2adminUser) {
            new UserRole(user: adminUser, role: admin).save()
        }
    }
}
