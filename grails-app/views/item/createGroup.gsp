<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.createGroup.title"/></title>
    </head>
    <body>
      <div class="row">
        <div class="col-sm-10">
          <tempvs:ajaxForm action="createGroup">
            <tempvs:formField type="text" name="name" label="item.group.name.label" />
            <tempvs:formField type="text" name="description" label="item.group.description.label" />
            <tempvs:ajaxSubmitButton value="item.createGroup.button" />
          </tempvs:ajaxForm>
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>
