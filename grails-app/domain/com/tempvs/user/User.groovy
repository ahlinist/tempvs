package com.tempvs.user

import com.tempvs.domain.BasePersistent
import com.tempvs.item.ItemGroup
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * An entity that corresponds a real person logged into the system.
 */
@EqualsAndHashCode(includes='email')
@ToString(includes='email', includeNames=true, includePackage=false)
class User extends BasePersistent implements Serializable {

	private static final long serialVersionUID = 1

	String email
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	Date lastActive = new Date()
	UserService userService

	static transients = ['userService']
	static hasOne = [userProfile: UserProfile]
	static hasMany = [clubProfiles: ClubProfile]

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	List<ItemGroup> getItemGroups() {
		ItemGroup.findAllByUser(this)
	}

	static constraints = {
		password blank: false, password: true
		email email: true, unique: true, blank: false, validator: { String email, User user ->
			email ? user.userService.isEmailUnique(email) : Boolean.TRUE
		}
	}

	static mapping = {
		itemGroups batchSize: 20
	}
}
