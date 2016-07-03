<!DOCTYPE html>
<html>
  <head>
    <title>title</title>
    <asset:stylesheet src="application.css"/>
  </head>
  <body>
  <div class="container">
    <div class="col-sm-3">
    </div>
    <div class="col-sm-6">
      <div class="row">
        <ul class="nav nav-tabs">
          <li style="padding:0px; margin:0px;" class="${registerActive ? '' : 'active'} col-sm-6 pull-left"><a data-toggle="tab" href="#login"><g:message code="user.login.tab"/></a></li>
          <li style="padding:0px; margin:0px;" class="${registerActive ? 'active' : ''} col-sm-6 pull-right"><a data-toggle="tab" href="#register"><g:message code="user.register.tab"/></a></li>
        </ul>
          <div class="tab-content">
            <div id="login" class="tab-pane fade ${registerActive ? '' : 'in active'}">
              <g:form uri="/login/authenticate" method="POST">
                <div class="col-sm-6">
                  <label for="username"><g:message code="user.email.label" /></label>
                </div>
                <div>
                  <g:field class="col-sm-6" type="email" name="username" value="${user?.email}" />
                </div>
                <div class="col-sm-6">
                  <label for="password"><g:message code="user.password.label" /></label>
                </div>
                <div>
                  <g:passwordField class="col-sm-6" name="password" value="${user?.password}" />
                </div>
                <div class="col-sm-6">
                  <label for="remember-me"><g:message code="user.login.rememberMe.label" /></label>
                </div>
                <div class="pull-right">
                  <g:checkBox name="remember-me"/>
                </div>
              <g:submitButton class="col-sm-12" name="login" value="${message(code:'user.login.button')}"/>
              </g:form>
            </div>
            <div id="register" class="tab-pane fade ${registerActive ? 'in active' : ''}">
              <g:form controller="user" action="register" method="POST">
                <div class="col-sm-6">
                  <label for="email"><g:message code="user.email.label" /></label>
                </div>
                <div>
                  <g:field class="col-sm-6" type="email" name="email" value="${user?.email}" />
                </div>
                <div class="col-sm-6">
                  <label for="password"><g:message code="user.password.label" /></label>
                </div>
                <div>
                  <g:passwordField class="col-sm-6" name="password" value="${user?.password}" />
                </div>
                <div class="col-sm-6">
                  <label for="repeatPassword"><g:message code="user.repeatPassword.label" /></label>
                </div>
                <div>
                  <g:passwordField class="col-sm-6" name="repeatPassword" value="${user?.repeatPassword}" />
                </div>
                <div class="col-sm-6">
                  <label for="firstName"><g:message code="user.firstName.label" /></label>
                </div>
                <div>
                  <g:textField class="col-sm-6" name="firstName" value="${user?.firstName}" />
                </div>
                <div class="col-sm-6">
                  <label for="lastName"><g:message code="user.lastName.label" /></label>
                </div>
                <div>
                  <g:textField class="col-sm-6" name="lastName" value="${user?.lastName}" />
                </div>
                <g:submitButton class="col-sm-12" name="register" value="${message(code:'user.register.button')}" />
              </g:form>
            </div>
              <g:if test="${registrationFailed}">
                <div class="alert alert-danger text-center">
                    <g:message code="${registrationFailed}" />
                </div>
              </g:if>
              <g:if test="${emailUsed}">
                <div class="alert alert-danger text-center">
                  <g:message code="${emailUsed}" />
                </div>
              </g:if>
              <g:if test="${loginFailed}">
                <div class="alert alert-danger text-center">
                  <g:message code="${loginFailed}" />
                </div>
              </g:if>
          </div>

      </div>
    </div>
    <div class="col-sm-3">
    </div>
  </div>

 <asset:javascript src="application.js"/>
   </body>
</html>
