<g:hasErrors bean="${errorBean}">
  <ul>
    <g:eachError var="err" bean="${errorBean}">
      <div class="alert alert-danger text-center">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        <g:message error="${err}" />
      </div>
    </g:eachError>
  </ul>
</g:hasErrors>