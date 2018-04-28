<%@ page import="grails.converters.JSON" %>
<g:set var="slideMapping" value="${images.collectEntries { [(images.indexOf(it)): it.id] } as JSON}"/>
<div id="modal-carousel">
  <g:set var="min" value="1"/>
  <g:if test="${images}">
    <div class="text-right">
      <span type="button" class="btn" data-toggle="modal" data-target="#modal-${images.hashCode()}" data-local="#carousel-${images.hashCode()}" onclick="modalCarousel.init(${slideMapping})">
        <g:set var="image" value="${images.first()}"/>
        <span class="badge badge-notify" style="position: absolute; right:15px; top:0px;">${images.size()}</span>
        <g:render template="/image/templates/image" model="${[image: image]}"/>
      </span>
    </div>
    <div class="modal fade" id="modal-${images.hashCode()}" tabindex="-1" role="dialog">
      <div class="modal-dialog" style="max-width: 90vw;">
        <div class="modal-content">
          <div class="modal-header" style="z-index:90;  position:absolute; right:0px; padding: 0px; display: table-row;">
            <g:if test="${editAllowed}">
              <span class="pull-right" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'image.delete.tooltip')}">
                <g:render template="/common/templates/modalButton"
                    model="${[id: 'deleteImage', size: 'modal-sm', icon: 'glyphicon glyphicon-trash']}">
                  <g:message code='image.delete.confirmation.text'/>
                  <br/>
                  <span class="btn btn-default submit-button" onclick="modalCarousel.deleteImage(this, '${controllerName}', '${objectId}');">
                    <span class="">
                      <g:message code="yes"/>
                    </span>
                  </span>
                  <button type="button" class="btn btn-default" onclick="$('#deleteImage').modal('hide');"><g:message code="no"/></button>
                </g:render>
              </span>
            </g:if>
          </div>
          <div class="modal-body">
            <div id="carousel-${images.hashCode()}" class="carousel slide">
                <!-- Indicators -->
                <ol class="carousel-indicators">
                  <g:each in="${images}" status="i" var="image">
                    <li data-target="#carousel" data-slide-to="${i}" class="${(i == 0) ? 'active' : ''}"></li>
                  </g:each>
                </ol>

                <!-- Wrapper for slides -->
                <div class="carousel-inner">
                  <g:each in="${images}" var="image" status="i">
                    <div class="item ${i == 0 ? 'active' : ''}">
                      <g:render template="/image/templates/image"
                          model="${[image: image, styles: 'height:90vh; max-width:90vw; width: auto;']}"/>
                      <p class="text-center">${image.imageInfo}</p>
                    </div>
                  </g:each>
                </div>

                <!-- Left and right controls -->
                <a class="left carousel-control" href="#carousel-${images.hashCode()}" data-slide="prev" style="background:none">
                  <span class="glyphicon glyphicon-chevron-left"></span>
                  <span class="sr-only">Previous</span>
                </a>
                <a class="right carousel-control" href="#carousel-${images.hashCode()}" data-slide="next" style="background:none">
                  <span class="glyphicon glyphicon-chevron-right"></span>
                  <span class="sr-only">Next</span>
                </a>
              </div>
          </div>
        </div>
      </div>
    </div>
  </g:if>
  <g:if test="${editAllowed}">
    <div class="panel-group">
      <div class="panel panel-default">
        <div class="panel-heading">
          <h4 class="panel-title">
            <a data-toggle="collapse" href="#addImageCollapse" onclick="imageUploader.init('imageUploadBeans', ${min});">
              <g:message code="image.add.collapse.title"/>
              <span class="caret"></span>
            </a>
          </h4>
        </div>
        <div id="addImageCollapse" class="panel-collapse collapse">
          <div class="row">
            <g:render template="/ajax/templates/ajaxForm"
                model="${[controller: controllerName, action: 'addImages', selector: 'div#modal-carousel']}">
              <g:render template="/image/templates/imageUploader" model="${[min: min]}"/>
              <input type="hidden" name="objectId" value="${objectId}"/>
              <g:render template="/ajax/templates/submitButton" model="${[onclick: '\$(".carousel").off("slide.bs.carousel");']}">
                <g:message code="images.upload.button"/>
              </g:render>
            </g:render>
          </div>
        </div>
      </div>
    </div>
  </g:if>
</div>
