<!DOCTYPE html>
<html>
  <head>
    <title>title</title>
    <asset:stylesheet src="application.css"/>
  </head>
  <body>
  <div class="container">
    <div class="col-sm-4">
    </div>
    <div class="col-sm-4">
      <div class="row">
        <ul class="nav nav-tabs">
          <li style="padding:0px; margin:0px;" class="${registerActive ? '' : 'active'} col-sm-6 pull-left"><a data-toggle="tab" href="#login"><g:message code="user.login.tab"/></a></li>
          <li style="padding:0px; margin:0px;" class="${registerActive ? 'active' : ''} col-sm-6 pull-right"><a data-toggle="tab" href="#register"><g:message code="user.register.tab"/></a></li>
        </ul>
        <g:form controller="user">
          <div class="tab-content">
            <div>
              <g:field class="col-sm-12" type="email" name="email" placeholder="${message(code:'user.email.placeholder')}" value="${user?.email}" />
            </div>
            <div>
              <g:passwordField class="col-sm-12" name="password" placeholder="${message(code:'user.password.placeholder')}" value="${user?.password}" />
            </div>
            <div id="login" class="tab-pane fade ${registerActive ? '' : 'in active'}">
              <g:actionSubmit class="col-sm-12" name="login" action="login" value="${message(code:'user.login.button')}" />
            </div>
            <div id="register" class="tab-pane fade ${registerActive ? 'in active' : ''}">
              <div>
                <g:passwordField class="col-sm-12" name="repeatPassword" placeholder="${message(code:'user.repeatPassword.placeholder')}" value="${user?.repeatPassword}" />
              </div>
              <div>
                <g:textField class="col-sm-12" name="firstName" placeholder="${message(code:'user.firstName.placeholder')}" value="${user?.firstName}" />
              </div>
              <div>
                <g:textField class="col-sm-12" name="lastName" placeholder="${message(code:'user.lastName.placeholder')}" value="${user?.lastName}" />
              </div>
              <g:actionSubmit class="col-sm-12" name="register" action="register" value="${message(code:'user.register.button')}" />
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
        </g:form>
      </div>
    </div>
    <div class="col-sm-4">
    </div>
  </div>

 <asset:javascript src="application.js"/>
   </body>
</html>
