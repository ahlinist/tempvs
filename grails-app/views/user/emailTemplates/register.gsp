<%@ page contentType="text/html"%>

<div><g:message code="user.register.verify.invitation.message" /></div>
<div>First Name: ${firstName}</div>
<div>Last Name: ${lastName}</div>
<div><g:message code="user.register.verify.finish.message" /></div>
<div>
  <g:link absolute="true" controller="user" action="verify" id="${verificationCode}">
    <g:message code="user.register.verify.finish.link" />
  </g:link>
</div>