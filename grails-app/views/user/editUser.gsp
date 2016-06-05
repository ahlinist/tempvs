<!DOCTYPE html>
<html>
    <head>
    <meta name="layout" content="main"/>
        <title>title</title>
    </head>
    <body>
      <%@ page import="com.tempvs.domain.user.User" %>
      <div>
        <g:form action="saveUser">
              <div><g:field type="email" name="email" placeholder="${message(code:'user.edit.email.placeholder')}" value="${user?.email}" /></div>
              <div><g:field type="password" name="password" placeholder="${message(code:'user.edit.password.placeholder')}" value="${user?.password}" /></div>
              <div><g:field type="password" name="repeatPassword" placeholder="${message(code:'user.repeatPassword.placeholder')}" value="${user instanceof User ? '' : user?.repeatPassword}" /></div>
              <div><g:field type="text" name="firstName" placeholder="${message(code:'user.edit.firstName.placeholder')}" value="${user?.firstName}" /></div>
              <div><g:field type="text" name="lastName" placeholder="${message(code:'user.edit.lastName.placeholder')}" value="${user?.lastName}" /></div>
              <g:submitButton name="saveUser" value="${message(code:'user.save.button')}" />
        </g:form>
        <g:if test="${editUserProfileFailed}">
          <div class="alert alert-danger text-center">
            <g:message code="${editUserProfileFailed}" />
          </div>
        </g:if>
        <g:if test="${userProfileUpdated}">
          <div class="alert alert-success text-center">
            <g:message code="${userProfileUpdated}" />
          </div>
        </g:if>
      </div>
    </body>
</html>
