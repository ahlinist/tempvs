<div id="club-profile-list">
  <g:if test="${activeProfiles || inactiveProfiles}">
    <g:if test="${activeProfiles}">
      <label>
        <g:message code="clubProfile.active.list.label"/>
      </label>
      <ul>
        <g:each var="clubProfile" in="${activeProfiles}">
          <g:set var="clubProfileId" value="${clubProfile.id}"/>
          <li class="row" id="clubProfile-${clubProfileId}">
            <g:link class="btn btn-default col-sm-10" controller="profile" action="club" id="${clubProfileId}">
              ${clubProfile}
            </g:link>
            <span class="pull-left">
              <g:render template="/common/templates/modalButton"
                  model="${[id: 'deactivateProfile' + clubProfileId, size: 'modal-sm', icon: 'glyphicon glyphicon-off']}">
                <g:message code='profile.deactivateConfirmation.text' args="${[clubProfile]}"/>
                <br/>
                <tempvs:ajaxLink controller="profile" action="deactivateProfile" id="${clubProfileId}" method="POST" selector="div#club-profile-list" classes="btn btn-default">
                  <g:message code="yes"/>
                </tempvs:ajaxLink>
                <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
              </g:render>
            </span>
          </li>
        </g:each>
      </ul>
    </g:if>
    <g:if test="${inactiveProfiles}">
      <label>
        <g:message code="clubProfile.inactive.list.label"/>
      </label>
      <ul>
        <g:each var="clubProfile" in="${inactiveProfiles}">
          <g:set var="clubProfileId" value="${clubProfile.id}"/>
          <li class="row" id="clubProfile-${clubProfileId}">
            <g:link class="btn btn-default col-sm-10" controller="profile" action="club" id="${clubProfileId}">
              ${clubProfile}
            </g:link>
            <span class="pull-left">
              <g:render template="/common/templates/modalButton"
                  model="${[id: 'activateProfile' + clubProfileId, size: 'modal-sm', icon: 'glyphicon glyphicon-play']}">
                <g:message code='profile.activateConfirmation.text' args="${[clubProfile]}"/>
                <br/>
                <tempvs:ajaxLink controller="profile" action="activateProfile" id="${clubProfileId}" method="POST" classes="btn btn-default">
                  <g:message code="yes"/>
                </tempvs:ajaxLink>
                <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
              </g:render>
            </span>
          </li>
        </g:each>
      </ul>
    </g:if>
  </g:if>
  <g:else>
    <i><g:message code="clubProfile.noProfiles.message"/></i>
  </g:else>
  <div>
    <g:render template="/common/templates/modalButton"
        model="${[id: 'createProfile', icon: 'fa fa-user-plus']}">
      <tempvs:ajaxForm controller="profile" action="createClubProfile">
        <g:render template="/common/templates/formField" model="${[type: 'file', name: 'imageUploadBean.image', label: 'profile.avatar.label']}"/>
        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'imageUploadBean.imageInfo', label: 'profile.avatarInfo.label']}"/>
        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'firstName', label: 'profile.firstName.label', mandatory: true]}"/>
        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'lastName', label: 'profile.lastName.label']}"/>
        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'nickName', label: 'profile.nickName.label']}"/>
        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'location', label: 'profile.location.label']}"/>
        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'profileId', label: 'profile.profileId.label']}"/>
        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'clubName', label: 'profile.clubName.label']}"/>
        <g:render template="/common/templates/formField" model="${[type: 'select', name: 'period', label: 'periodization.period.dropdown.label', mandatory: true, from: periods, optionKey: 'key', optionValue: 'value']}"/>
        <tempvs:ajaxSubmitButton value="clubProfile.create.button"/>
      </tempvs:ajaxForm>
    </g:render>
  </div>
</div>
