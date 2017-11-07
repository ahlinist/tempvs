<%@ page import="com.tempvs.periodization.Period"%>
<tempvs:ajaxForm action="createItem">
  <tempvs:imageUploader fieldName="imageUploadBeans" imageLabel="item.image.label" infoLabel="item.imageInfo.label"/>
  <tempvs:formField type="text" name="name" value="${item?.name}" label="item.name.label" />
  <tempvs:formField type="text" name="description" value="${item?.description}" label="item.description.label" />
  <tempvs:formField type="select" name="period" value="${item?.period}" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.dropdown.label"/>
  <input type="hidden" name="item" value="${item?.id}"/>
  <input type="hidden" name="itemGroup" value="${itemGroup?.id}"/>
  <tempvs:ajaxSubmitButton value="item.createItem.button"/>
</tempvs:ajaxForm>
