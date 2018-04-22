<g:set var="fieldName" value="imageUploadBeans"/>
<div class="${fieldName}-mock-container">
  <tempvs:formField classes="image hidden" type="file" name=" " label="image.label" />
  <tempvs:formField classes="imageInfo hidden" type="text" name=" " label="image.info.label" />
</div>
<div class="row">
  <span class="btn btn-default pull-right ${fieldName}-image-uploader-add-button" onclick="imageUploader.createFields('${fieldName}');">
    <g:message code="image.uploader.add.button"/>
  </span>
</div>

<script>
  imageUploader.init("${fieldName}", "${min}", "${max}");
</script>
