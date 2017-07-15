<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="source.library.title"/></title>
    </head>
    <body>
      <g:render template="/source/templates/navBar"/>
      <g:each in="${Period.values()}" var="period">
        <div>
          <g:link action="period" id="${period.id}" class="btn btn-default">
            ${period.value}
          </g:link>
        </div>
      </g:each>
    </body>
</html>
