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
      <g:set var="userProfile" value="${user.userProfile}" />
      <div class="row">
        <div class="col-sm-2">
        </div>
        <div class="col-sm-8">
          <g:uploadForm action="updateAvatar" onsubmit="sendAjaxRequest(this); return false;">
            <div class="row">
              <div class="col-sm-6">
                <label for="avatar"><g:message code="user.profile.avatar.label" /></label>
              </div>
              <div class="col-sm-6">
                <g:field class="col-sm-12" name="avatar" type="file" />
              </div>
            </div>
            <g:render template="../templates/ajaxTools" model="[button: 'user.profile.update.avatar.button']" />
          </g:uploadForm>
          <g:form action="updateUserProfile" onsubmit="sendAjaxRequest(this); return false;">
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
            </div>
            <g:render template="../templates/ajaxTools" model="[button: 'user.profile.update.button']" />
          </g:form>
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>
