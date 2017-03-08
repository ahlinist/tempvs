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
      <sec:ifLoggedIn>
        <tempvs:userPic />
        <g:link class="btn btn-default disableable pull-left" controller="user" action="show"><g:message code="user.show.profile.button" /></g:link>
        <g:link class="btn btn-default disableable pull-left" controller="user" action="edit"><g:message code="user.edit.button" /></g:link>
        <g:link class="btn btn-default disableable pull-left" controller="user" action="profile"><g:message code="user.profile.edit.button" /></g:link>
        <g:link class="btn btn-primary disableable pull-right" uri="/logoff"><g:message code="auth.logout.button" /></g:link>
      </sec:ifLoggedIn>
      <sec:ifNotLoggedIn>
        <g:link class="btn btn-default disableable pull-right" controller="auth" action="register"><g:message code="auth.register.link" /></g:link>
        <g:link class="btn btn-default disableable pull-right" controller="auth" action="login"><g:message code="auth.login.link" /></g:link>
      </sec:ifNotLoggedIn>
    </div>
    <hr/>
    <g:layoutBody/>
  </body>
</html>
