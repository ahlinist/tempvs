<%@ page contentType="text/html"%>

<div><g:message code="verify.profileEmail.invitation.message" /></div>
<div>
  <g:link absolute="true" controller="verify" action="byEmail" id="${verificationCode}">
    <g:message code="verify.profileEmail.finish.link" />
  </g:link>
</div>
