<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${item?.name ?: g.message(code: 'item.show.title')}</title>
    </head>
    <body>
      <g:if test="${item}">
        <g:set var="itemImage" value="${item.itemImage}"/>
        <g:set var="sourceImage" value="${item.sourceImage}"/>
        <g:render template="/item/templates/navBar" model="${[item, itemGroup, user, userProfile]}"/>
        <div class="row">
          <div><b><g:message code="item.name.label"/>:</b> ${item.name}</div>
          <div><b><g:message code="item.description.label"/>:</b> ${item.description}</div>
          <div><b><g:message code="periodization.period.value.label"/>:</b> ${item.period.value}</div>
          <hr/>
          <div class="col-sm-3">
            <g:message code="item.itemImage.label"/>: <tempvs:image image="${itemImage}"/>
          </div>
          <div class="col-sm-3">
            <g:message code="item.sourceImage.label"/>: <tempvs:image image="${sourceImage}"/>
          </div>
        </div>
        <g:if test="${editAllowed}">
          <div class="row">
            <tempvs:modalButton id="itemForm" message="item.updateItem.link">
              <tempvs:ajaxForm action="updateItemImage">
                <tempvs:formField type="file" name="image" label="item.itemImage.label" />
                <tempvs:formField type="text" name="imageInfo" value="${itemImage?.imageInfo}" label="item.item.name.label" />
                <tempvs:ajaxSubmitButton value="item.updateItemImage.button" />
              </tempvs:ajaxForm>
              <tempvs:ajaxForm action="updateSourceImage">
                <tempvs:formField type="file" name="image" label="item.sourceImage.label" />
                <tempvs:formField type="text" name="imageInfo" value="${sourceImage?.imageInfo}" label="item.item.name.label" />
                <tempvs:ajaxSubmitButton value="item.updateSourceImage.button" />
              </tempvs:ajaxForm>
              <g:render template="/item/templates/itemForm" model="${[action: 'editItem', button: 'item.updateItem.button', item: item]}"/>
            </tempvs:modalButton>
            <tempvs:modalButton id="deleteItem" size="modal-sm" message="item.delete.button">
              <g:message code='item.deleteConfirmation.text' args="${[item.name]}"/>
              <br/>
              <tempvs:ajaxLink message="yes" controller="item" action="deleteItem" id="${item.id}"/>
              <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
            </tempvs:modalButton>
          </div>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="item.notFound.message"/>
      </g:else>
    </body>
</html>
