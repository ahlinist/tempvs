package club.tempvs.user

import groovy.transform.CompileStatic

@CompileStatic
class UserDto {

    Long id
    Long userProfileId
    String userName

    UserDto(User user) {
        Profile userProfile = user.userProfile

        this.id = user.id
        this.userProfileId = userProfile.id
        this.userName = userProfile.toString()
    }
}
