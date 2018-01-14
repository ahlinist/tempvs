package com.tempvs.user

import com.tempvs.domain.BasePersistent
import com.tempvs.item.ItemGroup
import grails.compiler.GrailsCompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * An entity that corresponds a real person logged into the system.
 */
@EqualsAndHashCode(includes='email')
@ToString(includes='email', includeNames=true, includePackage=false)
@GrailsCompileStatic
class User implements BasePersistent {

	private static final long serialVersionUID = 1

    String email
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	Date lastActive = new Date()
	Collection<ClubProfile> clubProfiles
    Collection<ItemGroup> itemGroups

	static hasMany = [clubProfiles: ClubProfile, itemGroups: ItemGroup]
	static hasOne = [userProfile: UserProfile]

	Set<Role> getAuthorities() {
		List<UserRole> userRoles = UserRole.findAllByUser(this)
		userRoles*.role as Set<Role>
	}

	static constraints = {
		password blank: false, password: true
		email email: true, unique: true, blank: false
	}
}
