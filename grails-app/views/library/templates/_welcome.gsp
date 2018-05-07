<div id="welcome-block">
  <sec:ifAnyGranted roles="ROLE_CONTRIBUTOR">
    <g:set var="isContributor" value="${true}"/>
  </sec:ifAnyGranted>
  <sec:ifAnyGranted roles="ROLE_SCRIBE">
    <g:set var="isScribe" value="${true}"/>
  </sec:ifAnyGranted>
  <sec:ifAnyGranted roles="ROLE_ARCHIVARIUS">
    <g:set var="isArchivarius" value="${true}"/>
  </sec:ifAnyGranted>

  <div class="well">
    <g:if test="${isArchivarius}">
      <div class="text-center">
        <g:message code="library.archivarius.welcome.message"/>
        <g:link controller="library" action="admin" class="btn btn-default pull-right">
          <g:message code="library.admin.link"/>
        </g:link>
      </div>
    </g:if>
    <g:elseif test="${isScribe}">
      <div class="text-center">
        <g:message code="library.scribe.welcome.message"/>
      </div>
    </g:elseif>
    <g:elseif test="${isContributor}">
      <div class="text-center">
        <g:message code="library.contributor.welcome.message"/>
      </div>
      <span class="pull-right">
        <g:if test="${contributorRequested}">
          <g:render template="/ajax/templates/ajaxLink"
              model="${[controller: 'library', action: 'cancelRoleRequest', id: 'ROLE_SCRIBE', method: 'DELETE', selector: 'div#welcome-block', classes: 'btn btn-default']}">
            <g:message code="library.cancel.scribe.role.button"/>
          </g:render>
        </g:if>
        <g:else>
          <g:render template="/ajax/templates/ajaxLink"
              model="${[controller: 'library', action: 'requestRole', id: 'ROLE_SCRIBE', method: 'POST', selector: 'div#welcome-block', classes: 'btn btn-default']}">
            <g:message code="library.request.scribe.role.button"/>
          </g:render>
        </g:else>
      </span>
    </g:elseif>
    <g:else>
      <div class="text-center">
        <g:message code="library.guest.welcome.message"/>
        <sec:ifLoggedIn>
          <span class="pull-right">
            <g:if test="${contributorRequested}">
              <g:render template="/ajax/templates/ajaxLink"
                  model="${[controller: 'library', action: 'cancelRoleRequest', id: 'ROLE_CONTRIBUTOR', method: 'DELETE', selector: 'div#welcome-block', classes: 'btn btn-default']}">
                <g:message code="library.cancel.contributor.role.button"/>
              </g:render>
            </g:if>
            <g:else>
              <g:render template="/ajax/templates/ajaxLink"
                  model="${[controller: 'library', action: 'requestRole', id: 'ROLE_CONTRIBUTOR', method: 'POST', selector: 'div#welcome-block', classes: 'btn btn-default']}">
                <g:message code="library.request.contributor.role.button"/>
              </g:render>
            </g:else>
          </span>
        </sec:>
      </div>
    </g:else>
  </div>
</div>
