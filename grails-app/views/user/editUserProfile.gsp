<!DOCTYPE html>
<html>
    <head>
    <meta name="layout" content="main"/>
      <title>title</title>
      <style>
        form {
          margin:10px;
          padding:10px;
          border: 4px #eee groove;
        }

        input[type=submit] {
          margin-top:10px;
        }
      </style>
    </head>
    <body>
      <div>
        <g:uploadForm action="updateAvatar">
          <div><g:field name="avatar" type="file" /></div>
          <div><g:submitButton class="btn btn-primary" name="updateAvatar" value="${message(code:'user.profile.update.avatar.button')}" /></div>
        </g:uploadForm>
        <g:if test="${flash.avatarError}">
          <div class="alert alert-danger text-center">
            <g:message code="${flash.avatarError}" />
          </div>
        </g:if>
        <g:if test="${flash.avatarSuccess}">
          <div class="alert alert-success text-center">
            <g:message code="${flash.avatarSuccess}" />
          </div>
        </g:if>
        <g:form action="updateUserProfile">
          <div><g:field type="email" name="profileEmail" placeholder="${message(code:'user.profile.profileEmail.placeholder')}" value="${user.userProfile.profileEmail}"/></div>
          <div><g:field type="text" name="location" placeholder="${message(code:'user.profile.location.placeholder')}" value="${user.userProfile.location}"/></div>
          <div><g:field type="text" name="customId" placeholder="${message(code:'user.profile.customId.placeholder')}" value="${user.userProfile.customId}"/></div>
          <div><g:submitButton class="btn btn-primary" name="updateUserProfile" value="${message(code:'user.profile.update.button')}" /></div>
        </g:form>
        <g:if test="${flash.error}">
          <div class="alert alert-danger text-center">
            <g:message code="${flash.error}" />
          </div>
        </g:if>
        <g:if test="${flash.success}">
          <div class="alert alert-success text-center">
            <g:message code="${flash.success}" />
          </div>
        </g:if>
      </div>
    </body>
</html>
