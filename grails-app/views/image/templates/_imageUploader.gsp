<div class="${fieldName}-container">
  <tempvs:formField classes="image hidden" type="file" name=" " label="${imageLabel}" />
  <tempvs:formField classes="imageInfo hidden" type="text" name=" " label="${infoLabel}" />
</div>
<div class="row">
  <span class="btn btn-default pull-right" onclick="imageUploader.createFields('${fieldName}');">
    <g:message code="image.uploader.add.button"/>
  </span>
</div>

<script>
  imageUploader.count['${fieldName}'] = 0;
</script>
