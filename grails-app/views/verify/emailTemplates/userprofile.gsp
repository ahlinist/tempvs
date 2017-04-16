<%@ page contentType="text/html"%>

<div><g:message code="verify.profileEmail.invitation.message" /></div>
<div>
  <g:link absolute="true" controller="verify" action="userProfile" id="${verificationCode}">
    <g:message code="verify.profileEmail.finish.link" />
  </g:link>
</div>
