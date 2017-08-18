<%@ page import="com.tempvs.periodization.Period"%>
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
          <div class="col-sm-4">
            <b><g:message code="actions.label"/></b>:
          </div>
        </div>
        <div class="row">
          <div class="col-sm-8">
            <b><g:message code="periodization.period.value.label"/>:</b> ${period.value}
          </div>
          <div class="col-sm-4">
            <g:if test="${editAllowed}">
              <div class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.createSource.tooltip')}">
                <tempvs:modalButton id="sourceForm" classes="glyphicon glyphicon-plus">
                  <g:render template="/source/templates/sourceForm"
                      model="${[action: 'createSource', button: 'source.createSource.button', period: period]}"/>
                </tempvs:modalButton>
              </div>
            </g:if>
          </div>
        </div>
        <g:if test="${sources}">
          <b><g:message code="source.list.label"/></b>:
          <ul>
            <g:each in="${sources}" var="source">
              <li class="row">
                <g:link controller="source" action="show" id="${source.id}" class="btn btn-default col-sm-3">
                  ${source.name}
                </g:link>
              </li>
            </g:each>
          </ul>
        </g:if>
      </g:if>
      <g:else>
        <g:message code="period.notFound.message"/>
      </g:else>
    </body>
</html>
