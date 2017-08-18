<%@ page import="com.tempvs.periodization.Period"%>
<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="source.library.title"/></title>
    </head>
    <body>
      <g:render template="/source/templates/navBar"/>
      <b><g:message code="source.period.list.title"/></b>:
      <ul>
        <g:each in="${Period.values()}" var="period">
          <li class="row">
            <g:link action="period" id="${period.id}" class="btn btn-default col-sm-3">
              ${period.value}
            </g:link>
          </li>
        </g:each>
      </ul>
    </body>
</html>
