<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Auth</title>
  </head>
  <body>
    <div class="container" style="margin-top: 7em;">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <div class="text-center">
          <h1><g:message code="auth.header.message"/></h1>
          <br/>
          <g:message code="auth.greetings.message"/>
          <br/>
          <sec:ifNotLoggedIn>
            <g:message code="auth.enlist.message.start"/>
            <g:render template="/common/templates/modalButton"
                model="${[elementId: 'loginForm', message: 'auth.login.signup.button']}">
              <g:render template="/auth/templates/loginForm"/>
            </g:render>
            <g:message code="auth.enlist.message.end"/>
          </sec:ifNotLoggedIn>
          <sec:ifLoggedIn>
            <g:message code="auth.logout.message.start"/>
            <g:render template="/auth/templates/logoutButton"/>
            <g:message code="auth.logout.message.end"/>
          </sec:ifLoggedIn>
        </div>
      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
