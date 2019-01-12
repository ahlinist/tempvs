import club.tempvs.image.Image
import club.tempvs.rest.RestCaller
import club.tempvs.rest.RestResponse
import club.tempvs.user.Profile
import club.tempvs.user.ProfileType
import club.tempvs.user.Role
import club.tempvs.user.User
import club.tempvs.user.UserRole
import com.netflix.discovery.EurekaClient
import grails.converters.JSON
import groovy.util.logging.Slf4j
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@Slf4j
class BootStrap {

    private static final String ROLE_ADMIN = 'ROLE_ADMIN'
    private static final String ROLE_SCRIBE = 'ROLE_SCRIBE'
    private static final String ROLE_ARCHIVARIUS = 'ROLE_ARCHIVARIUS'
    private static final String ROLE_CONTRIBUTOR = 'ROLE_CONTRIBUTOR'
    private static final String ADMIN_EMAIL = 'admin@tempvs.club'
    private static final String ADMIN_FIRST_NAME = 'Tempvs'
    private static final String ADMIN_PASSWORD = System.getenv('ADMIN_PASSWORD') ?: 'adminPassword'

    RestCaller restCaller
    EurekaClient eurekaClient

    def init = { servletContext ->
        log.info 'Starting tempvs: ...'
        createRoles()
        createAdminUser()
        pingMicroService 'email'
        pingMicroService 'image'
        pingMicroService 'message'
        registerCustomJSONMarshallers()
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
            Profile adminUserProfile = new Profile(firstName: ADMIN_FIRST_NAME, type: ProfileType.USER, active: Boolean.FALSE)
            adminUser = new User(email: ADMIN_EMAIL, password: ADMIN_PASSWORD)
            adminUser.addToProfiles adminUserProfile
            adminUser.save()
        }

        if (!adminRole2adminUser) {
            new UserRole(user: adminUser, role: admin).save()
        }
    }

    private void registerCustomJSONMarshallers() {
        JSON.registerObjectMarshaller(Image) { Image image ->
            [
                    objectId:   image.objectId,
                    collection: image.collection,
                    imageInfo:  image.imageInfo,
            ]
        }
    }

    private pingMicroService(String serviceName) {
        new Thread(serviceName){
            void run(){
                String serviceUrl = eurekaClient.getApplication(serviceName)?.instances?.find()?.homePageUrl
                String pingUrl = "${serviceUrl}/api/ping"
                RestResponse response = restCaller.call(pingUrl, HttpMethod.GET)

                if (!response) {
                    log.error "${serviceName} service is down"
                } else if (response.statusCode == HttpStatus.OK) {
                    log.info "${serviceName} service is up and running at '${serviceUrl}'"
                } else {
                    log.error "${serviceName} service returns status code '${response.statusCode}' for URL: '${pingUrl}'"
                }
            }
        }.start()
    }
}
