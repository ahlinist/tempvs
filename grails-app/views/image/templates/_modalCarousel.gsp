<g:if test="${images}">
  <span type="button" class="btn" data-toggle="modal" data-target="#modal-${images.hashCode()}" data-local="#carousel-${images.hashCode()}">
    <tempvs:image image="${images.first()}" orientation="${orientation}" styles="${styles}"/>
  </span>

  <div class="modal fade" id="modal-${images.hashCode()}" tabindex="-1" role="dialog">
    <div class="modal-dialog" style="max-width: 90vw;">
      <div class="modal-content">
        <div class="modal-body">
          <div id="carousel-${images.hashCode()}" class="carousel slide" data-ride="carousel">
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
                    <tempvs:image image="${image}" orientation="${orientation}" styles="height:90vh; max-width:90vw; width: auto;"/>
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
