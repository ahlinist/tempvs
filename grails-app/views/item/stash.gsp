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
          <b><g:message code="item.groups.message"/></b>:
          <g:render template="/item/templates/groupList"/>
        </div>
        <g:if test="${editAllowed}">
          <span class="row">
            <span data-toggle="tooltip" data-placement="right" title="${g.message(code: 'item.createGroup.tooltip')}">
              <g:render template="/common/templates/modalButton"
                  model="${[id: 'createGroup', icon: 'glyphicon glyphicon-plus']}">
                <tempvs:ajaxForm action="createGroup">
                  <g:render template="/common/templates/formField" model="${[type: 'text', name: 'name', label: 'item.group.name.label', mandatory: true]}"/>
                  <g:render template="/common/templates/formField" model="${[type: 'text', name: 'description', label: 'item.group.description.label']}"/>
                  <g:render template="/ajax/templates/submitButton">
                    <g:message code="item.createGroup.button"/>
                  </g:render>
                </tempvs:ajaxForm>
              </g:render>
            </span>
          </span>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="item.stash.notFound.message"/>
      </g:else>
    </body>
</html>
