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
          <g:link class="btn btn-primary disableable pull-right" controller="auth" action="logout">
            <g:message code="auth.logout.button" />
          </g:link>
          <span class="dropdown pull-right">
            <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
              <g:message code="user.settings.button"/>
              <span class="caret"></span>
            </button>
            <ul class="dropdown-menu list-group">
              <li>
                <g:link class="list-group-item disableable" controller="user" action="edit">
                  <g:message code="user.edit.button" />
                </g:link>
              </li>
            </ul>
          </span>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
          <span class="pull-right">
            <tempvs:modalButton id="itemForm" message="auth.link">
              <g:render template="/auth/templates/loginForm"/>
            </tempvs:modalButton>
          </span>
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
