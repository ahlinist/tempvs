package com.tempvs.user

import com.tempvs.domain.BasePersistent
import grails.compiler.GrailsCompileStatic
import org.springframework.http.HttpMethod

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@GrailsCompileStatic
@EqualsAndHashCode(includes=['configAttribute', 'httpMethod', 'url'])
@ToString(includes=['configAttribute', 'httpMethod', 'url'], cache=true, includeNames=true, includePackage=false)
class Requestmap implements BasePersistent {

	private static final long serialVersionUID = 1

	String configAttribute
	HttpMethod httpMethod
	String url

	static constraints = {
		configAttribute blank: false
		httpMethod nullable: true
		url blank: false, unique: 'httpMethod'
	}

	static mapping = {
		cache true
	}
}
