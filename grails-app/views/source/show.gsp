<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${source?.name ?: g.message(code: 'source.show.title')}</title>
    </head>
    <body>
      <g:if test="${source}">
        <g:render template="/source/templates/navBar" model="${[period, source]}"/>
        <g:render template="/source/templates/source" model="${[source]}"/>
        <sec:ifLoggedIn>
          <hr/>
          <g:if test="${editAllowed}">
            <div class="row">
              <tempvs:modalButton id="sourceForm" message="source.editSource.button">
                <g:render template="/source/templates/sourceForm"
                    model="${[action: 'editSource', source: source, button: 'source.editSource.button', period: period]}"/>
              </tempvs:modalButton>
            </div>
          </g:if>
        </sec:ifLoggedIn>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
