import club.tempvs.user.Role
import club.tempvs.user.User
import club.tempvs.user.UserProfile
import club.tempvs.user.UserRole

class BootStrap {

    private static final String SCRIBE_ROLE = 'scribe'
    private static final String ARCHIVARIUS_ROLE = 'archivarius'
    private static final String ADMIN_EMAIL = 'admin@tempvs.club'
    private static final String ADMIN_FIRST_NAME = 'Tempvs'
    private static final String ADMIN_LAST_NAME = 'Admin'
    private static final String ADMIN_PASSWORD = System.getenv('ADMIN_PASSWORD') ?: 'pass'

    def init = { servletContext ->
        createAdminUser()
    }

    def destroy = {
    }

    private void createAdminUser() {
        Role archivarius = Role.findByAuthority(ARCHIVARIUS_ROLE)
        Role scribe = Role.findByAuthority(SCRIBE_ROLE)
        User admin = User.findByEmail(ADMIN_EMAIL)
        UserRole archivarius2admin = UserRole.findByUserAndRole(admin, archivarius)

        if (!archivarius) {
            archivarius = new Role(authority: ARCHIVARIUS_ROLE).save()
        }

        if (!scribe) {
            scribe = new Role(authority: SCRIBE_ROLE).save()
        }

        if (!admin) {
            UserProfile adminUserProfile = new UserProfile(firstName: ADMIN_FIRST_NAME, lastName: ADMIN_LAST_NAME, active: Boolean.FALSE)
            admin = new User(email: ADMIN_EMAIL, password: ADMIN_PASSWORD, userProfile: adminUserProfile)
            admin.save()
        }

        if (!archivarius2admin) {
            new UserRole(user: admin, role: archivarius).save()
        }
    }
}
