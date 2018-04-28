<ul class="nav nav-tabs">
  <li style="padding:0px; margin:0px;" class="col-sm-6 pull-left active">
    <a data-toggle="tab" href="#login">
      <g:message code="auth.login.link"/>
    </a>
  </li>
  <li style="padding:0px; margin:0px;" class="col-sm-6 pull-right">
    <a data-toggle="tab" href="#register">
      <g:message code="auth.register.link"/>
    </a>
  </li>
</ul>
<div class="tab-content">
  <div id="login" class="tab-pane fade in active">
    <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'auth', action: 'login']}">
      <g:render template="/common/templates/formField" model="${[type: 'email', name: 'email', label: 'auth.email.label', mandatory: true, editAllowed: true]}"/>
      <g:render template="/common/templates/formField" model="${[type: 'password', name: 'password', label: 'auth.password.label', mandatory: true, editAllowed: true]}"/>
      <g:render template="/common/templates/formField" model="${[type: 'checkbox', name: 'remember', label: 'auth.remember.label']}"/>
      <g:render template="/ajax/templates/submitButton">
        <g:message code="auth.login.button"/>
      </g:render>
    </g:render>
  </div>
  <div id="register" class="tab-pane fade">
    <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'auth', action: 'register']}">
      <g:render template="/common/templates/formField" model="${[type: 'email', name: 'email', label: 'auth.email.label', mandatory: true, editAllowed: true]}"/>
      <g:render template="/ajax/templates/submitButton">
        <g:message code="auth.request.registration.button"/>
      </g:render>
    </g:render>
  </div>
</div>
