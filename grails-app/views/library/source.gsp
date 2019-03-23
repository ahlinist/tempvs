<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title></title>
    <script src="/static/js/library.js"></script>
    <script src="/static/js/image.js"></script>
    <script src="/static/js/modal-carousel.js"></script>
  </head>
  <body>
    <div class="row" style="height:40px; padding: 0 15px;">
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
      <span id="delete-source-section" class="hidden pull-right">
        <button class="btn btn-default fa fa-trash" data-toggle="modal" data-target="#delete-source-button"></button>
        <div id="delete-source-button" class="modal fade" role="dialog">
          <div class="modal-dialog modal-sm">
            <div class="modal-content">
              <div class="modal-body">
                <span id="source-deletion-confirmation"></span>
                <br/>
                <form>
                  <button class="btn btn-default submit-button">
                    <span class="yes"></span>
                  </button>
                  <span class="btn btn-default" data-dismiss="modal">
                    <span class="no"></span>
                  </span>
                </form>
              </div>
            </div>
          </div>
        </div>
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
      </div>
      <div class="col-sm-4">
      </div>
      <div class="col-sm-4">
        <div id="image-container">
          <img id="default-image" src="/static/images/default_image.gif" style="width: 30vw;">
          <div id="image-carousel" class="hidden">
            <div class="btn" id="modal-activate-button" type="button" data-toggle="modal" data-target="#modal-source-images" data-local="#carousel-source-images">
              <span class="badge badge-notify" style="position: absolute; right:15px; top:0px;"></span>
              <div id="first-image-holder"></div>
            </div>
            <div class="modal fade" id="modal-source-images" tabindex="-1" role="dialog">
              <div class="modal-dialog" style="max-width: 90vw;">
                <div class="modal-content">
                  <div id="carousel-modal-header" class="modal-header" style="z-index:90;  position:absolute; right:0px; padding: 0px; display: table-row;">
                    <span id="delete-image-wrapper" class="pull-right" style="z-index:2147483647;  position:absolute; top: 0px; right:0px; padding: 0px; display: table-row;">
                      <button class="btn btn-default fa fa-trash" data-toggle="modal" data-target="#delete-image-button"></button>
                      <div id="delete-image-button" class="modal fade" role="dialog">
                        <div class="modal-dialog modal-sm">
                          <div class="modal-content">
                            <div class="modal-body">
                              <span id="image-deletion-confirmation"></span>
                              <br/>
                              <form>
                                <button class="btn btn-default submit-button">
                                  <span class="yes"></span>
                                </button>
                                <span class="btn btn-default" data-dismiss="modal">
                                  <span class="no"></span>
                                </span>
                              </form>
                            </div>
                          </div>
                        </div>
                      </div>
                    </span>
                  </div>
                  <div class="modal-body">
                    <div id="carousel-source-images" class="carousel slide">
                      <!-- Indicators -->
                      <ol class="carousel-indicators"></ol>
                      <template id="image-indicator">
                        <li data-target="#carousel"></li>
                      </template>
                      <!-- Wrapper for slides -->
                      <div class="carousel-inner"></div>
                      <template id="carousel-inner">
                        <div class="item">
                          <p class="text-center image-info"></p>
                        </div>
                      </template>
                      <!-- Left and right controls -->
                      <a class="left carousel-control" href="#carousel-source-images" data-slide="prev" style="background:none">
                        <span class="glyphicon glyphicon-chevron-left"></span>
                        <span class="sr-only">Previous</span>
                      </a>
                      <a class="right carousel-control" href="#carousel-source-images" data-slide="next" style="background:none">
                        <span class="glyphicon glyphicon-chevron-right"></span>
                        <span class="sr-only">Next</span>
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div>
              <form class="ajax-form" id="image-upload-form" onsubmit="library.uploadImage(this); return false;">
                <div class="row">
                  <div class="col-sm-5">
                    <label for="image"></label>
                  </div>
                  <div class="col-sm-7">
                    <label class="btn btn-default col-sm-12">
                      <span id="select-file-button" class="placeholder"><i></i></span>
                      <input type="file" name="image" onchange="img.setFileUploadPlaceholder(this);" hidden>
                    </label>
                  </div>
                </div>
                <div class="row">
                  <div class="col-sm-5">
                    <label for="imageInfo"></label>
                  </div>
                  <div class="col-sm-7">
                    <input class="col-sm-12 tempvs-form-field" type="text" name="imageInfo">
                  </div>
                </div>
                <br/>
                <button class="btn btn-default submit-button"></button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
