<!DOCTYPE html>
<html>
    <head>
        <title>title</title>
    </head>
    <body>
    ${message}
    <g:if test="${user}">
      <div>
        <div>${user.email}</div>
      </div>
      <div>
        <g:if test="${session.user && (session.user.id == id)}">
          <g:link action="editUserProfile">Edit User Profile</g:link>
        </g:if>
      </div>
    </g:if>
    <g:if test="${session.user}">
      <div>
        <g:link action="logout">logout</g:link>
      </div>
    </g:if>
    </body>
</html>
