import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile

class BootStrap {
    def init = { servletContext ->
        User user = new User(firstName: 'Anton',
                lastName: 'Hlinisty',
                email:'anton.hlinisty@gmail.com',
                password: 'passW0rd!'.encodeAsMD5())
        user.userProfile = new UserProfile(customId: 'albvs')
        user.save()
    }

    def destroy = {
    }
}
