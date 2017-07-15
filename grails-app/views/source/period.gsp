<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${period?.id ?: g.message(code: 'source.period.title')}</title>
    </head>
    <body>
      <g:if test="${period}">
        <g:render template="/source/templates/navBar" model="${[period]}"/>
        <div class="row">
          <div><b><g:message code="periodization.period.value.label"/>:</b> ${period.value}</div>
        </div>
        <g:each in="${sources}" var="source">
          <div>
            <g:link controller="source" action="show" id="${source.id}" class="btn btn-default">
              ${source.name}
            </g:link>
          </div>
        </g:each>
        <hr/>
        <g:if test="${editAllowed}">
          <div class="row">
            <tempvs:modalButton id="sourceForm" message="source.createSource.button">
              <tempvs:ajaxForm action="createSource">
                <tempvs:formField type="text" name="name" label="source.name.label" />
                <tempvs:formField type="text" name="description" label="source.description.label" />
                <tempvs:ajaxSubmitButton value="source.createSource.button" />
              </tempvs:ajaxForm>
            </tempvs:modalButton>
          </div>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
