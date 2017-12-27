<%@ page import="com.tempvs.item.Type"%>

<sec:ifLoggedIn>
  <g:set var="editAllowed" value="${true}"/>
</sec:ifLoggedIn>

<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${period?.id ?: g.message(code: 'source.period.title')}</title>
    </head>
    <body>
      <g:if test="${period}">
        <div class="row">
          <div class="col-sm-8">
            <g:render template="/source/templates/navBar" model="${[period]}"/>
          </div>
        </div>
        <div class="row">
          <div class="col-sm-12">
            <b><g:message code="periodization.period.value.label"/>:</b> ${period.value}
          </div>
        </div>
        <b><g:message code="source.list.label"/></b>:
        <g:if test="${sources}">
          <ul>
            <g:each in="${sources}" var="source">
              <g:set var="sourceId" value="${source.id}"/>
              <g:set var="sourceName" value="${source.name}"/>
              <li class="row" id="source-${sourceId}">
                <g:link controller="source" action="show" id="${sourceId}" class="btn btn-default col-sm-3">
                  ${sourceName}
                </g:link>
                <g:if test="${editAllowed}">
                  <div class="pull-left">
                    <span data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.delete.button')}">
                      <tempvs:modalButton id="deleteSource-${source.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
                        <g:message code='source.deleteConfirmation.text' args="${[sourceName]}"/>
                        <br/>
                        <tempvs:ajaxLink message="yes" controller="source" action="deleteSource" id="${sourceId}" method="DELETE" selector="li#source-${sourceId}"/>
                        <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                      </tempvs:modalButton>
                    </span>
                  </div>
                </g:if>
              </li>
            </g:each>
          </ul>
        </g:if>
        <div>
          <g:if test="${editAllowed}">
            <span data-toggle="tooltip" data-placement="right" title="${g.message(code: 'source.createSource.tooltip')}">
              <tempvs:modalButton id="sourceForm" classes="glyphicon glyphicon-plus">
                <tempvs:ajaxForm action="createSource">
                  <tempvs:imageUploader fieldName="imageUploadBeans" imageLabel="source.image.label" infoLabel="source.imageInfo.label"/>
                  <tempvs:formField type="text" name="fake-period" value="${period.value}" label="periodization.period.form.label" disabled="${true}"/>
                  <tempvs:formField type="select" name="type" from="${Type.values()}" optionKey="key" optionValue="value" label="item.type.dropdown.label"/>
                  <tempvs:formField type="text" name="name" value="${source?.name}" label="source.name.label" />
                  <tempvs:formField type="text" name="description" value="${source?.description}" label="source.description.label" />
                  <input type="hidden" name="period" value="${period?.key}"/>
                  <input type="hidden" name="sourceId" value="${source?.id}"/>
                  <tempvs:ajaxSubmitButton value="source.createSource.button" />
                </tempvs:ajaxForm>
              </tempvs:modalButton>
            </span>
          </g:if>
        </div>
      </g:if>
      <g:else>
        <g:message code="period.notFound.message"/>
      </g:else>
    </body>
</html>
