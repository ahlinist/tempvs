<%@ page import="com.tempvs.item.Type"%>
<%@ page import="com.tempvs.periodization.Period"%>

<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - ${item?.name ?: g.message(code: 'item.show.title')}</title>
  </head>
  <body>
    <div class="row">
        <g:render template="/item/templates/navBar" model="${[item, itemGroup, user, userProfile]}"/>
    </div>
    <g:if test="${item}">
      <g:set var="itemId" value="${item.id}"/>
      <g:set var="period" value="${item.period}"/>
      <div class="row">
        <div class="col-sm-8 ajax-form">
          <tempvs:ajaxSmartForm type="text" action="editItemField" name="name" value="${item.name}" objectId="${itemId}" label="item.name.label" editAllowed="${editAllowed}"/>
          <tempvs:ajaxSmartForm type="text" action="editItemField" name="description" value="${item.description}" objectId="${itemId}" label="item.description.label" editAllowed="${editAllowed}"/>
          <tempvs:ajaxSmartForm type="text" value="${item.type.value}" label="item.type.dropdown.label" editAllowed="${false}"/>
          <tempvs:ajaxSmartForm type="text" value="${period.value}" label="periodization.period.dropdown.label" editAllowed="${false}"/>
        </div>
        <div class="col-sm-4">
          <b><g:message code="item.sources.label"/></b>
          <g:render template="/item/templates/sources" model="${[itemId: itemId]}"/>
        </div>
      </div>
      <g:render template="/item/templates/imageSection" model="${[itemId: itemId]}"/>
    </g:if>
    <g:else>
      <g:message code="item.notFound.message"/>
    </g:else>
  </body>
</html>
