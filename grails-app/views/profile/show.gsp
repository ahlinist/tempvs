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
        <g:set var="profileId" value="${profile.id}"/>
        <g:if test="${profile.ofClubType && !active}">
          <div class="well">
            <div class="text-right">
              <b><g:message code="profile.inactive.message"/></b>&nbsp;
              <g:if test="${profile.user == user}">
                <g:render template="/ajax/templates/ajaxLink"
                    model="${[controller: 'profile', action: 'activateProfile', id: profileId, method: 'POST', classes:'btn btn-default']}">
                  <g:message code="profile.activate.button"/>
                </g:render>
              </g:if>
            </div>
          </div>
        </g:if>
        <div class="row">
          <div class="col-sm-3">
            ${profile}
            <g:render template="/profile/templates/avatar"/>
            <ul class="row">
              <li class="row">
                <g:link class="btn btn-default disableable col-sm-12" controller="item" action="stash" id="${user.id}">
                  <span class="pull-left">
                    <g:message code="item.stash.button"/>&nbsp;
                  </span>
                  <span class="pull-right">
                    <span class="glyphicon glyphicon-tent text-right"></span>
                  </span>
                </g:link>
              </li>
              <li class="row">
                <g:link class="btn btn-default disableable col-sm-12" controller="following" action="show" id="${profileId}">
                  <span class="pull-left">
                    <g:message code="following.list.button"/>&nbsp;
                  </span>
                  <span class="pull-right">
                    <span class="fa fa-users">
                      <g:if test="${newFollowings}">
                        <span class="badge badge-notify rounded" style="background-color: red; position: absolute; border-radius: 10px !important;">
                          ${newFollowings}
                        </span>
                      </g:if>
                    </span>
                  </span>
                </g:link>
              </li>
              <g:if test="${(profile != currentProfile) && (profile.type == currentProfile?.type) && (profile.period == currentProfile?.period)}">
                <li class="row">
                  <button class="btn btn-default col-sm-12" data-toggle="modal" data-target="#writeMessageButton">
                    <span class="pull-left">
                      <g:message code="message.write.message.button"/>&nbsp;
                    </span>
                    <span class="pull-right">
                      <span class="fa fa-envelope"></span>
                    </span>
                  </button>
                  <div id="writeMessageButton" class="modal fade" role="dialog">
                    <div class="modal-dialog">
                      <div class="modal-content">
                        <div class="modal-body">
                          <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'message', action: 'createDialogue']}">
                            <textarea name="text" style="width: 100%; height: 100px;"></textarea>
                            <input type="hidden" name="receivers[0]" value="${profileId}">
                            <div>
                              <g:render template="/ajax/templates/submitButton">
                                <g:message code="message.send.message.button"/>
                              </g:render>
                            </div>
                          </g:render>
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
              </g:if>
              <li class="row">
                <g:if test="${profile.ofClubType && editAllowed && active}">
                  <g:render template="/common/templates/modalButton"
                      model="${[elementId: 'deactivateProfile' + profileId, size: 'modal-sm', message: 'profile.club.deactivate.button', classes: 'col-sm-12']}">
                    <g:message code='profile.deactivateConfirmation.text' args="${[profile]}"/>
                    <br/>
                    <g:render template="/ajax/templates/ajaxLink"
                        model="${[controller: 'profile', action: 'deactivateProfile', id: profileId, method: 'POST', classes: 'btn btn-default']}">
                      <g:message code="yes"/>
                    </g:render>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                  </g:render>
                </g:if>
              </li>
              <li class="row">
                <g:render template="/profile/templates/followButton"/>
              </li>
            </ul>
          </div>
          <div class="col-sm-5">
            <div class="ajax-form">
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editProfileField', name: 'firstName', value: profile.firstName, label: 'profile.firstName.label', mandatory: true]}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editProfileField', name: 'lastName', value: profile.lastName, label: 'profile.lastName.label']}"/>
              <g:if test="${profile.ofClubType}">
                <g:render template="/ajax/templates/ajaxSmartForm"
                    model="${[type: 'text', action: 'editProfileField', name: 'nickName', value: profile.nickName, label: 'profile.nickName.label']}"/>
              </g:if>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editProfileField', name: 'profileId', value: profile.profileId, label: 'profile.profileId.label']}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editProfileEmail', name: 'profileEmail', value: profile.profileEmail, label: 'profile.profileEmail.label']}"/>
              <g:render template="/ajax/templates/ajaxSmartForm"
                  model="${[type: 'text', action: 'editProfileField', name: 'location', value: profile.location, label: 'profile.location.label']}"/>
              <g:if test="${profile.ofClubType}">
                <g:render template="/ajax/templates/ajaxSmartForm"
                    model="${[type: 'text', action: 'editProfileField', name: 'clubName', value: profile.clubName, label: 'profile.clubName.label']}"/>
                <g:render template="/ajax/templates/ajaxSmartForm"
                    model="${[type: 'text', name: 'period', value: profile.period?.value, label: 'periodization.period.dropdown.label', editAllowed: false]}"/>
              </g:if>
            </div>
          </div>
          <g:if test="${profile.ofUserType}">
            <div class="col-sm-2">
              <div id="club-profile-list">
                <g:if test="${activeProfiles || inactiveProfiles}">
                  <g:if test="${activeProfiles}">
                    <b><g:message code="clubProfile.active.list.label"/></b>
                    <ul>
                      <g:each var="clubProfile" in="${activeProfiles}">
                        <g:set var="clubProfileId" value="${clubProfile.id}"/>
                        <li class="row" id="clubProfile-${clubProfileId}">
                          <g:link class="btn btn-default col-sm-12" controller="profile" action="show" id="${clubProfileId}">
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
                          <g:link class="btn btn-default col-sm-12" controller="profile" action="show" id="${clubProfileId}">
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
                <g:if test="${editAllowed}">
                  <div class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.club.create.tooltip')}">
                    <g:render template="/common/templates/modalButton"
                        model="${[elementId: 'createProfile', icon: 'glyphicon glyphicon-plus']}">
                      <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'profile', action: 'createProfile']}">
                        <g:render template="/common/templates/formField" model="${[type: 'file', name: 'imageUploadBean.image', label: 'profile.avatar.label']}"/>
                        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'imageUploadBean.imageInfo', label: 'profile.avatarInfo.label']}"/>
                        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'firstName', label: 'profile.firstName.label', mandatory: true]}"/>
                        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'lastName', label: 'profile.lastName.label']}"/>
                        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'nickName', label: 'profile.nickName.label']}"/>
                        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'location', label: 'profile.location.label']}"/>
                        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'profileId', label: 'profile.profileId.label']}"/>
                        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'profileEmail', label: 'profile.profileEmail.label']}"/>
                        <g:render template="/common/templates/formField" model="${[type: 'text', name: 'clubName', label: 'profile.clubName.label']}"/>
                        <g:render template="/common/templates/formField" model="${[type: 'select', name: 'period', label: 'periodization.period.dropdown.label', mandatory: true, from: periods, optionKey: 'key', optionValue: 'value']}"/>
                        <input type="hidden" name="type" value="CLUB"/>
                        <g:render template="/ajax/templates/submitButton">
                          <g:message code="profile.club.create.button"/>
                        </g:render>
                      </g:render>
                    </g:render>
                  </div>
                </g:if>
              </div>
            </div>
            <div class="col-sm-2">

            </div>
          </g:if>
          <g:if test="${profile.ofClubType}">
            <div class="col-sm-2">
              <div class="row">
                <b><g:message code="profile.user.label"/></b>
                <g:link class="btn btn-default col-sm-12" controller="profile" action="show" id="${userProfile.id}">
                  ${userProfile}
                </g:link>
              </div>
            </div>
            <div class="col-sm-2">
              <b><g:message code="passport.list.label"/></b>
              <g:if test="${passports}">
                <ul>
                  <g:each var="passport" in="${passports}">
                    <g:set var="passportId" value="${passport.id}"/>
                    <g:set var="passportName" value="${passport.name}"/>
                    <li class="row" id="passport-${passportId}">
                      <g:link class="btn btn-default col-sm-12" controller="passport" action="show" id="${passportId}"  data-toggle="tooltip" data-placement="bottom" title="${passport.description}">
                        ${passportName}
                      </g:link>
                    </li>
                  </g:each>
                </ul>
              </g:if>
              <g:if test="${editAllowed && active}">
                <div class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'passport.create.tooltip')}">
                  <g:render template="/common/templates/modalButton"
                      model="${[elementId: 'createPassport', icon: 'glyphicon glyphicon-plus']}">
                    <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'passport', action: 'createPassport']}">
                      <g:render template="/image/templates/imageUploader"/>
                      <g:render template="/common/templates/formField" model="${[type: 'text', name: 'name', label: 'passport.name.label', mandatory: true]}"/>
                      <g:render template="/common/templates/formField" model="${[type: 'text', name: 'description', label: 'passport.description.label']}"/>
                      <g:render template="/ajax/templates/submitButton">
                        <g:message code="passport.create.button"/>
                      </g:render>
                    </g:render>
                  </g:render>
                </div>
              </g:if>
            </div>
          </g:if>
        </div>
      </g:if>
      <g:elseif test="${notFoundMessage}">
        <g:message code="${notFoundMessage}" args="${[id]}" />
      </g:elseif>
    </body>
</html>
