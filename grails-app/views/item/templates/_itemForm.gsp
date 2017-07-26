<%@ page import="com.tempvs.periodization.Period"%>
<%@ page import="com.tempvs.item.Source"%>
<tempvs:ajaxForm action="${action}">
  <g:if test="${action == 'createItem'}">
    <tempvs:imageUploader fieldName="imageBeans" imageLabel="item.image.label" infoLabel="item.imageInfo.label"/>
  </g:if>
  <tempvs:formField type="text" name="name" value="${item?.name}" label="item.name.label" />
  <tempvs:formField type="text" name="description" value="${item?.description}" label="item.description.label" />
  <tempvs:formField type="select" name="period" value="${item?.period}" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.form.label"/>
  <tempvs:formField type="select" name="source" value="${item?.source}" from="${Source.list()}" optionKey="id" optionValue="name" label="item.source.dropdown.label"/>
  <tempvs:ajaxSubmitButton value="${button}" />
</tempvs:ajaxForm>
