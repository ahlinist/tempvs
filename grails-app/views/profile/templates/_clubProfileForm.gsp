<%@ page import="com.tempvs.periodization.Period"%>
<tempvs:ajaxForm action="${action}">
  <tempvs:formField type="file" name="avatarBean.image" label="profile.avatar.label" />
  <tempvs:formField type="text" name="avatarBean.imageInfo" label="profile.avatarInfo.label" />
  <tempvs:formField type="text" name="firstName" value="${profile?.firstName}" label="clubProfile.firstName.label" />
  <tempvs:formField type="text" name="lastName" value="${profile?.lastName}" label="clubProfile.lastName.label" />
  <tempvs:formField type="text" name="nickName" value="${profile?.nickName}" label="clubProfile.nickName.label" />
  <tempvs:formField type="text" name="location" value="${profile?.location}" label="clubProfile.location.label" />
  <tempvs:formField type="text" name="clubName" value="${profile?.clubName}" label="clubProfile.clubName.label" />
  <tempvs:formField type="text" name="profileId" value="${profile?.profileId}" label="clubProfile.profileId.label" />
  <tempvs:formField type="select" name="period" value="${profile?.period}" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.form.label"/>
  <tempvs:ajaxSubmitButton value="${button}" />
</tempvs:ajaxForm>
