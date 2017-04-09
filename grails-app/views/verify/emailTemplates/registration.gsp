<%@ page contentType="text/html"%>

<div><g:message code="verify.registration.invitation.message" /></div>
<div><g:message code="verify.registration.finish.message" /></div>
<div>
  <g:link absolute="true" controller="verify" action="registration" id="${verificationCode}">
    <g:message code="verify.registration.finish.link" />
  </g:link>
</div>