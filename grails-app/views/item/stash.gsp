<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="item.stash.title"/></title>
    </head>
    <body>
      <g:if test="${itemStash}">
        <g:render template="/item/templates/navBar" model="${[itemStash, userProfile]}"/>
        <div class="col-sm-3">
          <g:render template="/item/templates/itemGroups" model="${[itemStash]}"/>
          <g:if test="${ownStash}">
            <div class="row">
              <button type="button" class="btn btn-default" data-toggle="modal" data-target="#createGroup">
                <g:message code="item.createGroup.link"/>
              </button>

              <g:render template="/templates/modal" model="${[modalId: 'createGroup']}">
                <tempvs:ajaxForm action="createGroup">
                  <tempvs:formField type="text" name="name" label="item.group.name.label" />
                  <tempvs:formField type="text" name="description" label="item.group.description.label" />
                  <tempvs:ajaxSubmitButton value="item.createGroup.button" />
                </tempvs:ajaxForm>
              </g:render>
            </div>
          </g:if>
        </div>
      </g:if>
      <g:else>
        <g:message code="item.stash.notFound.message"/>
      </g:else>
    </body>
</html>

