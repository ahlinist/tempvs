<!DOCTYPE html>
<html>
  <head>
    <title>title</title>
    <asset:stylesheet src="application.css"/>
  </head>
  <body>
  <g:if test="${flash.error}">
    ${flash.error}
  <g:if>
  <div class="container">
    <div class="col-sm-4">
    </div>
    <div class="col-sm-4">
      <div class="row">
        <ul class="nav nav-tabs">
          <li style="padding:0px; margin:0px;" class="active col-sm-6 pull-left"><a data-toggle="tab" href="#login"><g:message code="user.login.tab"/></a></li>
          <li style="padding:0px; margin:0px;" class="col-sm-6 pull-right"><a data-toggle="tab" href="#register"><g:message code="user.register.tab"/></a></li>
        </ul>
        <g:form controller="user">
          <div class="tab-content">
            <div>
              <g:field class="col-sm-12" type="email" name="email" placeholder="${message(code:'user.email.placeholder')}" />
              <g:if test="${flash.noEmail}">
                ${flash.noEmail}
              </g:if>
            </div>
            <div>
              <g:field class="col-sm-12"  type="password" name="password" placeholder="${message(code:'user.password.placeholder')}"/>
              <g:if test="${flash.noPassword}">
                ${flash.noPassword}
              </g:if>
            </div>
            <div id="login" class="tab-pane fade in active">
              <g:actionSubmit class="col-sm-12" name="login" action="login" value="${message(code:'user.login.button')}" />
            </div>
            <div id="register" class="tab-pane fade">
              <div>
                <g:field class="col-sm-12"  type="password" name="repeatPassword" placeholder="${message(code:'user.repeatPassword.placeholder')}"/>
                <g:if test="${flash.noPasswordRepeat}">
                  ${flash.noPasswordRepeat}
                </g:if>
              </div>
              <div>
                <g:field class="col-sm-12"  type="text" name="firstName" placeholder="${message(code:'user.profile.firstName.placeholder')}"/>
                <g:if test="${flash.noFirstName}">
                  ${flash.noFirstName}
                </g:if>
              </div>
              <div>
                <g:field class="col-sm-12"  type="text" name="lastName" placeholder="${message(code:'user.profile.lastName.placeholder')}"/>
                <g:if test="${flash.noLastName}">
                  ${flash.noLastName}
                </g:if>
              </div>
              <g:actionSubmit class="col-sm-12" name="register" value="${message(code:'user.register.button')}" />
            </div>
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
