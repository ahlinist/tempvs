<!DOCTYPE html>
<html>
    <head>
    <meta name="layout" content="main"/>
      <title>title</title>
      <style>
        form {
          margin:10px;
          padding:10px;
          border: 4px #eee groove;
        }

        input[type=submit] {
          margin-top:10px;
        }
      </style>
    </head>
    <body>
      <div class="row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-8">
          <g:uploadForm action="updateAvatar">
            <div class="row">
              <div class="col-sm-6">
                <label for="avatar"><g:message code="user.profile.avatar.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:field class="col-sm-12" name="avatar" type="file" />
              </div>
              <g:submitButton class="btn btn-primary" name="updateAvatar" value="${message(code:'user.profile.update.avatar.button')}" />
            </div>
          </g:uploadForm>
          <g:if test="${flash.avatarError}">
            <div class="alert alert-danger text-center">
              <g:message code="${flash.avatarError}" />
            </div>
          </g:if>
          <g:if test="${flash.avatarSuccess}">
            <div class="alert alert-success text-center">
              <g:message code="${flash.avatarSuccess}" />
            </div>
          </g:if>
          <g:form action="updateUserProfile">
            <div class="row">
              <div class="col-sm-6">
                <label for="firstName"><g:message code="user.profile.firstName.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:textField class="col-sm-12" name="firstName" value="${userProfile.firstName}" />
              </div>
              <div class="col-sm-6">
                <label for="lastName"><g:message code="user.profile.lastName.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:textField class="col-sm-12" name="lastName" value="${userProfile.lastName}" />
              </div>
              <div class="col-sm-6">
                <label for="email">
                  <g:message code="user.profile.profileEmail.label" />
                  (<a href="#" style="color:#3B6182;" onclick="$('input[name=profileEmail]').val('${userProfile.user.email}')">
                    <g:message code="user.profile.useLoginEmail.link"/>
                  </a>)
                </label>
              </div>
              <div class="col-sm-6">
                <g:field class="col-sm-12" type="email" name="profileEmail" value="${userProfile.profileEmail}"/>
              </div>
              <div class="col-sm-6">
                <label for="location"><g:message code="user.profile.location.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:textField class="col-sm-12" name="location" value="${userProfile.location}"/>
              </div>
              <div class="col-sm-6">
                <label for="customId"><g:message code="user.profile.customId.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:textField class="col-sm-12" name="customId" value="${userProfile.customId}"/>
              </div>
            <g:submitButton class="btn btn-primary" name="updateUserProfile" value="${message(code:'user.profile.update.button')}" />
            </div>
          </g:form>
          <g:render template="../templates/errors" model="[errorBean: userProfile]" />
          <g:render template="../templates/success" model="[success: success]" />
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>
