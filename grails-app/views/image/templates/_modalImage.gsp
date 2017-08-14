<g:set var="timestamp" value="${new Date().time}"/>

<div>
  <a data-toggle="modal" data-target="#modal-${image?.hashCode() ?: timestamp}" href="#">
    <tempvs:image image="${image}" orientation="${orientation}"/>
  </a>
</div>

<span id="modal-${image?.hashCode() ?: timestamp}" class="modal fade" role="dialog">
  <span class="modal-dialog" style="display: table; padding:0; margin-top:10px; width:0px;">
    <span class="modal-content" style="display: table-cell;">
      <span class="modal-body" style="padding: 0px;">
        <tempvs:image image="${image}" orientation="${orientation}" styles="min-height:90vh; max-height:90vh; max-width:90vw; width: auto;"/>
        <p class="text-center">${image?.imageInfo}</p>
      </span>
    </span>
  </span>
</span>

