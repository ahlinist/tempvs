<g:if test="${clubProfile}">
  <span class="pull-left">
    <g:link controller="profile" action="club" id="${clubProfile.id}">
      <u>
        ${clubProfile}
      </u>
    </g:link>
  </span>
  <g:if test="${passport}">
    <span class="pull-left">&nbsp;&gt;&nbsp;</span>
    <span class="pull-left">
      <g:link controller="passport" action="show" id="${passport.id}" class="pull-left">
        <u>${passport.name}</u>
      </g:link>
    </span>
  </g:if>
</g:if>

