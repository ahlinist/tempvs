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
          <div><b><g:message code="source.name.label"/>:</b> ${source.name}</div>
          <div><b><g:message code="source.description.label"/>:</b> ${source.description}</div>
          <div><b><g:message code="periodization.period.value.label"/>:</b> ${period.value}</div>
        </div>
        <sec:ifLoggedIn>
          <g:if test="${editAllowed}">
            <div class="row">
              <tempvs:modalButton id="sourceForm" message="source.editSource.button">
                <tempvs:ajaxForm action="editSource">
                  <tempvs:formField type="text" name="name" value="${source.name}" label="source.name.label" />
                  <tempvs:formField type="text" name="description" value="${source.description}" label="source.description.label" />
                  <tempvs:ajaxSubmitButton value="source.editSource.button" />
                </tempvs:ajaxForm>
              </tempvs:modalButton>
            </div>
          </g:if>
        </sec:ifLoggedIn>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
