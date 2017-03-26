<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Auth</title>
  </head>
  <body>
    <div class="container">
      <div class="col-sm-3">
      </div>
      <div class="col-sm-6">
        <div class="row">
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
              <tempvs:ajaxForm action="login">
                <tempvs:formField type="email" name="email" label="auth.email.label" />
                <tempvs:formField type="password" name="password" label="auth.password.label" />
                <tempvs:ajaxSubmitButton value="auth.login.button" />
              </tempvs:ajaxForm>
            </div>
            <div id="register" class="tab-pane fade">
              <tempvs:ajaxForm action="register">
                <tempvs:formField type="email" name="email" label="auth.email.label" />
                <tempvs:ajaxSubmitButton value="auth.request.registration.button" />
              </tempvs:ajaxForm>
            </div>
          </div>
        </div>

      </div>
      <div class="col-sm-3">
      </div>
    </div>
  </body>
</html>
