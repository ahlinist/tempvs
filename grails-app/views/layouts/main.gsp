<g:set var="user" value="${applicationContext.userService.currentUser}"/>
<g:set var="currentProfile" value="${applicationContext.profileHolder.profile}"/>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="location" content="/${controllerName}/${actionName}" />
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <title><g:layoutTitle default="Tempvs" /></title>
    <g:layoutHead/>
  </head>
  <body class="container">
    <div class="row">
      <header>
        <sec:ifLoggedIn>
          <g:render template="/templates/navigation/switchProfile" model="${[user, currentProfile]}"/>
          <g:link class="btn btn-primary disableable pull-right" uri="/logoff">
            <g:message code="auth.logout.button" />
          </g:link>
          <g:render template="/templates/navigation/settings"/>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
          <g:link class="btn btn-default disableable pull-right" controller="auth">
            <g:message code="auth.link" />
          </g:link>
        </sec:ifNotLoggedIn>
      </header>
    </div>
    <hr/>
    <div class="row">
      <div class="col-sm-2">
        <sec:ifLoggedIn>
          <g:render template="/templates/navigation/menu"/>
        </sec:ifLoggedIn>
      </div>
      <div class="col-sm-10">
        <g:layoutBody/>
      </div>
    </div>
  </body>
</html>
