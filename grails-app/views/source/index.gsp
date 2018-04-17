<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="source.library.title"/></title>
    </head>
    <body>
      <g:render template="/source/templates/navBar"/>
      <h1 class="text-center"><g:message code="periodization.list.title"/></h1>
      <div class="row">
        <div class="col-sm-2"></div>
        <ul class="col-sm-8">
          <g:each in="${periods}" var="period">
            <li class="row">
              <g:link action="period" id="${period.id}" class="btn btn-default col-sm-12" style="white-space: normal;">
                <div class="col-sm-2">
                  <asset:image src="periodization/${period.id}.jpg"/>
                </div>
                <div class="col-sm-10">
                  <b>
                    <p class="pull-left">${period.value}</p>
                  </b>
                  <br/>
                  <p style="text-align: justify;">${period.description}</p>
                </div>
              </g:link>
            </li>
          </g:each>
        </ul>
        <div class="col-sm-2"></div>
      </div>
    </body>
</html>
