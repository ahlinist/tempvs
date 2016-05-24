import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile

class BootStrap {
    def init = { servletContext ->
        new User(
                email:'anton.hlinisty@gmail.com',
                password: 'passW0rd!',
                userProfile: new UserProfile()
        ).save()
    }

    def destroy = {
    }
}
