<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="item"/>
      <title>Tempvs - <g:message code="item.stash.title"/></title>
    </head>
    <body>
      <g:if test="${user}">
        <g:if test="${editAllowed}">
          <div class="row">
            <div class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.createGroup.tooltip')}">
              <tempvs:modalButton id="createGroup" classes="glyphicon glyphicon-plus">
                <g:render template="/item/templates/groupForm" model="${[action: 'createGroup']}"/>
              </tempvs:modalButton>
            </div>
          </div>
        </g:if>
        <div class="row">
          <g:if test="${itemGroups}">
            <b><g:message code="item.groups.message"/></b>:
            <div class="row">
              <ul>
                <g:each var="itemGroup" in="${itemGroups.sort { it.id }}">
                  <li>
                    <div class="row">
                      <g:link class="btn btn-default col-sm-3" controller="item" action="group" id="${itemGroup.id}" data-toggle="tooltip" data-placement="bottom" title="${itemGroup.description}">
                          ${itemGroup.name}
                      </g:link>
                      <g:if test="${editAllowed}">
                        <div class="pull-left">
                          <g:render template="/item/templates/deleteGroupButton" model="${[itemGroup: itemGroup]}"/>
                        </div>
                      </g:if>
                    </div>
                  </li>
                </g:each>
              </ul>
            </div>
          </g:if>
          <g:else>
            <i><g:message code="item.noGroups.message"/></i>
          </g:else>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.stash.notFound.message"/>
      </g:else>
    </body>
</html>

