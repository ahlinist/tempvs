<tempvs:ajaxForm action="${action}">
  <tempvs:formField type="text" name="period" value="${period.value}" label="periodization.period.form.label" disabled="${true}"/>
  <tempvs:formField type="text" name="name" value="${source?.name}" label="source.name.label" />
  <tempvs:formField type="text" name="description" value="${source?.description}" label="source.description.label" />
  <tempvs:ajaxSubmitButton value="${button}" />
</tempvs:ajaxForm>
