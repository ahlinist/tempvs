<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.group.title"/></title>
    </head>
    <body>
      <div class="row">
        <g:render template="/item/templates/navBar" model="${[item, itemGroup, user, userProfile]}"/>
      </div>
      <g:if test="${itemGroup}">
        <g:set var="itemGroupId" value="${itemGroup.id}"/>
        <div class="row">
          <div class="row">
            <div class="ajax-form">
              <tempvs:ajaxSmartForm type="text" action="editItemGroupField" name="name" value="${itemGroup.name}" objectId="${itemGroupId}" label="item.group.name.label" editAllowed="${editAllowed}"/>
              <tempvs:ajaxSmartForm type="text" action="editItemGroupField" name="description" value="${itemGroup.description}" objectId="${itemGroupId}" label="item.group.description.label" editAllowed="${editAllowed}"/>
            </div>
          </div>
          <div class="row">
            <b><g:message code="item.items.message"/></b>:
            <ul>
              <g:each var="item" in="${items}">
                <g:set var="itemId" value="${item.id}"/>
                <g:set var="itemName" value="${item.name}"/>
                <li class="row" id="item-${itemId}">
                  <g:link class="btn btn-default col-sm-4" action="show" id="${itemId}"  data-toggle="tooltip" data-placement="bottom" title="${item.description}">${itemName}</g:link>
                  <g:if test="${editAllowed}">
                    <div class="pull-left">
                      <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.delete.button')}">
                        <tempvs:modalButton id="deleteItem-${item.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
                          <g:message code='item.deleteConfirmation.text' args="${[itemName]}"/>
                          <br/>
                          <tempvs:ajaxLink message="yes" controller="item" action="deleteItem" id="${itemId}" method="DELETE" selector="li#item-${itemId}"/>
                          <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                        </tempvs:modalButton>
                      </span>
                    </div>
                  </g:if>
                </li>
              </g:each>
            </ul>
          </div>
        </div>
        <g:if test="${editAllowed}">
          <span class="row">
            <span data-toggle="tooltip" data-placement="right" title="${g.message(code: 'item.createItem.tooltip')}">
              <tempvs:modalButton id="itemForm" classes="glyphicon glyphicon-plus">
                <tempvs:ajaxForm action="createItem">
                  <tempvs:imageUploader fieldName="imageUploadBeans" imageLabel="item.image.label" infoLabel="item.imageInfo.label"/>
                  <tempvs:formField type="text" name="name" label="item.name.label" />
                  <tempvs:formField type="text" name="description" label="item.description.label" />
                  <tempvs:formField type="select" name="period" from="${Period.values()}" optionKey="key" optionValue="value" label="periodization.period.dropdown.label"/>
                  <input type="hidden" name="itemGroup" value="${itemGroupId}"/>
                  <tempvs:ajaxSubmitButton value="item.createItem.button"/>
                </tempvs:ajaxForm>
              </tempvs:modalButton>
            </span>
          </span>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="item.group.notFound.message"/>
      </g:else>
    </body>
</html>
