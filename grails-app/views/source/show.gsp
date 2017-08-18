<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${source?.name ?: g.message(code: 'source.show.title')}</title>
    </head>
    <body>
      <g:if test="${source}">
        <div class="row">
          <div class="col-sm-8">
            <g:render template="/source/templates/navBar" model="${[period, source]}"/>
          </div>
          <div class="col-sm-4">
            <b><g:message code="actions.label"/></b>:
            <br/>
            <g:if test="${editAllowed}">
              <div class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.editSource.button')}">
                <tempvs:modalButton id="sourceForm" classes="glyphicon glyphicon-pencil">
                  <g:render template="/source/templates/sourceForm"
                      model="${[action: 'editSource', source: source, button: 'source.editSource.button', period: period]}"/>
                </tempvs:modalButton>
              </div>
            </g:if>
          </div>
        </div>
        <div class="row">
          <g:render template="/source/templates/source" model="${[source]}"/>
        </div>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
