${profile}
<g:set var="avatar" value="${profile.avatar}"/>
<tempvs:modalImage image="${avatar}">
  <sec:ifLoggedIn>
    <g:if test="${editAllowed}">
      <g:if test="${avatar}">
        <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.deleteAvatar.tooltip')}">
          <tempvs:modalButton id="deleteAvatar" size="modal-sm" classes="glyphicon glyphicon-trash">
            <g:message code='profile.avatar.deleteConfirmation.text'/>
            <br/>
            <tempvs:ajaxLink controller="profile" action="deleteAvatar" params="${[profileClass: profile.class.name, profileId: profile.id]}" method="DELETE" classes="btn btn-default">
              <g:message code="yes"/>
            </tempvs:ajaxLink>
            <button type="button" class="btn btn-default" onclick="$('#deleteAvatar').modal('hide');"><g:message code="no"/></button>
          </tempvs:modalButton>
        </span>
      </g:if>
      <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.editAvatar.tooltip')}">
        <tempvs:modalButton id="uploadAvatar" classes="glyphicon glyphicon-picture">
          <tempvs:ajaxForm action="uploadAvatar">
            <tempvs:formField type="file" name="image" label="profile.avatar.label" />
            <tempvs:formField type="text" name="imageInfo" label="profile.avatarInfo.label" />
            <tempvs:ajaxSubmitButton value="profile.updateAvatar.button" />
          </tempvs:ajaxForm>
        </tempvs:modalButton>
      </span>
    </g:if>
  </sec:ifLoggedIn>
</tempvs:modalImage>
<g:render template="/profile/templates/followButton"/>
