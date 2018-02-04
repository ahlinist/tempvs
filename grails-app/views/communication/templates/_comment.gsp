<g:set var="profile" value="${comment.clubProfile ?: comment.userProfile}"/>
<g:set var="currentProfile" value="${applicationContext.profileService.currentProfile}"/>
<div style="border: 1px solid #000;">
  <g:if test="${editAllowed || profile == currentProfile}">
    <div class="pull-right">
      <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'comment.delete.button')}">
        <tempvs:modalButton id="deleteComment-${comment.id}" size="modal-sm" classes="glyphicon glyphicon-trash">
          <g:message code='comment.deleteConfirmation.text'/>
          <br/>
          <tempvs:ajaxLink controller="${controllerName}" action="deleteComment" params="${[objectId: object.id, commentId: comment.id]}" method="DELETE" selector="div#comments" classes="btn btn-default">
            <g:message code="yes"/>
          </tempvs:ajaxLink>
          <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
        </tempvs:modalButton>
      </span>
    </div>
  </g:if>
  <div>
    <tempvs:image image="${profile.avatar}" styles="max-width: 45px; float: left;"/>
    <g:link controller="profile" action="${profile.class}" id="${profile.id}" class="btn btn-default">
      ${profile}
    </g:link>
  </div>
  <br/>
  <div class="row">
    <div class="well">${comment.text}</div>
  </div>
</div>