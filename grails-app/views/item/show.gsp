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
          <ul id="linkedSources">
            <g:each var="source" in="${sources}">
              <g:render template="/item/templates/linkedSource" model="${[source: source, itemId: itemId]}"/>
            </g:each>
            <g:if test="${editAllowed}">
              <div class="panel-group">
                <div class="panel panel-default">
                  <div class="panel-heading">
                    <h4 class="panel-title">
                      <a data-toggle="collapse" href="#sourceCollapse">
                        <g:message code="item.linkSource.collapse.title"/>
                        <span class="caret"></span>
                      </a>
                    </h4>
                  </div>
                  <div id="sourceCollapse" class="panel-collapse collapse">
                    <li class="row">
                      <tempvs:ajaxForm controller="item" action="linkSource" selector="ul#linkedSources">
                        <g:select class="col-sm-12 tempvs-form-field" name="sourceId" from="${availableSources}" noSelection="${['':'-']}"
                                  optionKey="id" optionValue="name"/>
                        <input type="hidden" name="itemId" value="${itemId}" />
                        <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-plus"/>
                      </tempvs:ajaxForm>
                    </li>
                  </div>
                </div>
              </div>
            </g:if>
          </ul>
        </div>
      </div>
      <div class="row">
        <tempvs:carousel images="${images}" orientation="horizontal" styles="min-height: 25vw; max-height: 25vw;"/>
      </div>
      <g:if test="${editAllowed}">
        <div class="panel-group">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h4 class="panel-title">
                <a data-toggle="collapse" href="#imageCollapse">
                  <g:message code="item.image.edit.collapse.title"/>
                  <span class="caret"></span>
                </a>
              </h4>
            </div>
            <div id="imageCollapse" class="panel-collapse collapse">
              <ul class="list-group" id="itemImages">
                <g:each var="image" in="${images}">
                  <g:render template="/item/templates/addImageForm" model="${[image: image, itemId: itemId]}"/>
                </g:each>
                <li class="list-group-item row">
                  <div class="row">
                    <tempvs:ajaxForm controller="item" action="addImage" selector="ul#itemImages">
                      <tempvs:formField type="file" name="imageUploadBean.image" label="item.image.label" />
                      <tempvs:formField type="text" name="imageUploadBean.imageInfo" label="item.imageInfo.label" />
                      <input type="hidden" name="itemId" value="${itemId}"/>
                      <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-floppy-disk"/>
                    </tempvs:ajaxForm>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </g:if>
    </g:if>
    <g:else>
      <g:message code="item.notFound.message"/>
    </g:else>
  </body>
</html>
