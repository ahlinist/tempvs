<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.stash.title"/></title>
    </head>
    <body>
      <g:if test="${user}">
        <g:render template="/item/templates/navBar" model="${[user, userProfile]}"/>
        <div class="col-sm-3">
          <g:render template="/item/templates/itemGroups" model="${[itemGroups]}"/>
          <g:if test="${editAllowed}">
            <div class="row">
              <tempvs:modalButton id="createGroup" message="item.createGroup.link">
                <tempvs:ajaxForm action="createGroup">
                  <tempvs:formField type="text" name="name" label="item.group.name.label" />
                  <tempvs:formField type="text" name="description" label="item.group.description.label" />
                  <tempvs:ajaxSubmitButton value="item.createGroup.button" />
                </tempvs:ajaxForm>
              </tempvs:modalButton>
            </div>
          </g:if>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.stash.notFound.message"/>
      </g:else>
    </body>
</html>

