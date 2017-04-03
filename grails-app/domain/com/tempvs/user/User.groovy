package com.tempvs.user

import com.tempvs.domain.BasePersistent
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

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

	static hasOne = [userProfile: UserProfile]
	static hisMany = [clubProfiles: ClubProfile]

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	static constraints = {
		password blank: false, password: true
		email email: true, unique: true, blank: false, validator: {email, user ->
			UserProfile userProfile = UserProfile.findByProfileEmail(email)
			!userProfile || (userProfile?.user == user)
		}
	}
}
