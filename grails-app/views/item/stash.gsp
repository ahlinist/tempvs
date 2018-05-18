<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.stash.title"/></title>
    </head>
    <body>
      <div class="row">
        <g:render template="/item/templates/navBar" model="${[item, itemGroup, user, userProfile]}"/>
      </div>
      <g:if test="${user}">
        <div class="row">
          <div class="col-sm-6">
            <b><g:message code="item.groups.message"/></b>:
            <ul>
              <g:if test="${itemGroups}">
                <g:each var="itemGroup" in="${itemGroups}">
                  <g:set var="itemGroupId" value="${itemGroup.id}"/>
                  <g:set var="itemGroupName" value="${itemGroup.name}"/>
                  <li id="itemGroup-${itemGroupId}">
                    <div class="row">
                      <g:link class="btn btn-default col-sm-3" controller="item" action="group" id="${itemGroupId}" data-toggle="tooltip" data-placement="bottom" title="${itemGroup.description}">
                          ${itemGroupName}
                      </g:link>
                    </div>
                  </li>
                </g:each>
              </g:if>
              <g:else>
                <i><g:message code="item.no.groups.message"/></i>
              </g:else>
            </ul>
          </div>
          <div class="col-sm-6">
            <g:if test="${editAllowed}">
              <div class="pull-right">
                <g:render template="/common/templates/modalButton"
                    model="${[id: 'createGroup', message: 'item.create.group.button']}">
                  <g:render template="/ajax/templates/ajaxForm" model="${[controller: 'item', action: 'createGroup']}">
                    <g:render template="/common/templates/formField" model="${[type: 'text', name: 'name', label: 'item.group.name.label', mandatory: true]}"/>
                    <g:render template="/common/templates/formField" model="${[type: 'text', name: 'description', label: 'item.group.description.label']}"/>
                    <g:render template="/ajax/templates/submitButton">
                      <g:message code="item.createGroup.button"/>
                    </g:render>
                  </g:render>
                </g:render>
              </div>
            </g:if>
          </div>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.stash.notFound.message"/>
      </g:else>
    </body>
</html>
