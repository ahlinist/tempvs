<!DOCTYPE html>
<html>
    <head>
        <title>title</title>
    </head>
    <body>
      <div>
        <g:form action="saveUserProfile">
          <div><g:field type="text" name="firstName" placeholder="${message(code:'user.profile.firstName.placeholder')}" value="${userProfile.firstName}"/></div>
          <div><g:field type="text" name="lastName" placeholder="${message(code:'user.profile.lastName.placeholder')}" value="${userProfile.lastName}"/></div>
          <div><g:field type="email" name="profileEmail" placeholder="${message(code:'user.profile.profileEmail.placeholder')}" value="${userProfile.profileEmail}"/></div>
          <div><g:field type="text" name="location" placeholder="${message(code:'user.profile.location.placeholder')}" value="${userProfile.location}"/></div>
          <div><g:submitButton name="saveUserProfile" value="${message(code:'user.profile.save.button')}" /></div>
        </g:form>
      </div>
    </body>
</html>
