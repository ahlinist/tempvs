<g:set var="imageId" value="${image.id}"/>
<g:set var="imageInfo" value="${image.imageInfo}"/>
<li class="list-group-item row" id="itemImage-${imageId}">
  <div class="col-sm-4">
    <tempvs:modalImage image="${image}" classes="item-image" orientation="horizontal" styles="min-height: 20vw; max-height: 20vw;"/>
  </div>
  <div class="col-sm-4">
  </div>
  <div class="col-sm-4">
    <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.deleteImage.button')}">
      <tempvs:modalButton id="deleteItemImage-${image.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
        <g:message code='item.imageDeleteConfirmation.text'/>
        <br/>
        <tempvs:ajaxLink message="yes" controller="item" action="deleteImage" params="${[itemId: itemId, imageId: imageId]}" method="DELETE" selector="li#itemImage-${imageId}"/>
        <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
      </tempvs:modalButton>
    </span>
    <g:if test="${imageInfo}">
      <b><g:message code="item.imageInfo.label"/></b>:
      <p>${imageInfo}</p>
    </g:if>
  </div>
</li>

