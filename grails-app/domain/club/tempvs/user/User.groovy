package club.tempvs.user

import club.tempvs.domain.BasePersistent
import club.tempvs.item.ItemGroup
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='email')
@ToString(includes='email', includeNames=true, includePackage=false)
class User implements BasePersistent {

    private static final long serialVersionUID = 1

    String email
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    Date lastActive = new Date()
    Long currentProfileId
    List<Profile> profiles
    List<ItemGroup> itemGroups

    transient Profile currentProfile

    static hasMany = [itemGroups: ItemGroup, profiles: Profile]

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    Profile getCurrentProfile() {
        if (this.currentProfileId) {
            Profile.get(this.currentProfileId)
        } else {
            Profile userProfile = this.profiles.find { it.type == ProfileType.USER }
            this.currentProfileId = userProfile.id
            userProfile
        }
    }

    static constraints = {
        email email: true, unique: true, blank: false, size: 0..35
        currentProfileId nullable: true
    }

    static mapping = {
        table '`user`'
        password column: '`password`'
    }
}
