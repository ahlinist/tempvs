<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Edit ${user.email}</title>
  </head>
  <body>
    <div class="row">
      <div class="col-sm-3"></div>
      <div class="col-sm-6">
        <div class="ajax-form">
          <tempvs:ajaxSmartForm type="text" action="updateEmail" name="email" value="${user.email}" label="user.edit.email.label" editAllowed="${true}"/>
        </div>
        <tempvs:ajaxForm action="updatePassword">
          <tempvs:formField type="password" name="currentPassword" label="user.edit.currentPassword.label" />
          <tempvs:formField type="password" name="newPassword" label="user.edit.newPassword.label" />
          <tempvs:formField type="password" name="repeatNewPassword" label="user.edit.repeatNewPassword.label" />
          <tempvs:ajaxSubmitButton value="user.edit.password.button" />
        </tempvs:ajaxForm>
        <div class="col-sm-3"></div>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-4"></div>
      <div class="col-sm-4">
        <label>
          <g:message code="userProfile.list.message"/>
        </label>
        <g:link class="list-group-item" controller="profile" action="userProfile" id="${userProfile.id}">
          ${userProfile}
        </g:link>
        <g:if test="${clubProfiles}">
          <label>
            <g:message code="clubProfile.list.message"/>
          </label>
          <ul>
            <g:each var="clubProfile" in="${clubProfiles}">
              <g:set var="clubProfileId" value="${clubProfile.id}"/>
              <li class="row">
                <g:link class="btn btn-default col-sm-10" controller="profile" action="clubProfile" id="${clubProfileId}">
                  ${clubProfile}
                </g:link>
                <div class="pull-left">
                  <tempvs:modalButton id="deleteProfile" size="modal-sm" classes="glyphicon glyphicon-trash">
                    <g:message code='profile.deleteConfirmation.text' args="${[clubProfile]}"/>
                    <br/>
                    <tempvs:ajaxLink message="yes" controller="profile" action="deleteProfile" id="${clubProfileId}" method="DELETE"/>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                  </tempvs:modalButton>
                </div>
              </li>
            </g:each>
          </ul>
        </g:if>
        <div>
          <tempvs:modalButton id="createProfile" message="clubProfile.create.link">
            <tempvs:ajaxForm controller="profile" action="createClubProfile">
              <tempvs:formField type="file" name="imageUploadBean.image" label="profile.avatar.label"/>
              <tempvs:formField type="text" name="imageUploadBean.imageInfo" label="profile.avatarInfo.label"/>
              <tempvs:formField type="text" name="firstName" label="profile.firstName.label"/>
              <tempvs:formField type="text" name="lastName" label="profile.lastName.label"/>
              <tempvs:formField type="text" name="location" label="profile.location.label"/>
              <tempvs:formField type="text" name="profileId" label="profile.profileId.label"/>
              <tempvs:formField type="text" name="nickName" label="profile.nickName.label"/>
              <tempvs:formField type="text" name="clubName" label="profile.clubName.label"/>
              <tempvs:formField type="select" name="period" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.dropdown.label"/>
              <tempvs:ajaxSubmitButton value="clubProfile.create.button"/>
            </tempvs:ajaxForm>
          </tempvs:modalButton>
        </div>
      </div>
      <div class="col-sm-4"></div>
    </div>
  </body>
</html>
