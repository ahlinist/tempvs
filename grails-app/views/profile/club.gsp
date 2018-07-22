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
      <g:if test="${!active}">
        <div class="well">
          <div class="text-right">
            <b><g:message code="profile.inactive.message"/></b>&nbsp;
            <g:if test="${profile.user == user}">
              <g:render template="/ajax/templates/ajaxLink"
                  model="${[controller: 'profile', action: 'activateProfile', id: profile.id, method: 'POST', classes:'btn btn-default']}">
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
              <g:link class="btn btn-default disableable col-sm-12" controller="following" action="show" id="${profile.id}">
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
            <li class="row">
              <g:if test="${editAllowed && active}">
                <g:render template="/common/templates/modalButton"
                    model="${[id: 'deactivateProfile' + profile.id, size: 'modal-sm', message: 'profile.club.deactivate.button', classes: 'col-sm-12']}">
                  <g:message code='profile.deactivateConfirmation.text' args="${[profile]}"/>
                  <br/>
                  <g:render template="/ajax/templates/ajaxLink"
                      model="${[controller: 'profile', action: 'deactivateProfile', id: profile.id, method: 'POST', classes: 'btn btn-default']}">
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
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', action: 'editProfileField', name: 'nickName', value: profile.nickName, label: 'profile.nickName.label']}"/>
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', action: 'editProfileField', name: 'profileId', value: profile.profileId, label: 'profile.profileId.label']}"/>
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', action: 'editProfileField', name: 'profileEmail', value: profile.profileEmail, label: 'profile.profileEmail.label']}"/>
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', action: 'editProfileField', name: 'location', value: profile.location, label: 'profile.location.label']}"/>
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', action: 'editProfileField', name: 'clubName', value: profile.clubName, label: 'profile.clubName.label']}"/>
            <g:render template="/ajax/templates/ajaxSmartForm"
                model="${[type: 'text', name: 'period', value: profile.period?.value, label: 'periodization.period.dropdown.label', editAllowed: false]}"/>
          </div>
        </div>
        <div class="col-sm-2">
          <div class="row">
            <b><g:message code="profile.user.label"/></b>
            <g:link class="btn btn-default col-sm-12" controller="profile" action="user" id="${user.userProfile.id}">
              ${user.userProfile}
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
                  model="${[id: 'createPassport', icon: 'glyphicon glyphicon-plus']}">
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
      </div>
    </g:if>
    <g:elseif test="${notFoundMessage}">
      <g:message code="${notFoundMessage}" args="${[id]}" />
    </g:elseif>
  </body>
</html>
