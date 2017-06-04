<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.createItem.title"/></title>
    </head>
    <body>
      <div class="row">
        <div class="col-sm-10">
          <tempvs:ajaxForm action="createItem">
            <tempvs:formField type="text" name="name" label="item.item.name.label" />
            <tempvs:formField type="text" name="description" label="item.item.description.label" />
            <tempvs:formField type="file" name="itemImage" label="item.itemImage.label" />
            <tempvs:formField type="file" name="sourceImage" label="item.sourceImage.label" />
            <tempvs:ajaxSubmitButton value="item.createItem.button" />
          </tempvs:ajaxForm>
        </div>
        <div class="col-sm-2">
        </div>
      </div>
    </body>
</html>
