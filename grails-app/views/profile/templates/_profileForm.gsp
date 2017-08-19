<%@ page import="com.tempvs.periodization.Period"%>
<g:if test="${action in ['editClubProfile', 'editUserProfile']}">
  <tempvs:ajaxForm action="editProfileEmail">
    <tempvs:formField type="email" name="email" value="${profile.profileEmail}" label="profile.profileEmail.label"/>
    <tempvs:ajaxSubmitButton value="profile.profileEmail.update.button"/>
  </tempvs:ajaxForm>
</g:if>
<tempvs:ajaxForm action="${action}">
  <tempvs:formField type="file" name="avatarBean.image" label="profile.avatar.label"/>
  <tempvs:formField type="text" name="avatarBean.imageInfo" label="profile.avatarInfo.label"/>
  <tempvs:formField type="text" name="firstName" value="${profile?.firstName}" label="profile.firstName.label"/>
  <tempvs:formField type="text" name="lastName" value="${profile?.lastName}" label="profile.lastName.label"/>
  <tempvs:formField type="text" name="location" value="${profile?.location}" label="profile.location.label"/>
  <tempvs:formField type="text" name="profileId" value="${profile?.profileId}" label="profile.profileId.label"/>
  <g:if test="${action in ['editClubProfile', 'createClubProfile']}">
    <tempvs:formField type="text" name="nickName" value="${profile?.nickName}" label="profile.nickName.label"/>
    <tempvs:formField type="text" name="clubName" value="${profile?.clubName}" label="profile.clubName.label"/>
    <tempvs:formField type="select" name="period" value="${profile?.period}" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.dropdown.label"/>
  </g:if>
  <tempvs:ajaxSubmitButton value="${button}"/>
</tempvs:ajaxForm>
