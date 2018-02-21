<div id="comments">
  <ul>
    <g:each var="comment" in="${source.comments}">
      <li>
        <g:render template="/communication/templates/comment"
                model="${[object: source, comment: comment, controllerName: 'source', currentProfile: currentProfile]}"/>
      </li>
    </g:each>
    <sec:ifLoggedIn>
      <tempvs:ajaxForm controller="source" action="addComment" selector="div#comments">
        <g:textArea name="text" rows="2" cols="10"/>
        <br/>
        <input type="hidden" name="sourceId" value="${source.id}"/>
        <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-plus"/>
      </tempvs:ajaxForm>
    </sec:ifLoggedIn>
  </ul>
</div>
