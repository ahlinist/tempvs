<%@ page import="com.tempvs.periodization.Period"%>
<tempvs:ajaxForm action="${action}">
  <tempvs:formField type="text" name="name" value="${item?.name}" label="item.item.name.label" />
  <tempvs:formField type="text" name="description" value="${item?.description}" label="item.item.description.label" />
  <tempvs:formField type="select" name="period" value="${item?.period}" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.form.label"/>
  <tempvs:ajaxSubmitButton value="${button}" />
</tempvs:ajaxForm>
