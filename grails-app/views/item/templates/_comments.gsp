<div id="comments">
  <ul>
    <g:each var="comment" in="${item.comments}">
      <li>
        <g:render template="/communication/templates/comment"
                model="${[object: item, comment: comment, controllerName: 'item', currentProfile: currentProfile]}"/>
      </li>
    </g:each>
    <sec:ifLoggedIn>
      <tempvs:ajaxForm controller="item" action="addComment" selector="div#comments">
        <g:textArea name="text" rows="2" cols="10"/>
        <br/>
        <input type="hidden" name="itemId" value="${item.id}"/>
        <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-plus"/>
      </tempvs:ajaxForm>
    </sec:ifLoggedIn>
  </ul>
</div>
