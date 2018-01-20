<div id="image-section">
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
              <g:set var="imageId" value="${image.id}"/>
              <g:set var="imageInfo" value="${image.imageInfo}"/>
              <li class="list-group-item row" id="sourceImage-${imageId}">
                <div class="col-sm-4">
                  <tempvs:modalImage image="${image}" classes="source-image" orientation="horizontal" styles="min-height: 20vw; max-height: 20vw;"/>
                </div>
                <div class="col-sm-4">
                </div>
                <div class="col-sm-4">
                  <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'source.deleteImage.button')}">
                    <tempvs:modalButton id="deleteSourceImage-${imageId}" size="modal-sm" classes="glyphicon glyphicon-trash">
                      <g:message code='source.imageDeleteConfirmation.text'/>
                      <br/>
                      <tempvs:ajaxLink controller="source" action="deleteImage" params="${[sourceId: sourceId, imageId: imageId]}" method="DELETE" selector="div#image-section">
                        <g:message code="yes"/>
                      </tempvs:ajaxLink>
                      <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                    </tempvs:modalButton>
                  </span>
                  <g:if test="${imageInfo}">
                    <b><g:message code="source.imageInfo.label"/></b>:
                    <p>${imageInfo}</p>
                  </g:if>
                </div>
              </li>
            </g:each>
            <li class="list-group-item row">
              <div class="row">
                <tempvs:ajaxForm controller="source" action="addImage" selector="div#image-section">
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
</div>
