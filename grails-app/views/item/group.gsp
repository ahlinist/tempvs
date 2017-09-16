<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="item"/>
      <title>Tempvs - <g:message code="item.group.title"/></title>
    </head>
    <body>
      <g:if test="${itemGroup}">
        <g:set var="itemGroupId" value="${itemGroup.id}"/>
        <g:set var="items" value="${itemGroup.items}"/>      
        <div class="row">
          <div class="row">
            <div class="col-sm-8 ajax-form">
              <tempvs:ajaxSmartForm type="text" action="editItemGroupField" name="name" value="${itemGroup.name}" objectId="${itemGroupId}" label="item.group.name.label" editAllowed="${editAllowed}"/>
              <tempvs:ajaxSmartForm type="text" action="editItemGroupField" name="description" value="${itemGroup.description}" objectId="${itemGroupId}" label="item.group.description.label" editAllowed="${editAllowed}"/>
            </div>
            <div class="col-sm-4">
              <g:if test="${editAllowed}">
                <div class="row">
                  <div class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.createItem.tooltip')}">
                    <tempvs:modalButton id="itemForm" classes="glyphicon glyphicon-plus">
                      <g:render template="/item/templates/itemForm" model="${[action: 'createItem', button: 'item.createItem.button', itemGroup: itemGroup]}"/>
                    </tempvs:modalButton>
                  </div>
                </div>
              </g:if>
            </div>
          </div>
          <div class="row">
            <g:if test="${items}">
              <b><g:message code="item.items.message"/></b>:
              <ul>
                <g:each var="item" in="${items.sort { it.id }}">
                   <li class="row">
                     <g:link class="btn btn-default col-sm-4" action="show" id="${item.id}"  data-toggle="tooltip" data-placement="bottom" title="${item.description}">${item.name}</g:link>
                     <g:if test="${editAllowed}">
                       <div class="pull-left">
                         <g:link action="editItemPage" id="${item.id}" class="btn btn-default glyphicon glyphicon-pencil"/>
                       </div>
                       <div class="pull-left">
                         <g:render template="/item/templates/deleteItemButton" model="${[item: item]}"/>
                       </div>
                     </g:if>
                   </li>
                </g:each>
              </ul> 
            </g:if>
            <g:else>
              <i><g:message code="item.noItems.message"/></i>
            </g:else>
          </div>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.group.notFound.message"/>
      </g:else>
    </body>
</html>
