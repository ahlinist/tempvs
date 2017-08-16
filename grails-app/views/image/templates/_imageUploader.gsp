<script>
  var count = 0;
  var container = ".${fieldName}-container"

  function createFields() {
    count++;
    var imageField = cloneField(".image").insertAfter($('hr:last'));
    var imageInfoField = cloneField(".imageInfo").insertAfter($(imageField));
    $(document.createElement('hr')).insertAfter($(imageInfoField));
  }

  function cloneField(fieldCls) {
    var fieldValue = "${fieldName}[" + count + "]" + fieldCls;
    var section = $(fieldCls + ':last').clone();
    $(section).find("input").attr('name', fieldValue).attr('id', fieldValue).val('');
    $(section).find("label").attr('for', fieldValue);
    return section;
  }
</script>
<div class="${fieldName}-container">
  <tempvs:formField cls="image" type="file" name="${fieldName}[0].image" label="${imageLabel}" />
  <tempvs:formField cls="imageInfo" type="text" name="${fieldName}[0].imageInfo" label="${infoLabel}" />
  <hr/>
</div>
<div class="row">
  <span class="btn btn-default glyphicon glyphicon-plus pull-right" data-toggle="tooltip"
      data-placement="bottom" title="${g.message(code: 'image.addImage.tooltip')}" onclick="createFields();"></span>
</div>
