<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - ${userProfile}</title>
  </head>
  <body>
    <div class="row">
      <div class="col-sm-1">
      </div>
      <div class="col-sm-4">
        <label>
          <g:message code="userProfile.list.message"/>
        </label>
        <g:link class="list-group-item" controller="profile" action="userProfile" id="${userProfile.id}">
          ${userProfile}
        </g:link>
      </div>
      <div class="col-sm-1">
      </div>
      <div class="col-sm-5">
        <g:if test="${clubProfiles}">
          <label>
            <g:message code="clubProfile.list.message"/>
          </label>
          <ul>
            <g:each var="clubProfile" in="${clubProfiles}">
              <li class="row">
                <g:link class="btn btn-default col-sm-10" action="clubProfile" id="${clubProfile.id}">
                  ${clubProfile}
                </g:link>
                <span class="pull-left">
                  <tempvs:modalButton id="deleteProfile" size="modal-sm" classes="glyphicon glyphicon-trash">
                    <g:message code='profile.deleteConfirmation.text' args="${[clubProfile]}"/>
                    <br/>
                    <tempvs:ajaxLink message="yes" controller="profile" action="deleteProfile" id="${clubProfile.id}" method="DELETE"/>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                  </tempvs:modalButton>
                </span>
              </li>
            </g:each>
          </ul>
        </g:if>
        <g:else>
          <i><g:message code="clubProfile.noProfiles.message"/></i>
        </g:else>
        <div>
          <tempvs:modalButton id="createProfile" classes="fa fa-user-plus">
            <tempvs:ajaxForm controller="profile" action="createClubProfile">
              <tempvs:formField type="file" name="imageUploadBean.image" label="profile.avatar.label"/>
              <tempvs:formField type="text" name="imageUploadBean.imageInfo" label="profile.avatarInfo.label"/>
              <tempvs:formField type="text" name="firstName" label="profile.firstName.label"/>
              <tempvs:formField type="text" name="lastName" label="profile.lastName.label"/>
              <tempvs:formField type="text" name="nickName" label="profile.nickName.label"/>
              <tempvs:formField type="text" name="location" label="profile.location.label"/>
              <tempvs:formField type="text" name="profileId" label="profile.profileId.label"/>
              <tempvs:formField type="text" name="clubName" label="profile.clubName.label"/>
              <tempvs:formField type="select" name="period" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.dropdown.label"/>
              <tempvs:ajaxSubmitButton value="clubProfile.create.button"/>
            </tempvs:ajaxForm>
          </tempvs:modalButton>
        </div>
      </div>
      <div class="col-sm-1">
      </div>
    </div>
  </body>
</html>
