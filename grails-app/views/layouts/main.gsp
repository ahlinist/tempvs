<g:set var="currentUser" value="${applicationContext.userService.currentUser}"/>

<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta name="location" content="/${controllerName}/${actionName}" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <title><g:layoutTitle default="Tempvs" /></title>
    <g:layoutHead/>
  </head>
  <body class="container">
    <div class="row">
      <header>
        <sec:ifLoggedIn>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.show.button')}">
            <g:link class="btn btn-secondary disableable" controller="profile">
              <span class="glyphicon glyphicon-user"></span>
            </g:link>
          </span>
          <span class="dropdown pull-left">
            <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
              ${applicationContext.profileHolder.profile}
              <span class="caret"></span>
            </button>
            <ul class="dropdown-menu list-group">
              <li>
                <g:link class="list-group-item disableable" controller="profile" action="switchProfile">
                  ${currentUser.userProfile}
                </g:link>
              </li>
              <g:each var="clubProfile" in="${currentUser.clubProfiles}">
                <li>
                  <g:link class="list-group-item disableable" controller="profile" action="switchProfile" id="${clubProfile.id}">
                    ${clubProfile}
                  </g:link>
                </li>
              </g:each>
            </ul>
          </span>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.stash.button')}">
            <g:link class="btn btn-secondary disableable" controller="item" action="stash">
              <span class="glyphicon glyphicon-tent"></span>
            </g:link>
          </span>
          <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'auth.logout.button')}">
            <g:link class="btn btn-secondary disableable" controller="auth" action="logout">
              <span class="glyphicon glyphicon-log-out"></span>
            </g:link>
          </span>
          <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'settings.tooltip')}">
            <g:link class="btn btn-default disableable" controller="user" action="edit">
              <span class="glyphicon glyphicon-cog"></span>
            </g:link>
          </span>
          <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.library.tooltip')}">
            <g:link class="btn btn-default disableable pull-right" controller="source">
              <span class="glyphicon glyphicon-book"></span>
            </g:link>
          </span>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
          <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'auth.link.tooltip')}">
            <tempvs:modalButton id="loginForm" classes="glyphicon glyphicon-log-in">
              <g:render template="/auth/templates/loginForm"/>
            </tempvs:modalButton>
          </span>
        </sec:ifNotLoggedIn>
      </header>
    </div>
    <hr/>
    <div class="row">
      <g:layoutBody/>
    </div>
  </body>
</html>
