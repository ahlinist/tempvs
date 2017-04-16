<%@ page contentType="text/html"%>

<div><g:message code="user.edit.email.verify.invitation.message" /></div>
<div>
  <g:link absolute="true" controller="verify" action="byEmail" id="${verificationCode}">
    <g:message code="user.edit.email.verify.finish.link" />
  </g:link>
</div>
