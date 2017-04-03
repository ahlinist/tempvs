<%@ page contentType="text/html"%>

<div><g:message code="user.register.verify.invitation.message" /></div>
<div><g:message code="user.register.verify.finish.message" /></div>
<div>
  <g:link absolute="true" controller="verify" action="registration" id="${verificationCode}">
    <g:message code="user.register.verify.finish.link" />
  </g:link>
</div>