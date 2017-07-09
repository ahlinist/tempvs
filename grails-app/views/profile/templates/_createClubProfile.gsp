<%@ page import="com.tempvs.periodization.Period"%>
<tempvs:ajaxForm action="createClubProfile">
  <tempvs:formField type="text" name="firstName" label="clubProfile.firstName.label" />
  <tempvs:formField type="text" name="lastName" label="clubProfile.lastName.label" />
  <tempvs:formField type="text" name="nickName" label="clubProfile.nickName.label" />
  <tempvs:formField type="text" name="clubName" label="clubProfile.clubName.label" />
  <tempvs:formField type="select" name="period" value="" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.form.label"/>
  <tempvs:ajaxSubmitButton value="clubProfile.create.button" />
</tempvs:ajaxForm>
