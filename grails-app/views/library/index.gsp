<sec:ifAnyGranted roles="ROLE_CONTRIBUTOR">
  <g:set var="isContributor" value="${true}"/>
</sec:ifAnyGranted>
<sec:ifAnyGranted roles="ROLE_SCRIBE">
  <g:set var="isScribe" value="${true}"/>
</sec:ifAnyGranted>
<sec:ifAnyGranted roles="ROLE_ARCHIVARIUS">
  <g:set var="isArchivarius" value="${true}"/>
</sec:ifAnyGranted>

<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - <g:message code="library.title"/></title>
    </head>
    <body>
      <div class="row">
        <div class="col-sm-8">
          <g:render template="/library/templates/navBar"/>
        </div>
      </div>
      <div class="well">
        <g:if test="${isArchivarius}">
          <div class="text-center">
            <g:message code="library.archivarius.welcome.message"/>
          </div>
        </g:if>
        <g:elseif test="${isScribe}">
          <div class="text-center">
            <g:message code="library.scribe.welcome.message"/>
          </div>
        </g:elseif>
        <g:elseif test="${isContributor}">
          <div class="text-center">
            <g:message code="library.contributor.welcome.message"/>
          </div>
        </g:elseif>
        <g:else>
          <div class="text-center">
            <g:message code="library.guest.welcome.message"/>
          </div>
        </g:else>
      </div>
      <h1><g:message code="periodization.list.title"/></h1>
      <div class="row">
        <div class="col-sm-2"></div>
        <ul class="col-sm-8">
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
        <div class="col-sm-2"></div>
      </div>
    </body>
</html>
