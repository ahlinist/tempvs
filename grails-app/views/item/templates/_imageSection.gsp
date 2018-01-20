<div id="image-section">
  <div class="row">
    <tempvs:carousel images="${images}" orientation="horizontal" styles="min-height: 25vw; max-height: 25vw;"/>
  </div>
  <g:if test="${editAllowed}">
    <div class="panel-group">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h4 class="panel-title">
            <a data-toggle="collapse" href="#imageCollapse">
              <g:message code="item.image.edit.collapse.title"/>
              <span class="caret"></span>
            </a>
          </h4>
        </div>
        <div id="imageCollapse" class="panel-collapse collapse">
          <ul class="list-group" id="itemImages">
            <g:each var="image" in="${images}">
              <g:set var="imageId" value="${image.id}"/>
              <g:set var="imageInfo" value="${image.imageInfo}"/>
              <li class="list-group-item row" id="itemImage-${imageId}">
                <div class="col-sm-4">
                  <tempvs:modalImage image="${image}" classes="item-image" orientation="horizontal" styles="min-height: 20vw; max-height: 20vw;"/>
                </div>
                <div class="col-sm-4">
                </div>
                <div class="col-sm-4">
                  <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.deleteImage.button')}">
                    <tempvs:modalButton id="deleteItemImage-${image.hashCode()}" size="modal-sm" classes="glyphicon glyphicon-trash">
                      <g:message code='item.imageDeleteConfirmation.text'/>
                      <br/>
                      <tempvs:ajaxLink controller="item" action="deleteImage" params="${[itemId: itemId, imageId: imageId]}" method="DELETE" selector="div#image-section">
                        <g:message code="yes"/>
                      </tempvs:ajaxLink>
                      <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
                    </tempvs:modalButton>
                  </span>
                  <g:if test="${imageInfo}">
                    <b><g:message code="item.imageInfo.label"/></b>:
                    <p>${imageInfo}</p>
                  </g:if>
                </div>
              </li>
            </g:each>
            <li class="list-group-item row">
              <div class="row">
                <tempvs:ajaxForm controller="item" action="addImage" selector="div#image-section">
                  <tempvs:formField type="file" name="imageUploadBean.image" label="item.image.label" />
                  <tempvs:formField type="text" name="imageUploadBean.imageInfo" label="item.imageInfo.label" />
                  <input type="hidden" name="itemId" value="${itemId}"/>
                  <tempvs:ajaxSubmitButton icon="glyphicon glyphicon-floppy-disk"/>
                </tempvs:ajaxForm>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </g:if>
</div>