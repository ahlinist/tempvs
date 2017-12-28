<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <g:if test="${profile}">
        <title>Tempvs - ${profile}</title>
      </g:if>
    </head>
    <body>
      <span id="mins-ago" class="hidden"><g:message code="date.minutesAgo"/></span>
      <span id="half-hour-ago" class="hidden"><g:message code="date.halfHourAgo"/></span>
      <g:if test="${profile}">
        <div class="row">
          <div class="col-sm-3">
            <g:render template="/profile/templates/identity"/>
            <div><b><g:message code="date.lastActive" /></b> <tempvs:dateFromNow date="${user.lastActive}"/></div>
          </div>
          <div class="col-sm-5 ajax-form">
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="firstName" value="${profile.firstName}" label="profile.firstName.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="lastName" value="${profile.lastName}" label="profile.lastName.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="nickName" value="${profile.nickName}" label="profile.nickName.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="profileId" value="${profile.profileId}" label="profile.profileId.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileEmail" name="profileEmail" value="${profile.profileEmail}" label="profile.profileEmail.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="location" value="${profile.location}" label="profile.location.label"/>
            <tempvs:ajaxSmartForm type="text" action="editProfileField" name="clubName" value="${profile.clubName}" label="profile.clubName.label"/>
            <tempvs:ajaxSmartForm type="select" action="editProfileField" name="period" value="${profile.period?.value}" label="periodization.period.dropdown.label" editAllowed="${false}"/>
          </div>
          <div class="col-sm-4">
            <b><g:message code="passport.list.label"/></b>
            <g:if test="${passports}">
                <ul>
                  <g:each var="passport" in="${passports}">
                    <g:set var="passportId" value="${passport.id}"/>
                    <g:set var="passportName" value="${passport.name}"/>
                    <li class="row" id="passport-${passportId}">
                      <g:link class="btn btn-default col-sm-10" controller="passport" action="show" id="${passportId}"  data-toggle="tooltip" data-placement="bottom" title="${passport.description}">
                        ${passportName}
                      </g:link>
                      <g:if test="${editAllowed}">
                        <div class="pull-left">
                          <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'passport.delete.button')}">
                            <tempvs:modalButton id="deletePassport-${passport.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
                              <g:message code='passport.deleteConfirmation.text' args="${[passportName]}"/>
                              <br/>
                              <tempvs:ajaxLink message="yes" controller="passport" action="deletePassport" id="${passportId}" method="DELETE" selector="li#passport-${passportId}"/>
                              <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                            </tempvs:modalButton>
                          </span>
                        </div>
                      </g:if>
                    </li>
                  </g:each>
                </ul>
            </g:if>
            <g:if test="${editAllowed}">
              <div>
                <tempvs:modalButton id="createPassport" classes="glyphicon glyphicon-plus">
                  <tempvs:ajaxForm controller="passport" action="createPassport">
                    <tempvs:formField type="text" name="name" label="passport.name.label"/>
                    <tempvs:formField type="text" name="description" label="passport.description.label"/>
                    <tempvs:ajaxSubmitButton value="passport.create.button"/>
                  </tempvs:ajaxForm>
                </tempvs:modalButton>
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
