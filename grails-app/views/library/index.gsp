<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Tempvs - <g:message code="library.title"/></title>
    <script>
      window.onload = function() {
        library.welcomePage();
      };
    </script>
  </head>
  <body>
    <div class="row">
      <div class="col-sm-8">
        <g:render template="/library/templates/navBar"/>
      </div>
    </div>
    <div class="row">
      <div class="col-sm-2"></div>
      <div class="col-sm-8">
        <div id="welcome-section">
        </div>
        <template class="welcome-block">
          <div class="row">
            <div class="well text-center">
              <span class="greeting-block"></span>
              <span class="role-button btn btn-default"></span>
            </div>
          </div>
        </template>
        <ul>
          <h1><g:message code="periodization.list.title"/></h1>
          <g:each in="${periods}" var="period">
            <li class="row">
              <g:link controller="library" action="period" id="${period.id}" class="btn btn-default col-sm-12" style="white-space: normal;">
                <div class="col-sm-2">
                  <asset:image src="periodization/thumbnails/${period.id}.jpg"/>
                </div>
                <div class="col-sm-10">
                  <b>
                    <p class="pull-left">${period.value}</p>
                  </b>
                  <br/>
                  <p style="text-align: justify;"><g:message code="periodization.${period.id}.short.description"/></p>
                </div>
              </g:link>
            </li>
          </g:each>
        </ul>
      </div>
      <div class="col-sm-2"></div>
    </div>
  </body>
</html>
