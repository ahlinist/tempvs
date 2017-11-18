<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="item"/>
    <title>Tempvs - ${item?.name ?: g.message(code: 'item.show.title')}</title>
  </head>
  <body>
    <g:if test="${item}">
      <g:set var="itemId" value="${item.id}"/>
      <g:set var="period" value="${item.period}"/>
      <div class="row">
        <g:if test="${editAllowed}">
          <div class="pull-right">
            <g:render template="/item/templates/deleteItemButton" model="${[item: item]}"/>
          </div>
        </g:if>
      </div>
      <div class="row">
        <div class="col-sm-8 ajax-form">
          <tempvs:ajaxSmartForm type="text" action="editItemField" name="name" value="${item.name}" objectId="${itemId}" label="item.name.label" editAllowed="${editAllowed}"/>
          <tempvs:ajaxSmartForm type="text" action="editItemField" name="description" value="${item.description}" objectId="${itemId}" label="item.description.label" editAllowed="${editAllowed}"/>
          <tempvs:ajaxSmartForm type="select" action="editItemField" name="period" value="${period}" optionKey="key" optionValue="value" from="${Period.values()}" objectId="${itemId}" label="periodization.period.dropdown.label" editAllowed="${editAllowed}"/>
        </div>
        <div class="col-sm-4">
          <g:message code="item.sources.label"/>
          <ul id="linkedSources">
            <g:each var="source" in="${sources}">
              <g:render template="/item/templates/linkedSource" model="${[source: source, itemId: itemId]}"/>
            </g:each>
            <g:if test="${editAllowed}">
              <li class="row">
                <tempvs:ajaxForm controller="item" action="linkSource">
                  <g:select class="col-sm-12 tempvs-form-field" name="sourceId" from="${availableSources}" noSelection="${['':'-']}"
                            optionKey="id" optionValue="name"/>
                  <input type="hidden" name="itemId" value="${itemId}" />
                  <input type="hidden" name="selector" value="#linkedSources" />
                  <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-plus"/>
                </tempvs:ajaxForm>
              </li>
            </g:if>
          </ul>
        </div>
      </div>
      <div class="row">
        <tempvs:carousel images="${images}" orientation="horizontal" styles="min-height: 25vw; max-height: 25vw;"/>
      </div>
      <g:if test="${editAllowed}">
        <div class="panel-group">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h4 class="panel-title">
                <a data-toggle="collapse" href="#collapse">
                  <g:message code="item.image.edit.collapse.title"/>
                  <span class="caret"></span>
                </a>
              </h4>
            </div>
            <div id="collapse" class="panel-collapse collapse">
              <ul class="list-group">
                <g:each var="image" in="${images}">
                  <g:set var="imageId" value="${image.id}"/>
                  <g:set var="imageInfo" value="${image.imageInfo}"/>
                  <li class="list-group-item row">
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
                          <tempvs:ajaxLink message="yes" controller="item" action="deleteItemImage" params="${[itemId: itemId, imageId: imageId]}" method="DELETE"/>
                          <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                        </tempvs:modalButton>
                      </span>
                      <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.editImage.tooltip')}">
                        <tempvs:modalButton id="editItemImage-${image.hashCode()}" classes="glyphicon glyphicon-picture">
                          <tempvs:ajaxForm action="editItemImage">
                            <tempvs:formField classes="image" type="file" name="image" label="item.image.label" />
                            <tempvs:formField classes="imageInfo" type="text" name="imageInfo" value="${imageInfo}" label="item.imageInfo.label" />
                            <input type="hidden" name="itemId" value="${itemId}"/>
                            <input type="hidden" name="imageId" value="${imageId}"/>
                            <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-floppy-disk" />
                          </tempvs:ajaxForm>
                        </tempvs:modalButton>
                      </span>
                      <g:if test="${imageInfo}">
                        <b><g:message code="item.imageInfo.label"/></b>:
                        <p>${imageInfo}</p>
                      </g:if>
                    </div>
                  </li>
                </g:each>
                <li class="list-group-item row">
                  <div class="row">
                    <tempvs:ajaxForm action="addItemImages">
                      <tempvs:imageUploader fieldName="imageUploadBeans" imageLabel="item.image.label" infoLabel="item.imageInfo.label"/>
                      <input type="hidden" name="item" value="${itemId}"/>
                      <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-floppy-disk"/>
                    </tempvs:ajaxForm>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </g:if>
    </g:if>
    <g:else>
      <g:message code="item.notFound.message"/>
    </g:else>
  </body>
</html>
