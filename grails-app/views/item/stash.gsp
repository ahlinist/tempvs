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
              <tempvs:modalButton id="createGroup" classes="glyphicon glyphicon-plus">
                <tempvs:ajaxForm action="createGroup">
                  <tempvs:formField type="text" name="name" label="item.group.name.label" />
                  <tempvs:formField type="text" name="description" label="item.group.description.label" />
                  <input type="hidden" name="groupId"/>
                  <tempvs:ajaxSubmitButton value="item.createGroup.button" />
                </tempvs:ajaxForm>
              </tempvs:modalButton>
            </span>
          </span>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="item.stash.notFound.message"/>
      </g:else>
    </body>
</html>

