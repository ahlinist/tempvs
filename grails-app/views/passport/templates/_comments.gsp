<div id="comments">
  <ul>
    <g:each var="comment" in="${passport.comments}">
      <li>
        <g:render template="/communication/templates/comment" model="${[object: passport, comment: comment, controllerName: 'passport']}"/>
      </li>
    </g:each>
    <sec:ifLoggedIn>
      <tempvs:ajaxForm controller="passport" action="addComment" selector="div#comments">
        <g:textArea name="text" rows="2" cols="10"/>
        <br/>
        <input type="hidden" name="passportId" value="${passport.id}"/>
        <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-plus"/>
      </tempvs:ajaxForm>
    </sec:ifLoggedIn>
  </ul>
</div>
