<div class="row">
  <div class="row">
    <div class="col-sm-8 ajax-form">
      <tempvs:ajaxSmartForm type="text" action="editSourceField" name="name" value="${source.name}" objectId="${sourceId}" label="source.name.label" editAllowed="${editAllowed}"/>
      <tempvs:ajaxSmartForm type="text" action="editSourceField" name="description" value="${source.description}" objectId="${sourceId}" label="source.description.label" editAllowed="${editAllowed}"/>
      <tempvs:ajaxSmartForm type="text" name="period" value="${source.period.value}" label="periodization.period.dropdown.label" editAllowed="${false}"/>
    </div>
  </div>
  <div class="row">
    <tempvs:carousel images="${source.images}" orientation="horizontal" styles="min-height: 25vw; max-height: 25vw;"/>
  </div>
</div>

