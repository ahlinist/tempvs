package com.tempvs.domain.user

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='email')
@ToString(includes='email', includeNames=true, includePackage=false)
class User implements Serializable {

	private static final long serialVersionUID = 1

	String firstName
	String lastName
	String email
	String password
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	Date lastActive

	static hasOne = [userProfile: UserProfile]

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	static constraints = {
		password blank: false, password: true
		email email: true, unique: true, blank: false
	}

	static mapping = {
		password column: '`password`'
	}
}
