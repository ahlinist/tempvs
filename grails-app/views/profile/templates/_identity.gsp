${profile}
<tempvs:modalImage image="${avatar}">
  <sec:ifLoggedIn>
    <g:if test="${editAllowed}">
      <g:if test="${avatar}">
        <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.deleteAvatar.tooltip')}">
          <tempvs:modalButton id="deleteAvatar" size="modal-sm" classes="glyphicon glyphicon-trash">
            <g:message code='profile.avatar.deleteConfirmation.text'/>
            <br/>
            <tempvs:ajaxLink message="yes" controller="profile" action="deleteAvatar" id="${profile.id}" params="${[className: profile.class.name]}"/>
            <button type="button" class="btn btn-default" onclick="$('#deleteAvatar').modal('hide');"><g:message code="no"/></button>
          </tempvs:modalButton>
        </span>
      </g:if>
      <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.editAvatar.tooltip')}">
        <tempvs:modalButton id="uploadAvatar" classes="glyphicon glyphicon-edit">
          <tempvs:ajaxForm action="uploadAvatar">
            <tempvs:formField type="file" name="imageUploadBean.image" label="profile.avatar.label" />
            <tempvs:formField type="text" name="imageUploadBean.imageInfo" label="profile.avatarInfo.label" />
            <input type="hidden" name="className" value="${profile.class.name}"/>
            <input type="hidden" name="id" value="${profile.id}"/>
            <tempvs:ajaxSubmitButton value="profile.updateAvatar.button" />
          </tempvs:ajaxForm>
        </tempvs:modalButton>
      </span>
    </g:if>
  </sec:ifLoggedIn>
</tempvs:modalImage>
