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
        <sec:ifLoggedIn>
          <hr/>
          <g:if test="${editAllowed}">
            <div class="row">
              <tempvs:modalButton id="sourceForm" message="source.createSource.button">
                <g:render template="/source/templates/sourceForm"
                    model="${[action: 'createSource', button: 'source.createSource.button', period: period]}"/>
              </tempvs:modalButton>
            </div>
          </g:if>
        </sec:ifLoggedIn>
      </g:if>
      <g:else>
        <g:message code="period.notFound.message"/>
      </g:else>
    </body>
</html>
