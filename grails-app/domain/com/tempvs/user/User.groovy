package com.tempvs.user

import com.tempvs.domain.BasePersistent
import com.tempvs.item.ItemGroup
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic
import groovy.transform.TypeCheckingMode

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
    Class currentProfileClass
    Long currentProfileId
    Collection<ClubProfile> clubProfiles
    Collection<ItemGroup> itemGroups

    transient Profile currentProfile

    static hasMany = [clubProfiles: ClubProfile, itemGroups: ItemGroup]
    static hasOne = [userProfile: UserProfile]

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    Profile getCurrentProfile() {
        if (this.currentProfileClass && this.currentProfileId) {
            this.currentProfileClass.get(this.currentProfileId)
        } else {
            this.currentProfileClass = this.userProfile.class
            this.currentProfileId = this.userProfile.id
            this.userProfile
        }
    }

    static constraints = {
        email email: true, unique: true, blank: false, size: 0..35
        currentProfileClass nullable: true
        currentProfileId nullable: true
    }

    static mapping = {
        table '`user`'
        password column: '`password`'
    }
}
