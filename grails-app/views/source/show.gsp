<sec:ifLoggedIn>
  <g:set var="editAllowed" value="${true}"/>
</sec:ifLoggedIn>

<!DOCTYPE html>
<html>
    <head>
      <meta name="layout" content="main"/>
      <title>Tempvs - ${source?.name ?: g.message(code: 'source.show.title')}</title>
    </head>
    <body>
      <g:if test="${source}">
        <g:set var="sourceId" value="${source.id}"/>
        <div class="row">
          <div class="col-sm-8">
            <g:render template="/source/templates/navBar" model="${[period, source]}"/>
          </div>
          <div class="col-sm-4">
          </div>
        </div>
        <div class="row">
          <div class="col-sm-8 ajax-form">
            <tempvs:ajaxSmartForm type="text" action="editSourceField" name="name" value="${source.name}" objectId="${sourceId}" label="source.name.label"/>
            <tempvs:ajaxSmartForm type="text" action="editSourceField" name="description" value="${source.description}" objectId="${sourceId}" label="source.description.label"/>
            <tempvs:ajaxSmartForm type="text" value="${source.type.value}" label="item.type.dropdown.label" editAllowed="${false}"/>
            <tempvs:ajaxSmartForm type="text" value="${source.period.value}" label="periodization.period.dropdown.label" editAllowed="${false}"/>
          </div>
        </div>
        <div class="row">
          <tempvs:carousel images="${images}" orientation="horizontal" styles="min-height: 25vw; max-height: 25vw;"/>
        </div>
        <sec:ifLoggedIn>
          <div class="panel-group">
            <div class="panel panel-default">
              <div class="panel-heading">
                <h4 class="panel-title">
                  <a data-toggle="collapse" href="#collapse">
                    <g:message code="source.image.edit.collapse.title"/>
                    <span class="caret"></span>
                  </a>
                </h4>
              </div>
              <div id="collapse" class="panel-collapse collapse">
                <ul class="list-group" id="sourceImages">
                  <g:each var="image" in="${images}">
                    <g:render template="/source/templates/addImageForm" model="${[image: image, sourceId: sourceId]}"/>
                  </g:each>
                  <li class="list-group-item row">
                    <div class="row">
                      <tempvs:ajaxForm controller="source" action="addImage" selector="ul#sourceImages">
                        <tempvs:formField type="file" name="imageUploadBean.image" label="source.image.label" />
                        <tempvs:formField type="text" name="imageUploadBean.imageInfo" label="source.imageInfo.label" />
                        <input type="hidden" name="sourceId" value="${sourceId}"/>
                        <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-floppy-disk"/>
                      </tempvs:ajaxForm>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </sec:ifLoggedIn>
      </g:if>
      <g:else>
        <g:message code="source.notFound.message"/>
      </g:else>
    </body>
</html>
