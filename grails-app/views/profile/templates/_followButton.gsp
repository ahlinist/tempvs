<div id="followButton">
  <sec:ifLoggedIn>
    <g:if test="${mayBeFollowed}">
      <div style="border: 4px #eee groove; padding: 6px;" class="text-center" class="row">
        <g:set var="parameters" value="${[profileClassName: profile.class.name, profileId: profile.id]}"/>
        <g:if test="${isFollowed}">
          <g:render template="/ajax/templates/ajaxLink"
              model="${[controller: 'following', action: 'unfollow', params: parameters, method: 'DELETE', selector: 'div#followButton', classes: 'btn btn-secondary btn-block active']}">
            <g:message code="profile.unfollow.button"/>
          </g:render>
        </g:if>
        <g:else>
          <g:render template="/ajax/templates/ajaxLink"
              model="${[controller: 'following', action: 'follow', params: parameters, method: 'POST', selector: 'div#followButton', classes: 'btn btn-default btn-block']}">
            <g:message code="profile.follow.button"/>
          </g:render>
        </g:else>
      </div>
    </g:if>
  </sec:ifLoggedIn>
</div>
