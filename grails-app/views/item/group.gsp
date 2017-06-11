<g:set var="itemStash" value="${itemGroup?.itemStash}"/>
<g:set var="userProfile" value="${itemStash?.user?.userProfile}"/>

<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.group.title"/></title>
    </head>
    <body>
      <g:if test="${itemGroup}">
        <g:render template="/item/templates/navBar" model="${[itemGroup, itemStash, userProfile]}"/>
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
          <g:if test="${ownGroup}">
            <div class="row">
              <button type="button" class="btn btn-default" data-toggle="modal" data-target="#itemForm">
                <g:message code="item.createItem.link"/>
              </button>
              <button type="button" class="btn btn-default" data-toggle="modal" data-target="#deleteGroup">
                <g:message code="item.group.delete.button"/>
              </button>

              <g:render template="/templates/modal" model="${[modalId: 'itemForm']}">
                <g:render template="/item/templates/itemForm" model="${[action: 'createItem', button: 'item.createItem.button']}"/>
              </g:render>
              <g:render template="/templates/modal" model="${[modalId: 'deleteGroup', size: 'modal-sm']}">
                <g:message code='item.group.deleteConfirmation.text' args="${[itemGroup.name]}"/>
                <br/>
                <tempvs:ajaxLink message="yes" controller="item" action="deleteGroup" id="${itemGroup.id}"/>
                <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
              </g:render>
            </div>
          </g:if>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.group.notFound.message"/>
      </g:else>
    </body>
</html>
