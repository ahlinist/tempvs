<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.group.title"/></title>
    </head>
    <body>
      <g:if test="${itemGroup}">
        <g:render template="/item/templates/navBar" model="${[itemGroup, user, userProfile]}"/>
        <div class="col-sm-3">
          <b>${itemGroup.name}</b>
          <br/>
          <b>${itemGroup.description}</b>
          <hr/>
          <g:each var="item" in="${itemGroup.items}">
             <div>
               <g:link class="btn btn-default" action="show" id="${item.id}">${item.name}</g:link>
             </div>
          </g:each>
          <g:if test="${editAllowed}">
            <div class="row">
              <tempvs:modalButton id="itemForm" message="item.createItem.link">
                <g:render template="/item/templates/itemForm" model="${[action: 'createItem', button: 'item.createItem.button']}"/>
              </tempvs:modalButton>
              <tempvs:modalButton id="deleteGroup" size="modal-sm" message="item.group.delete.button">
                <g:message code='item.group.deleteConfirmation.text' args="${[itemGroup.name]}"/>
                <br/>
                <tempvs:ajaxLink message="yes" controller="item" action="deleteGroup" id="${itemGroup.id}"/>
                <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
              </tempvs:modalButton>
            </div>
          </g:if>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.group.notFound.message"/>
      </g:else>
    </body>
</html>
