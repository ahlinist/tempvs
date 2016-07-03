import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile

class BootStrap {
    def init = { servletContext ->
/*        User user = new User(firstName: 'Anton',
                lastName: 'Hlinisty',
                email:'anton.hlinisty@gmail.com',
                password: 'passW0rd!'.encodeAsMD5(),
                lastActive: new Date())
        user.userProfile = new UserProfile(customId: 'albvs')
        user.save()

        User user2 = new User(firstName: 'Anton',
                lastName: 'Hlinisty',
                email:'anton.hlinisty@tempvs.com',
                password: 'passW0rd!',
                lastActive: new Date())
        user2.userProfile = new UserProfile()
        user2.save()*/
    }

    def destroy = {
    }
}
