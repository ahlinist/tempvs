<g:set var="timestamp" value="${new Date().time}"/>

<div>
  <a data-toggle="modal" data-target="#modal-${image?.hashCode() ?: timestamp}" href="#">
    <g:render template="/image/templates/image" model="${[image: image, styles: styles]}"/>
  </a>
</div>

<span id="modal-${image?.hashCode() ?: timestamp}" class="modal fade" role="dialog">
  <span class="modal-dialog" style="display: table; padding:0; margin-top:10px; min-width: 35vw; width:auto;">
    <span class="modal-content" style="display: table-row;">
      <span class="modal-header" style="z-index:90;  position:absolute; right:0px; padding: 0px; display: table-row;">
        ${raw(body())}
      </span>
      <span class="modal-body" style="padding: 0px; margin: 50% 0 50% 0;">
        <g:render template="/image/templates/image"
                                  model="${[image: image, styles: 'height:90vh; max-width:90vw; width: auto;']}"/>
        <p class="text-center">${image?.imageInfo}</p>
      </span>
    </span>
  </span>
</span>
