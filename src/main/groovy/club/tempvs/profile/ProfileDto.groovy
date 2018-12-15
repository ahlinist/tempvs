package club.tempvs.profile

import club.tempvs.user.Profile

class ProfileDto {

    Long id
    String name
    String type
    String period

    ProfileDto(Profile profile) {
        this.id = profile.id
        this.name = profile.toString()
        this.type = profile.type.toString()
        this.period = profile.period.toString()
    }
}
