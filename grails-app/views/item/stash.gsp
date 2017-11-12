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
                <tempvs:ajaxForm action="createGroup">
                  <tempvs:formField type="text" name="name" label="item.group.name.label" />
                  <tempvs:formField type="text" name="description" label="item.group.description.label" />
                  <input type="hidden" name="groupId"/>
                  <tempvs:ajaxSubmitButton value="item.createGroup.button" />
                </tempvs:ajaxForm>
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
                  <g:set var="itemGroupId" value="${itemGroup.id}"/>
                  <g:set var="itemGroupName" value="${itemGroup.name}"/>
                  <li>
                    <div class="row">
                      <g:link class="btn btn-default col-sm-3" controller="item" action="group" id="${itemGroupId}" data-toggle="tooltip" data-placement="bottom" title="${itemGroup.description}">
                          ${itemGroupName}
                      </g:link>
                      <g:if test="${editAllowed}">
                        <div class="pull-left">
                          <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.group.delete.tooltip')}">
                            <tempvs:modalButton id="deleteGroup-${itemGroup.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
                              <g:message code='item.group.deleteConfirmation.text' args="${[itemGroupName]}"/>
                              <br/>
                              <tempvs:ajaxLink message="yes" controller="item" action="deleteGroup" id="${itemGroupId}" method="DELETE"/>
                              <button class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                            </tempvs:modalButton>
                          </span>
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

