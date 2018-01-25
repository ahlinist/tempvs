<g:set var="profile" value="${comment.clubProfile ?: comment.userProfile}"/>
<g:set var="currentProfile" value="${applicationContext.profileService.currentProfile}"/>
<div style="border: 1px solid #000;">
  <div>
    <tempvs:image image="${profile.avatar}" styles="max-width: 45px; float: left;"/>
    <g:link controller="profile" action="${profile.class}" id="${profile.id}" class="btn btn-default">
      ${profile}
    </g:link>
  </div>
  <g:if test="${editAllowed || profile == currentProfile}">
    <div class="pull-right">
      <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'comment.delete.button')}">
        <tempvs:modalButton id="deleteComment-${comment.id}" size="modal-sm" classes="glyphicon glyphicon-trash">
          <g:message code='comment.deleteConfirmation.text'/>
          <br/>
          <tempvs:ajaxLink controller="${controllerName}" action="deleteComment" params="${[objectId: object.id, commentId: comment.id]}" method="DELETE" selector="div#comments">
            <g:message code="yes"/>
          </tempvs:ajaxLink>
          <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
        </tempvs:modalButton>
      </span>
    </div>
  </g:if>
  <br/>
  <div class="row">
    ${comment.text}
  </div>
</div>
