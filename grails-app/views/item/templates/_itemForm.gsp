<tempvs:ajaxForm action="${action}">
  <tempvs:formField type="text" name="name" value="${item?.name}" label="item.item.name.label" />
  <tempvs:formField type="text" name="description" value="${item?.description}" label="item.item.description.label" />
  <tempvs:ajaxSubmitButton value="${button}" />
</tempvs:ajaxForm>
