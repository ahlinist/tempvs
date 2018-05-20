<div id="avatar">
  <g:set var="avatar" value="${profile.avatar}"/>
  <g:render template="/image/templates/modalImage" model="${[image: avatar]}">
    <sec:ifLoggedIn>
      <g:if test="${editAllowed}">
        <g:if test="${avatar}">
          <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.deleteAvatar.tooltip')}">
            <g:render template="/common/templates/modalButton"
                model="${[id: 'deleteAvatar', size: 'modal-sm', icon: 'glyphicon glyphicon-trash']}">
              <g:message code='profile.avatar.deleteConfirmation.text'/>
              <br/>
              <g:render template="/ajax/templates/ajaxLink"
                  model="${[controller: 'profile', action: 'deleteAvatar', params: [profileClass: profile.class.name, profileId: profile.id], method: 'DELETE', classes: 'btn btn-default']}">
                <g:message code="yes"/>
              </g:render>
              <button type="button" class="btn btn-default" onclick="$('#deleteAvatar').modal('hide');"><g:message code="no"/></button>
            </g:render>
          </span>
        </g:if>
        <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.editAvatar.tooltip')}">
          <g:render template="/common/templates/modalButton"
              model="${[id: 'uploadAvatar', icon: 'glyphicon glyphicon-picture']}">
            <g:render template="/ajax/templates/ajaxForm"
                model="${[controller: 'profile', action: 'uploadAvatar', selector: 'div#avatar']}">
              <g:render template="/common/templates/formField" model="${[type: 'file', name: 'image', label: 'profile.avatar.label']}"/>
              <g:render template="/common/templates/formField" model="${[type: 'text', name: 'imageInfo', label: 'profile.avatarInfo.label']}"/>
              <g:render template="/ajax/templates/submitButton">
                <g:message code="profile.updateAvatar.button"/>
              </g:render>
            </g:render>
          </g:render>
        </span>
      </g:if>
    </sec:ifLoggedIn>
  </g:render>
</div>
