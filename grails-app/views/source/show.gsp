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
        <div class="row">
          <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.editSource.button')}">
            <g:if test="${editAllowed}">
              <div class="row">
                <tempvs:modalButton id="sourceForm" cls="glyphicon glyphicon-edit">
                  <g:render template="/source/templates/sourceForm"
                      model="${[action: 'editSource', source: source, button: 'source.editSource.button', period: period]}"/>
                </tempvs:modalButton>
              </div>
            </g:if>
          </span>
        </div>
        <g:render template="/source/templates/source" model="${[source]}"/>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
