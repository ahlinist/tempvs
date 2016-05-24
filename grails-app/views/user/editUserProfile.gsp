<!DOCTYPE html>
<html>
    <head>
        <title>title</title>
    </head>
    <body>
      <div>
        <g:form action="saveUserProfile">
          <div><g:field type="text" name="firstName" placeholder="First Name" value="${userProfile.firstName}"/></div>
          <div><g:field type="text" name="lastName" placeholder="Last Name" value="${userProfile.lastName}"/></div>
          <div><g:field type="email" name="profileEmail" placeholder="Profile E-mail" value="${userProfile.profileEmail}"/> Will be displayed.</div>
          <div><g:field type="text" name="location" placeholder="Location" value="${userProfile.location}"/></div>
          <div><g:submitButton name="saveUserProfile" value="Save User Profile" /></div>
        </g:form>
      </div>
    </body>
</html>
