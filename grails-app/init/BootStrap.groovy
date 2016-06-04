import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile

class BootStrap {
    def init = { servletContext ->
        UserProfile userProfile = new UserProfile(firstName: 'Anton', lastName: 'Hlinisty', customId: 'albvs')
        new User(email:'anton.hlinisty@gmail.com', password: 'passW0rd!',userProfile: userProfile).save()
    }

    def destroy = {
    }
}
