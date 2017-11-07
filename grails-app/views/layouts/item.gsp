<g:applyLayout name="main">
  <!DOCTYPE html>
  <html>
    <head>
      <g:layoutHead/>
    </head>
    <body>
      <div class="row">
        <g:render template="/item/templates/navBar" model="${[item, itemGroup, user, userProfile]}"/>
      </div>
        <g:layoutBody/>
    </body>
  </html>
</g:applyLayout>
