<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:stylesheet src="application.css"/>
    <g:layoutHead/>
  </head>
  <body class="container">
    <div class="row">
      <sec:ifLoggedIn>
    <g:if test="${user?.userProfile?.avatar}">
      <img class="pull-left" src="${createLink(controller:'user', action:'getAvatar')}" />
    </g:if>
    <g:link class="btn btn-default pull-left" controller="user" action="show"><g:message code="user.show.profile.button" /></g:link>
    <g:link class="btn btn-default pull-left" controller="user" action="editUser"><g:message code="user.edit.button" /></g:link>
    <g:link class="btn btn-default pull-left" controller="user" action="editUserProfile"><g:message code="user.profile.edit.button" /></g:link>
    <g:link class="btn btn-primary pull-right" uri="/logoff"><g:message code="user.logout.button" /></g:link>
      </sec:ifLoggedIn>
      <sec:ifNotLoggedIn>
        <g:link class="btn btn-default pull-right" controller="user" action="register"><g:message code="user.register.link" /></g:link>
        <g:link class="btn btn-default pull-right" controller="user" action="login"><g:message code="user.login.link" /></g:link>
      </sec:ifNotLoggedIn>
    </div>

    <hr/>
    <g:layoutBody/>
    <asset:javascript src="application.js"/>
  </body>
</html>
