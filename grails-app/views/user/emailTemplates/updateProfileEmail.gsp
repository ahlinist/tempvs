<%@ page contentType="text/html"%>

<div><g:message code="user.edit.profileEmail.verify.invitation.message" /></div>
<div>
  <g:link absolute="true" controller="user" action="verify" id="${verificationCode}">
    <g:message code="user.edit.profileEmail.verify.finish.link" />
  </g:link>
</div>