<tempvs:ajaxForm action="${action}">
  <g:if test="${action == 'createSource'}">
    <tempvs:imageUploader fieldName="imageUploadBeans" imageLabel="source.sourceImage.label" infoLabel="source.imageInfo.label"/>
  </g:if>
  <tempvs:formField type="text" name="fake-period" value="${period.value}" label="periodization.period.form.label" disabled="${true}"/>
  <tempvs:formField type="text" name="name" value="${source?.name}" label="source.name.label" />
  <tempvs:formField type="text" name="description" value="${source?.description}" label="source.description.label" />
  <input type="hidden" name="period" value="${period?.key}"/>
  <input type="hidden" name="sourceId" value="${source?.id}"/>
  <tempvs:ajaxSubmitButton value="${button}" />
</tempvs:ajaxForm>
