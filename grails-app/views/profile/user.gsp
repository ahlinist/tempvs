<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <g:if test="${profile}">
        <title>Tempvs - ${profile}</title>
      </g:if>
    </head>
    <body>
      <g:if test="${profile}">
        <div class="row">
          <div class="col-sm-3">
            ${profile}
            <g:render template="/profile/templates/avatar"/>
            <div class="row">
              <g:link class="btn btn-default disableable" controller="item" action="stash" id="${user.id}">
                <span class="pull-left">
                  <g:message code="item.stash.button"/>&nbsp;
                </span>
                <span class="pull-right">
                  <span class="glyphicon glyphicon-tent text-right"></span>
                </span>
              </g:link>
            </div>
            <g:render template="/profile/templates/followButton"/>
          </div>
          <div class="col-sm-5">
            <div class="ajax-form">
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editProfileField', name: 'firstName', value: profile.firstName, label: 'profile.firstName.label', mandatory: true]}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editProfileField', name: 'lastName', value: profile.lastName, label: 'profile.lastName.label', mandatory: true]}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editProfileField', name: 'profileId', value: profile.profileId, label: 'profile.profileId.label']}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editProfileEmail', name: 'profileEmail', value: profile.profileEmail, label: 'profile.profileEmail.label']}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editProfileField', name: 'location', value: profile.location, label: 'profile.location.label']}"/>
            </div>
          </div>
          <div class="col-sm-2">
            <div id="club-profile-list">
              <g:if test="${activeProfiles || inactiveProfiles}">
                <g:if test="${activeProfiles}">
                  <b><g:message code="clubProfile.active.list.label"/></b>
                  <ul>
                    <g:each var="clubProfile" in="${activeProfiles}">
                      <g:set var="clubProfileId" value="${clubProfile.id}"/>
                      <li class="row" id="clubProfile-${clubProfileId}">
                        <g:link class="btn btn-default col-sm-12" controller="profile" action="club" id="${clubProfileId}">
                          ${clubProfile}
                        </g:link>
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
                        <g:link class="btn btn-default col-sm-12" controller="profile" action="club" id="${clubProfileId}">
                          ${clubProfile}
                        </g:link>
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
                    model="${[id: 'createProfile', message: 'clubProfile.create.button']}">
                  <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'profile', action: 'createClubProfile']}">
                    <g:render template="/common/templates/formField" model="${[type: 'file', name: 'imageUploadBean.image', label: 'profile.avatar.label']}"/>
                    <g:render template="/common/templates/formField" model="${[type: 'text', name: 'imageUploadBean.imageInfo', label: 'profile.avatarInfo.label']}"/>
                    <g:render template="/common/templates/formField" model="${[type: 'text', name: 'firstName', label: 'profile.firstName.label', mandatory: true]}"/>
                    <g:render template="/common/templates/formField" model="${[type: 'text', name: 'lastName', label: 'profile.lastName.label']}"/>
                    <g:render template="/common/templates/formField" model="${[type: 'text', name: 'nickName', label: 'profile.nickName.label']}"/>
                    <g:render template="/common/templates/formField" model="${[type: 'text', name: 'location', label: 'profile.location.label']}"/>
                    <g:render template="/common/templates/formField" model="${[type: 'text', name: 'profileId', label: 'profile.profileId.label']}"/>
                    <g:render template="/common/templates/formField" model="${[type: 'text', name: 'clubName', label: 'profile.clubName.label']}"/>
                    <g:render template="/common/templates/formField" model="${[type: 'select', name: 'period', label: 'periodization.period.dropdown.label', mandatory: true, from: periods, optionKey: 'key', optionValue: 'value']}"/>
                    <g:render template="/ajax/templates/submitButton">
                      <g:message code="clubProfile.create.button"/>
                    </g:render>
                  </g:render>
                </g:render>
              </div>
            </div>
          </div>
          <div class="col-sm-2">

          </div>
        </div>
      </g:if>
      <g:elseif test="${notFoundMessage}">
        <g:message code="${notFoundMessage}" args="${[id]}" />
      </g:elseif>
    </body>
</html>
