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
    <header class="row" style="height:35px;">
      <sec:ifLoggedIn>
        <div class="col-sm-4">
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.show.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="profile" action=" ">
              <span class="glyphicon glyphicon-user"></span>
            </g:link>
          </span>
          <span id="profile-dropdown" class="dropdown pull-left">
            <button id="current-profile" class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
              <span id="current-profile-name"></span>
              <span class="caret"></span>
            </button>
            <ul id="profiles" class="dropdown-menu list-group">
              <li id="user-profile">
                <a class="list-group-item disableable" href=""></a>
              </li>
            </ul>
          </span>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'following.list.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="following" action=" ">
              <span class="fa fa-users">
                <span id="new-followings" class="badge badge-notify rounded hidden counter"></span>
              </span>
            </g:link>
          </span>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.stash.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="item" action="stash">
              <span class="glyphicon glyphicon-tent"></span>
            </g:link>
          </span>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'message.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="message" action=" ">
              <span class="fa fa-envelope">
                <span id="new-conversations" class="badge badge-notify rounded hidden counter"></span>
              </span>
            </g:link>
          </span>
        </div>
        <div class="col-sm-4">
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
        </div>
        <div class="col-sm-4">
          <span class="pull-right">
            <g:render template="/auth/templates/logoutButton"/>
          </span>
          <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'settings.tooltip')}">
            <g:link class="btn btn-default disableable" controller="user" action="edit">
              <span class="glyphicon glyphicon-cog"></span>
            </g:link>
          </span>
          <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'library.tooltip')}">
            <g:link class="btn btn-default disableable pull-right" controller="library" action=" ">
              <span class="glyphicon glyphicon-book"></span>
            </g:link>
          </span>
        </div>
      </sec:ifLoggedIn>
      <sec:ifNotLoggedIn>
        <div class="col-sm-4"></div>
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
          <span class="pull-right">
            <g:render template="/common/templates/modalButton"
                model="${[id: 'loginForm', message: g.message(code: 'auth.login.signup.button')]}">
              <g:render template="/auth/templates/loginForm"/>
            </g:render>
          </span>
        </div>
      </sec:ifNotLoggedIn>
    </header>
    <hr/>
    <main>
      <g:layoutBody/>
    </main>
    <footer class="navbar-fixed-bottom text-center text-muted col-sm-12" style="height:35px;">
      <hr/>
      <g:message code="tempvs.footer.message"/>
    </footer>
  </body>
</html>
