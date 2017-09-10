<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="item"/>
    <title>Tempvs - Edit ${item?.name ?: g.message(code: 'item.show.title')}</title>
  </head>
  <body>
    <g:set var="images" value="${item.images}"/>
    <g:set var="source" value="${item.source}"/>
    <g:set var="itemId" value="${item?.id}"/>
    <div class="row">
      <div class="row ajax-form">
        <tempvs:ajaxSmartForm type="text" action="updateItemField" name="name" value="${item?.name}" objectId="${itemId}" label="item.name.label" editAllowed="${editAllowed}"/>
        <tempvs:ajaxSmartForm type="text" action="updateItemField" name="description" value="${item?.description}" objectId="${itemId}" label="item.description.label" editAllowed="${editAllowed}"/>
        <tempvs:ajaxSmartForm type="select" action="updateItemField" name="period" value="${item?.period}" optionKey="key" optionValue="value" from="${Period.values()}" objectId="${itemId}" label="periodization.period.dropdown.label" editAllowed="${editAllowed}"/>
        <tempvs:ajaxSmartForm type="select" action="updateItemField" name="source" value="${item?.source?.name}" optionKey="id" optionValue="name" from="${applicationContext.sourceService.getSourcesByPeriod(item?.period)}" objectId="${itemId}" label="item.source.dropdown.label" editAllowed="${editAllowed}"/>
      </div>
      <div class="row">
        <g:if test="${images}">
          <ul>
            <g:each in="${images.sort {it.id}}" var="image">
              <li class="row image-list">
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
                      <tempvs:ajaxLink message="yes" controller="item" action="deleteItemImage" params="${[itemId: itemId, imageId: image.id]}" method="DELETE"/>
                      <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                    </tempvs:modalButton>
                  </span>
                  <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.editImage.tooltip')}">
                    <tempvs:modalButton id="editItemImage-${image.hashCode()}" classes="glyphicon glyphicon-picture">
                      <tempvs:ajaxForm action="editItemImage">
                        <tempvs:formField classes="image" type="file" name="image" label="item.image.label" />
                        <tempvs:formField classes="imageInfo" type="text" name="imageInfo" value="${image.imageInfo}" label="item.imageInfo.label" />
                        <input type="hidden" name="itemId" value="${itemId}"/>
                        <input type="hidden" name="imageId" value="${image.id}"/>
                        <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-floppy-disk" />
                      </tempvs:ajaxForm>
                    </tempvs:modalButton>
                  </span>
                  <g:if test="${image.imageInfo}">
                    <b><g:message code="item.imageInfo.label"/></b>:
                    <p>${image.imageInfo}</p>
                  </g:if>
                </div>
              </li>
            </g:each>
          </ul>
        </g:if>
      </div>
      <div class="row">
        <tempvs:ajaxForm action="addItemImages">
          <tempvs:imageUploader fieldName="imageUploadBeans" imageLabel="item.image.label" infoLabel="item.imageInfo.label"/>
          <input type="hidden" name="itemId" value="${itemId}"/>
          <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-floppy-disk"/>
        </tempvs:ajaxForm>
      </div>
    </div>
  </body>
</html>
