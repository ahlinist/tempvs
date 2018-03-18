<div id="image-section">
  <div class="row">
    <tempvs:modalCarousel images="${images}" orientation="horizontal" styles="min-height: 25vw; max-height: 25vw;"/>
  </div>
  <g:if test="${editAllowed}">
    <div class="panel-group">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h4 class="panel-title">
            <a data-toggle="collapse" href="#imageCollapse">
              <g:message code="image.edit.collapse.title"/>
              <span class="caret"></span>
            </a>
          </h4>
        </div>
        <div id="imageCollapse" class="panel-collapse collapse">
          <ul class="list-group">
            <g:each var="image" in="${images}">
              <g:set var="imageId" value="${image.id}"/>
              <g:set var="imageInfo" value="${image.imageInfo}"/>
              <li class="list-group-item row">
                <div class="col-sm-4">
                  <tempvs:modalImage image="${image}" orientation="horizontal" styles="min-height: 20vw; max-height: 20vw;"/>
                </div>
                <div class="col-sm-4">
                </div>
                <div class="col-sm-4">
                  <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'image.delete.button')}">
                    <tempvs:modalButton id="delete${controllerName}Image-${image.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
                      <g:message code='image.delete.confirmation.text'/>
                      <br/>
                      <tempvs:ajaxLink controller="${controllerName}" action="deleteImage" params="${[objectId: objectId, imageId: imageId]}" method="DELETE" selector="div#image-section" classes="btn btn-default">
                        <g:message code="yes"/>
                      </tempvs:ajaxLink>
                      <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                    </tempvs:modalButton>
                  </span>
                  <g:if test="${imageInfo}">
                    <b><g:message code="image.info.label"/></b>:
                    <p>${imageInfo}</p>
                  </g:if>
                </div>
              </li>
            </g:each>
          </ul>
        </div>
      </div>
    </div>
  </g:if>
</div>
