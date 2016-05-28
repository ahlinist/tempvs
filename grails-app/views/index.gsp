<!DOCTYPE html>
<html>
    <head>
        <title>title</title>
    </head>
    <body>
      <div>
        ${flash.message}
        <g:form controller="user">
          <div><g:field type="email" name="email" placeholder="${message(code:'user.email.placeholder')}" /></div>
          <div><g:field type="password" name="password" placeholder="${message(code:'user.password.placeholder')}"/></div>
          <div>
            <g:actionSubmit name="login" action="login" value="${message(code:'user.login.button')}" />
            <g:actionSubmit name="register" value="${message(code:'user.register.button')}" />
          </div>
        </g:form>
      </div>
    </body>
</html>
