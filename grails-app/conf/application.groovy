// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.tempvs.domain.user.User'
grails.plugin.springsecurity.userLookup.usernamePropertyName = 'email'
grails.plugin.springsecurity.userLookup.passwordPropertyName = 'password'
grails.plugin.springsecurity.auth.loginFormUrl = "/auth/login"
grails.plugin.springsecurity.failureHandler.defaultFailureUrl = "/auth/login"
grails.plugin.springsecurity.successHandler.defaultTargetUrl = "/auth/show"
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.tempvs.domain.user.UserRole'
grails.plugin.springsecurity.authority.className = 'com.tempvs.domain.user.Role'
grails.plugin.springsecurity.requestMap.className = 'com.tempvs.domain.user.Requestmap'

grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"

grails.plugin.springsecurity.interceptUrlMap = [
		[pattern: '/auth/login',     				access: ['permitAll']],
        [pattern: '/auth/register',  				access: ['permitAll']],
		[pattern: '/user/verify/**',				access: ['permitAll']],
		[pattern: '/user/createUser/**',			access: ['permitAll']],
		[pattern: '/user/show/**',   				access: ['permitAll']],
		[pattern: '/userProfile/getAvatar', 		access: ['permitAll']],
		[pattern: '/assets/**',      				access: ['permitAll']],
		[pattern: '/**/js/**',       				access: ['permitAll']],
		[pattern: '/**/css/**',      				access: ['permitAll']],
		[pattern: '/**/images/**',   				access: ['permitAll']],
		[pattern: '/**/favicon.ico', 				access: ['permitAll']],
		[pattern: '/**',             				access: ['isAuthenticated()']],
]

/*grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets*//**',      filters: 'none'],
	[pattern: '*//**//*js*//**',       filters: 'none'],
	[pattern: '*//**//*css*//**',      filters: 'none'],
	[pattern: '*//**//*images*//**',   filters: 'none'],
	[pattern: '*//**//*favicon.ico', filters: 'none'],
	[pattern: '*//**',             filters: 'JOINED_FILTERS']
]*/