<g:set var="fieldName" value="imageUploadBeans"/>
<div class="${fieldName}-mock-container">
  <g:render template="/common/templates/formField" model="${[type: 'file', name: ' ', label: 'image.label', classes: 'image hidden']}"/>
  <g:render template="/common/templates/formField" model="${[type: 'text', name: ' ', label: 'image.info.label', classes: 'imageInfo hidden']}"/>
</div>
<div class="row">
  <span class="btn btn-default pull-right ${fieldName}-image-uploader-add-button" onclick="imageUploader.createFields('${fieldName}');">
    <g:message code="image.uploader.add.button"/>
  </span>
</div>

<script>
  imageUploader.init("${fieldName}", "${min}", "${max}");
</script>
