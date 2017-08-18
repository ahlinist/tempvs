<tempvs:ajaxForm action="${action}">
  <tempvs:formField type="text" name="name" value="${itemGroup?.name}" label="item.group.name.label" />
  <tempvs:formField type="text" name="description" value="${itemGroup?.description}" label="item.group.description.label" />
  <input type="hidden" name="groupId" value="${itemGroup?.id}"/>
  <tempvs:ajaxSubmitButton value="item.createGroup.button" />
</tempvs:ajaxForm>
