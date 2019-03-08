<sec:ifAnyGranted roles="ROLE_CONTRIBUTOR">
  <g:set var="isContributor" value="${true}"/>
</sec:ifAnyGranted>
<sec:ifAnyGranted roles="ROLE_SCRIBE">
  <g:set var="isScribe" value="${true}"/>
</sec:ifAnyGranted>

<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title></title>
    <script src="/static/js/library.js"></script>
    <script>
      window.onload = function() {
        library.renderSourcePage('${sourceId}');
      };
    </script>
  </head>
  <body>
    <div class="row">
      <span class="pull-left">
        <u><a href="/library" id="breadcrumb-library"></a></u>
      </span>
      <span class="pull-left">&nbsp;&gt;&nbsp;</span>
      <span class="pull-left">
        <u><a id="breadcrumb-period"></a></u>
      </span>
      <span class="pull-left">&nbsp;&gt;&nbsp;</span>
      <span class="pull-left">
        <u><a id="breadcrumb-source-name"></a></u>
      </span>
    </div>
    <div class="row">
      <div class="col-sm-4">
        <div id="source-form" class="ajax-form">
          <div class="row" id="source-name">
            <span class="col-sm-5">
              <b></b> *
            </span>
            <span class="col-sm-7">
              <form style="display: inline;">
                <span class="hovering text-wrapper">
                  <span class="text-holder" style="line-height: 39px; padding-left: 15px;"></span>
                  <span class="fa fa-pencil smart-form-activator hidden"></span>
                </span>
                <span class="hidden input-wrapper">
                  <input style="margin: 4px 0px 3px 0px;" type="text" name="name" value="" autocomplete="off">
                </span><img style="width: 15px; height: 15px;" class="spinner hidden" src="/static/images/spinner-sm.gif">
              </form>
            </span>
          </div>
          <div class="row" id="source-description">
            <span class="col-sm-5">
              <b></b>
            </span>
            <span class="col-sm-7">
              <form style="display: inline;">
                <span class="hovering text-wrapper">
                  <span class="text-holder" style="line-height: 39px; padding-left: 15px;"></span>
                  <span class="fa fa-pencil smart-form-activator hidden"></span>
                </span>
                <span class="hidden input-wrapper">
                  <input style="margin: 4px 0px 3px 0px;" type="text" name="description" value="" autocomplete="off">
                </span><img style="width: 15px; height: 15px;" class="spinner hidden" src="/static/images/spinner-sm.gif">
              </form>
            </span>
          </div>
          <div class="row" id="source-classification">
            <span class="col-sm-5">
              <b></b>
            </span>
            <span class="col-sm-7">
              <span class="text-holder" style="padding-left: 15px;"></span>
            </span>
          </div>
          <div class="row" id="source-type">
            <span class="col-sm-5">
              <b></b>
            </span>
            <span class="col-sm-7">
              <span class="text-holder" style="padding-left: 15px;"></span>
            </span>
          </div>
          <div class="row" id="source-period">
            <span class="col-sm-5">
              <b></b>
            </span>
            <span class="col-sm-7">
              <span class="text-holder" style="padding-left: 15px;"></span>
            </span>
          </div>
        </div>

        <div class="row">
          <sec:ifAnyGranted roles="ROLE_ARCHIVARIUS">
            <div class="pull-left">
              <g:render template="/common/templates/modalButton"
                  model="${[elementId: 'deleteSource' + sourceId, size: 'modal-sm', message: 'source.delete.button']}">
                <g:message code='source.deleteConfirmation.text' args="${[source]}"/>
                <br/>
                <g:render template="/ajax/templates/ajaxLink"
                    model="${[controller: 'source', action: 'deleteSource', id: sourceId, method: 'DELETE', classes: 'btn btn-default']}">
                  <g:message code="yes"/>
                </g:render>
                <button type="button" class="btn btn-default" data-dismiss="modal"><g:message code="no"/></button>
              </g:render>
            </div>
          </sec:ifAnyGranted>
        </div>
      </div>
      <div class="col-sm-4">
      </div>
      <div class="col-sm-4">
      </div>
    </div>
  </body>
</html>
