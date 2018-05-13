<div id="welcome-block">
  <sec:ifAnyGranted roles="ROLE_CONTRIBUTOR">
    <g:set var="contributorGranted" value="${true}"/>
  </sec:ifAnyGranted>
  <sec:ifAnyGranted roles="ROLE_SCRIBE">
    <g:set var="scribeGranted" value="${true}"/>
  </sec:ifAnyGranted>
  <sec:ifAnyGranted roles="ROLE_ARCHIVARIUS">
    <g:set var="archivariusGranted" value="${true}"/>
  </sec:ifAnyGranted>

  <div class="row">
    <div class="well">
      <g:if test="${archivariusGranted}">
        <div class="text-center">
          <g:message code="library.archivarius.welcome.message"/>
          <g:link controller="library" action="admin" class="btn btn-default pull-right">
            <g:message code="library.admin.link"/>
          </g:link>
        </div>
      </g:if>
      <g:elseif test="${scribeGranted}">
        <div class="text-center">
          <g:message code="library.scribe.welcome.message"/>
        </div>
      </g:elseif>
      <g:elseif test="${contributorGranted}">
        <div class="text-center">
          <g:message code="library.contributor.welcome.message"/>
          <g:if test="${scribeObtained}">
            <br/>
            <g:message code="library.role.relogin.message"/>
          </g:if>
          <g:else>
            <div class="text-right">
              <g:if test="${scribeRequested}">
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
            </div>
          </g:else>
        </div>
      </g:elseif>
      <g:else>
        <div class="text-center">
          <g:message code="library.guest.welcome.message"/>
          <sec:ifLoggedIn>
            <g:if test="${contributorObtained}">
              <br/>
              <g:message code="library.role.relogin.message"/>
            </g:if>
            <g:else>
              <div class="text-right">
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
              </div>
            </g:else>
          </sec:>
        </div>
      </g:else>
    </div>
  </div>
</div>
