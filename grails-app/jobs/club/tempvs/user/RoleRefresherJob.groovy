package club.tempvs.user

import club.tempvs.ampq.AmqpProcessor
import club.tempvs.json.JsonConverter
import org.springframework.beans.factory.annotation.Value

class RoleRefresherJob {

    private static final String USER_ROLES_AMQP_QUEUE = "user.roles"

    static triggers = {
        //simple repeatInterval: 3600000l //run hourly
        simple repeatInterval: 60000l
    }

    @Value('${amqp.enabled}')
    private boolean amqpEnabled

    AmqpProcessor amqpProcessor
    JsonConverter jsonConverter

    def execute() {
        if (amqpEnabled) {
            amqpProcessor.receive(USER_ROLES_AMQP_QUEUE, action)
        }
    }

    private Closure getAction() {
        return { String userRoleJson ->
            UserRolesDto userRolesDto = jsonConverter.convert(UserRolesDto.class, userRoleJson)
            User user = User.get userRolesDto.id
            List<Role> roles = Role.findAllByAuthorityInList(userRolesDto.roles)

            roles.each { Role role ->
                new UserRole(user: user, role: role).save()
            }
        }
    }
}
