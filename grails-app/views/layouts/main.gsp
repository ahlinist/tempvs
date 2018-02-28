<g:set var="currentUser" value="${request.currentUser}"/>
<g:set var="currentProfile" value="${request.currentProfile}"/>
<g:set var="newFollowings" value="${request.newFollowingsCount}"/>

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
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.show.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="profile">
              <span class="glyphicon glyphicon-user"></span>
            </g:link>
          </span>
          <span class="dropdown pull-left">
            <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
              <g:set var="profileString" value="${currentProfile.toString()}"/>
              ${profileString.size() <= 30 ? profileString : profileString[0..29] + '...'}
              <span class="caret"></span>
            </button>
            <ul class="dropdown-menu list-group">
              <li>
                <g:link class="list-group-item disableable" controller="profile" action="switchProfile">
                  <g:set var="profileString" value="${currentUser.userProfile.toString()}"/>
                  ${profileString.size() <= 30 ? profileString : profileString[0..29] + '...'}
                </g:link>
              </li>
              <g:each var="clubProfile" in="${currentUser.clubProfiles.findAll {it.active}}">
                <li>
                  <g:link class="list-group-item disableable" controller="profile" action="switchProfile" id="${clubProfile.id}">
                    <g:set var="profileString" value="${clubProfile.toString()}"/>
                    ${profileString.size() <= 30 ? profileString : profileString[0..29] + '...'}
                  </g:link>
                </li>
              </g:each>
            </ul>
          </span>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.list.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="profile" action="list">
              <span class="glyphicon glyphicon-th-list"></span>
            </g:link>
          </span>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'following.list.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="following" action="show">
              <span class="fa fa-users">
                <g:if test="${newFollowings}">
                  <span class="badge badge-notify rounded" style="background-color: red; position: absolute; border-radius: 10px !important;">
                    ${newFollowings}
                  </span>
                </g:if>
              </span>
            </g:link>
          </span>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.stash.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="item" action="stash">
              <span class="glyphicon glyphicon-tent"></span>
            </g:link>
          </span>
          <span>
            <span id="profile-search-dropdown" class="dropdown" style="margin:10px;">
              <input style="width: 300px;" placeholder="${g.message(code: 'profile.search.placeholder')}" type="text" id="profile-search-box" name="query"/>
              <button id="profile-search-button" class="btn btn-default dropdown-toggle" onclick="searchProfile(this, 0);">
                <span class="glyphicon glyphicon-search"></span>
              </button>
              <div class="dropdown-menu" style="width: 300px;">
                <ul id="profile-search-result">
                </ul>
                <button id="load-more-button" class="btn btn-secondary col-sm-12" onclick="searchProfile(this, 10);">
                  <i><g:message code="profile.search.loadMore.link"/></i>
                </button>
              </div>
            </span>
            <span class="pull-right">
              <g:link class="btn btn-secondary disableable" controller="auth" action="logout">
                <g:message code="auth.logout.button"/>
              </g:link>
            </span>
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
          <span class="pull-right">
            <tempvs:modalButton id="loginForm" message="${g.message(code: 'auth.login.signup.button')}">
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
