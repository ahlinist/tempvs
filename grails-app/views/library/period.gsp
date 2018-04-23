<sec:ifLoggedIn>
  <g:set var="editAllowed" value="${true}"/>
</sec:ifLoggedIn>

<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${period?.value ?: g.message(code: 'library.period.title')}</title>
    </head>
    <body>
      <g:if test="${period}">
        <div class="row">
          <div class="col-sm-8">
            <g:render template="/library/templates/navBar" model="${[period]}"/>
          </div>
        </div>
        <h1>${period.value}</h1>
        <div class="row">
          <div class="col-sm-3">
            <asset:image src="periodization/${period.id}.jpg"/>
          </div>
          <div style="text-align: justify; text-justify: inter-word;">
            <g:message code="periodization.${period.id}.long.description"/>
          </div>
        </div>
        <div class="row">
          <h1><g:message code="library.source.list.label"/></h1>
          <g:each var="sourceType" in="${sourceTypes}">
            <g:set var="groupedSources" value="${sources.findAll {it.sourceType == sourceType}}"/>
            <div class="col-sm-3">
              <h2><g:message code="source.sourceType.${sourceType.id}.value"/></h2>
              <div>
                <g:if test="${groupedSources}">
                  <ul>
                    <g:each in="${groupedSources}" var="source">
                      <g:set var="sourceId" value="${source.id}"/>
                      <g:set var="sourceName" value="${source.name}"/>
                      <li class="row" id="source-${sourceId}">
                        <g:link controller="source" action="show" id="${sourceId}" class="btn btn-default col-sm-12">
                          ${sourceName}
                        </g:link>
                      </li>
                    </g:each>
                  </ul>
                </g:if>
                <g:else>
                  <div class="text-center">
                    <i><g:message code="source.sourceType.list.empty"/></i>
                  </div>
                </g:else>
              </div>
            </div>
          </g:each>
        </div>
        <div class="row">
          <br/>
          <hr/>
          <div class="pull-right">
            <g:if test="${editAllowed}">
              <span data-toggle="tooltip" data-placement="right" title="${g.message(code: 'source.create.tooltip')}">
                <g:render template="/common/templates/modalButton"
                    model="${[id: 'sourceForm', message: 'source.add.button']}">
                  <tempvs:ajaxForm controller="source" action="createSource">
                    <g:render template="/image/templates/imageUploader"/>
                    <tempvs:formField type="text" name="fake-period" value="${period.value}" label="periodization.period.form.label" disabled="${true}"/>
                    <tempvs:formField type="select" name="itemType" from="${itemTypes}" optionKey="key" optionValue="value" label="item.itemType.dropdown.label" mandatory="${true}"/>
                    <tempvs:formField type="select" name="sourceType" from="${sourceTypes}" optionKey="key" optionValue="value" label="source.sourceType.dropdown.label" mandatory="${true}"/>
                    <tempvs:formField type="text" name="name" value="${source?.name}" label="source.name.label" mandatory="${true}"/>
                    <tempvs:formField type="text" name="description" value="${source?.description}" label="source.description.label" />
                    <input type="hidden" name="period" value="${period?.key}"/>
                    <input type="hidden" name="sourceId" value="${source?.id}"/>
                    <tempvs:ajaxSubmitButton value="source.create.button" />
                  </tempvs:ajaxForm>
                </g:render>
              </span>
            </g:if>
          </div>
        </div>
      </g:if>
      <g:else>
        <g:message code="period.notFound.message"/>
      </g:else>
    </body>
</html>
