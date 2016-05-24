<!DOCTYPE html>
<html>
    <head>
        <title>title</title>
    </head>
    <body>
      <div>
        ${message}
        <g:form controller="user">
          <div><g:field type="email" name="email" placeholder="E-mail" /></div>
          <div><g:field type="password" name="password" placeholder="Password"/></div>
          <div>
            <g:actionSubmit name="login" action="login" value="Login" />
            <g:actionSubmit name="register" value="Register" />
          </div>
        </g:form>
      </div>
    </body>
</html>
