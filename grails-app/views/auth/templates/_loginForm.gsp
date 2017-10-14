<g:set var="timestamp" value="${new Date().time}"/>
<ul class="nav nav-tabs">
  <li style="padding:0px; margin:0px;" class="col-sm-6 pull-left active">
    <a data-toggle="tab" href="#login-${timestamp}">
      <g:message code="auth.login.link"/>
    </a>
  </li>
  <li style="padding:0px; margin:0px;" class="col-sm-6 pull-right">
    <a data-toggle="tab" href="#register-${timestamp}">
      <g:message code="auth.register.link"/>
    </a>
  </li>
</ul>
<div class="tab-content">
  <div id="login-${timestamp}" class="tab-pane fade in active">
    <tempvs:ajaxForm controller="auth" action="login">
      <tempvs:formField type="email" name="email" label="auth.email.label" />
      <tempvs:formField type="password" name="password" label="auth.password.label" />
      <tempvs:ajaxSubmitButton value="auth.login.button" />
    </tempvs:ajaxForm>
  </div>
  <div id="register-${timestamp}" class="tab-pane fade">
    <tempvs:ajaxForm controller="auth" action="register">
      <tempvs:formField type="email" name="email" label="auth.email.label" />
      <tempvs:ajaxSubmitButton value="auth.request.registration.button" />
    </tempvs:ajaxForm>
  </div>
</div>
