<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="item"/>
    <title>Tempvs - ${item?.name ?: g.message(code: 'item.show.title')}</title>
  </head>
  <body>
    <g:if test="${item}">
      <g:set var="itemId" value="${item.id}"/>
      <g:set var="period" value="${item.period}"/>
      <div class="row">
        <g:if test="${editAllowed}">
          <div class="pull-right">
            <g:render template="/item/templates/deleteItemButton" model="${[item: item]}"/>
          </div>
          <div class="pull-right">
            <g:link action="editItemPage" id="${itemId}" class="btn btn-default glyphicon glyphicon-pencil"/>
          </div>
        </g:if>
      </div>
      <div class="row">
        <div class="col-sm-8 ajax-form">
          <tempvs:ajaxSmartForm type="text" action="editItemField" name="name" value="${item.name}" objectId="${itemId}" label="item.name.label" editAllowed="${editAllowed}"/>
          <tempvs:ajaxSmartForm type="text" action="editItemField" name="description" value="${item.description}" objectId="${itemId}" label="item.description.label" editAllowed="${editAllowed}"/>
          <tempvs:ajaxSmartForm type="select" action="editItemField" name="period" value="${period}" optionKey="key" optionValue="value" from="${Period.values()}" objectId="${itemId}" label="periodization.period.dropdown.label" editAllowed="${editAllowed}"/>
        </div>
      </div>
      <div class="row">
        <tempvs:carousel images="${images}" orientation="horizontal" styles="min-height: 25vw; max-height: 25vw;"/>
      </div>

      <g:message code="item.sources.label"/>
      <ul>
        <g:each var="source" in="${sources}">
          <li class="row">
            <g:link controller="source" action="show" id="${source.id}" class="btn btn-default col-sm-3">
              ${source.name}
            </g:link>
            <g:if test="${editAllowed}">
              <div class="pull-left">
                <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.unlink.button')}">
                  <tempvs:modalButton id="unlinkSource-${source.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
                    <g:message code='source.unlinkConfirmation.text' args="${[source.name]}"/>
                    <br/>
                    <tempvs:ajaxLink message="yes" controller="item" action="unlinkSource" params="${[itemId: itemId, sourceId: source.id]}" method="DELETE"/>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                  </tempvs:modalButton>
                </span>
              </div>
            </g:if>
          </li>
        </g:each>
        <g:if test="${editAllowed}">
          <li class="row">
            <div class="col-sm-3">
              <tempvs:ajaxForm controller="item" action="linkSource">
                <g:select class="col-sm-12 tempvs-form-field" name="sourceId" from="${availableSources}" noSelection="${['':'-']}"
                          optionKey="id" optionValue="name"/>
                <input type="hidden" name="itemId" value="${itemId}" />
                <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-plus"/>
              </tempvs:ajaxForm>
            </div>
          </li>
        </g:if>
      </ul>
    </g:if>
    <g:else>
      <g:message code="item.notFound.message"/>
    </g:else>
  </body>
</html>
