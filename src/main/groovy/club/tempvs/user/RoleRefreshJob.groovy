package club.tempvs.user

import club.tempvs.ampq.AmqpProcessor
import club.tempvs.json.JsonConverter
import org.springframework.scheduling.annotation.Scheduled

class RoleRefreshJob {

    private static final String USER_ROLES_AMQP_QUEUE = "user.roles"

    AmqpProcessor amqpProcessor
    JsonConverter jsonConverter

    @Scheduled(initialDelay = 300000L, fixedRate = 3600000L)
    def execute() {
        amqpProcessor.receive(USER_ROLES_AMQP_QUEUE) { String userRoleJson ->
            UserRolesDto userRolesDto = jsonConverter.convert(UserRolesDto.class, userRoleJson)
            User user = User.get userRolesDto.id
            List<Role> roles = Role.findAllByAuthorityInList(userRolesDto.roles)

            roles.each { Role role ->
                new UserRole(user: user, role: role).save()
            }
        }
    }
}
