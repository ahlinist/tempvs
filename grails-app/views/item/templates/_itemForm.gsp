<%@ page import="com.tempvs.periodization.Period"%>
<tempvs:ajaxForm action="${action}">
  <g:if test="${action == 'createItem'}">
    <tempvs:imageUploader fieldName="imageUploadBeans" imageLabel="item.image.label" infoLabel="item.imageInfo.label"/>
  </g:if>
  <tempvs:formField type="text" name="name" value="${item?.name}" label="item.name.label" />
  <tempvs:formField type="text" name="description" value="${item?.description}" label="item.description.label" />
  <tempvs:formField type="select" name="period" value="${item?.period}" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.dropdown.label"/>
  <tempvs:formField type="select" name="source" value="${item?.source?.id}" from="${applicationContext.sourceService.getSourcesByPeriod(item?.period)}" optionKey="id" optionValue="name" label="item.source.dropdown.label" styles="${action == 'createItem' ? 'display:none;' : ''}"/>
  <input type="hidden" name="itemId" value="${item?.id}"/>
  <input type="hidden" name="groupId" value="${itemGroup?.id}"/>
  <tempvs:ajaxSubmitButton value="${button}" icon="${icon}"/>
</tempvs:ajaxForm>
