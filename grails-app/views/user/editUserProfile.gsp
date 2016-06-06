<!DOCTYPE html>
<html>
    <head>
    <meta name="layout" content="main"/>
        <title>title</title>
    </head>
    <body>
      <div>
        <g:form action="updateUserProfile">
          <div><g:field type="email" name="profileEmail" placeholder="${message(code:'user.profile.profileEmail.placeholder')}" value="${userProfile.profileEmail}"/></div>
          <div><g:field type="text" name="location" placeholder="${message(code:'user.profile.location.placeholder')}" value="${userProfile.location}"/></div>
          <div><g:field type="text" name="customId" placeholder="${message(code:'user.profile.customId.placeholder')}" value="${userProfile.customId}"/></div>
          <div><g:submitButton name="updateUserProfile" value="${message(code:'user.profile.save.button')}" /></div>
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
