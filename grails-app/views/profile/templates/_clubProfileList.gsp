<%@ page import="com.tempvs.periodization.Period"%>
<div id="club-profile-list">
  <g:if test="${clubProfiles}">
    <label>
      <g:message code="clubProfile.list.message"/>
    </label>
    <ul>
      <g:each var="clubProfile" in="${clubProfiles}">
        <g:set var="clubProfileId" value="${clubProfile.id}"/>
        <li class="row" id="clubProfile-${clubProfileId}">
          <g:link class="btn btn-default col-sm-10" controller="profile" action="clubProfile" id="${clubProfileId}">
            ${clubProfile}
          </g:link>
          <span class="pull-left">
            <tempvs:modalButton id="deleteProfile-${clubProfileId}" size="modal-sm" classes="glyphicon glyphicon-trash">
              <g:message code='profile.deleteConfirmation.text' args="${[clubProfile]}"/>
              <br/>
              <tempvs:ajaxLink message="yes" controller="profile" action="deleteProfile" id="${clubProfileId}"
                      method="DELETE" selector="div#club-profile-list"/>
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