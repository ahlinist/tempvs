<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - Passport</title>
  </head>
  <body>
    <div class="row">
        <g:render template="/passport/templates/navBar"/>
    </div>
    <g:if test="${passport}">
      <g:set var="passportId" value="${passport.id}"/>
      <div class="row">
        <div class="col-sm-6 ajax-form">
          <tempvs:ajaxSmartForm type="text" action="editPassportField" name="name" value="${passport.name}" objectId="${passportId}" label="passport.name.label"/>
          <tempvs:ajaxSmartForm type="text" action="editPassportField" name="description" value="${passport.description}" objectId="${passportId}" label="passport.description.label"/>
        </div>
        <div class="col-sm-6">
          <ul id="passportItems">
            <g:render template="/passport/templates/itemButton" model="${[itemMap: itemMap, passport: passport]}"/>
          </ul>
          <g:if test="${editAllowed}">
            <div class="panel-group">
              <div class="panel panel-default">
                <div class="panel-heading">
                  <h4 class="panel-title">
                    <a data-toggle="collapse" href="#addItemCollapse">
                      <g:message code="passport.addItem.collapse.title"/>
                      <span class="caret"></span>
                    </a>
                  </h4>
                </div>
                <div id="addItemCollapse" class="panel-collapse collapse">
                  <tempvs:ajaxForm controller="passport" action="addItem" selector="ul#passportItems">
                    <g:select class="col-sm-12 tempvs-form-field" name="itemId" from="${availableItems}" noSelection="${['':'-']}"
                              optionKey="id" optionValue="name"/>
                    <input type="hidden" name="passportId" value="${passportId}" />
                    <input type="number" name="quantity" value="1" />
                    <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-plus"/>
                  </tempvs:ajaxForm>
                </div>
              </div>
            </div>
          </g:if>
        </div>
      </div>
    </g:if>
    <g:else>
      <g:message code="${notFoundMessage}" args="${[id]}" />
    </g:else>
  </body>
</html>
