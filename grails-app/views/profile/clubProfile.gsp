<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <g:if test="${profile}">
        <title>Tempvs - <tempvs:fullName profile="${profile}"/></title>
      </g:if>
    </head>
    <body>
      <span id="mins-ago" class="hidden"><g:message code="date.minutesAgo"/></span>
      <span id="half-hour-ago" class="hidden"><g:message code="date.halfHourAgo"/></span>

      <g:if test="${profile}">
        <g:set var="user" value="${profile.user}"/>
        <div class="row">
          <div class="col-sm-3">
            <tempvs:fullName profile="${profile}"/>
            <tempvs:image objectId="${profile.avatar}" collection="avatar"/>
          </div>
          <div class="col-sm-3">
            <div><g:message code="date.lastActive" /> <tempvs:dateFromNow date="${user.lastActive}"/></div>
            <div><g:message code="clubProfile.profileEmail.label" />: ${profile.profileEmail}</div>
            <div><g:message code="clubProfile.location.label" />: ${profile.location}</div>
            <div><g:message code="clubProfile.clubName.label" />: ${profile.clubName}</div>
          </div>
        </div>
        <g:if test="${sec.username() == user.email.encodeAsHTML()}">
          <div class="row">
            <tempvs:modalButton id="deleteProfile" size="modal-sm" message="profile.delete.button">
              <g:message code='profile.deleteConfirmation.text' args="${[profile]}"/>
              <br/>
              <tempvs:ajaxLink message="yes" controller="profile" action="deleteProfile" id="${profile.id}"/>
              <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
            </tempvs:modalButton>
          </div>
        </g:if>
      </g:if>
      <g:elseif test="${message}">
        <g:message code="${message}" args="${args}" />
      </g:elseif>
    </body>
</html>
