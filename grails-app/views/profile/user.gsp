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
            <div>
              <g:link class="btn btn-default disableable col-sm-12" controller="item" action="stash" id="${user.id}">
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
          <div class="col-sm-6 ajax-form">
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
      </g:if>
      <g:elseif test="${notFoundMessage}">
        <g:message code="${notFoundMessage}" args="${[id]}" />
      </g:elseif>
    </body>
</html>
