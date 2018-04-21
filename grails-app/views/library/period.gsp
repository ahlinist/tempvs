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
        <h1><g:message code="library.source.list.label"/></h1>
        <g:render template="/library/templates/sourceList"/>
        <div>
          <g:if test="${editAllowed}">
            <span data-toggle="tooltip" data-placement="right" title="${g.message(code: 'source.createSource.tooltip')}">
              <tempvs:modalButton id="sourceForm" classes="glyphicon glyphicon-plus">
                <tempvs:ajaxForm controller="source" action="createSource">
                  <tempvs:imageUploader fieldName="imageUploadBeans"/>
                  <tempvs:formField type="text" name="fake-period" value="${period.value}" label="periodization.period.form.label" disabled="${true}"/>
                  <tempvs:formField type="select" name="itemType" from="${itemTypes}" optionKey="key" optionValue="value" label="item.itemType.dropdown.label" mandatory="${true}"/>
                  <tempvs:formField type="select" name="sourceType" from="${sourceTypes}" optionKey="key" optionValue="value" label="source.sourceType.dropdown.label" mandatory="${true}"/>
                  <tempvs:formField type="text" name="name" value="${source?.name}" label="source.name.label" mandatory="${true}"/>
                  <tempvs:formField type="text" name="description" value="${source?.description}" label="source.description.label" />
                  <input type="hidden" name="period" value="${period?.key}"/>
                  <input type="hidden" name="sourceId" value="${source?.id}"/>
                  <tempvs:ajaxSubmitButton value="source.createSource.button" />
                </tempvs:ajaxForm>
              </tempvs:modalButton>
            </span>
          </g:if>
        </div>
      </g:if>
      <g:else>
        <g:message code="period.notFound.message"/>
      </g:else>
    </body>
</html>
