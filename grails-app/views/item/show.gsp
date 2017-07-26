<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - ${item?.name ?: g.message(code: 'item.show.title')}</title>
  </head>
  <body>
    <g:if test="${item}">
      <g:set var="itemImages" value="${item.images}"/>
      <g:set var="source" value="${item.source}"/>
      <g:render template="/item/templates/navBar" model="${[item, itemGroup, user, userProfile]}"/>
<ul class="nav nav-tabs">
  <li style="padding:0px; margin:0px;" class="col-sm-6 pull-left active">
    <a data-toggle="tab" href="#itemContent">
      <g:message code="item.item.link"/>
    </a>
  </li>
  <li style="padding:0px; margin:0px;" class="col-sm-6 pull-right">
    <a data-toggle="tab" href="#sourceContent">
      <g:message code="item.source.link"/>
    </a>
  </li>
</ul>
<div class="tab-content">
  <div id="itemContent" class="tab-pane fade in active">
    <div class="row">
      <div><b><g:message code="item.name.label"/>:</b> ${item.name}</div>
      <div><b><g:message code="item.description.label"/>:</b> ${item.description}</div>
      <div><b><g:message code="periodization.period.value.label"/>:</b> ${item.period.value}</div>
      <hr/>
      <div class="row">
        <div class="col-sm-4">
        </div>
        <div class="col-sm-4">
          <tempvs:carousel images="${item.images}" orientation="horizontal"/>
        </div>
        <div class="col-sm-4">
        </div>
      </div>
    </div>
    <g:if test="${editAllowed}">
      <div class="row">
        <tempvs:modalButton id="itemForm" message="item.updateItem.link">
          <g:render template="/item/templates/itemForm" model="${[action: 'editItem', button: 'item.updateItem.button', item: item]}"/>
        </tempvs:modalButton>
        <tempvs:modalButton id="deleteItem" size="modal-sm" message="item.delete.button">
          <g:message code='item.deleteConfirmation.text' args="${[item.name]}"/>
          <br/>
          <tempvs:ajaxLink message="yes" controller="item" action="deleteItem" id="${item.id}"/>
          <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
        </tempvs:modalButton>
      </div>
    </g:if>
  </div>
  <div id="sourceContent" class="tab-pane fade">
    <g:if test="${source}">
      <g:render template="/source/templates/source" model="${[source]}"/>
    </g:if>
  </div>
</div>
    </g:if>
    <g:else>
      <g:message code="item.notFound.message"/>
    </g:else>
  </body>
</html>
