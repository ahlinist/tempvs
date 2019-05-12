<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title></title>
    <script type="module" src="/static/js/library.js"></script>
    <script src="/static/js/image.js"></script>
    <script src="/static/js/modal-carousel.js"></script>
  </head>
  <body>
    <content></content>
    <template id="library">
      <div>
        <div class="row">
          <u><a href="/library" id="breadcrumb-library"></a></u>
        </div>
        <div class="row">
          <div class="col-sm-2"></div>
          <div class="col-sm-8">
            <div id="welcome-section">
            </div>
            <template class="welcome-block">
              <div class="row">
                <div class="well text-center">
                  <span class="greeting-block"></span>
                  <a class="role-button btn btn-default"></a>
                </div>
              </div>
            </template>
            <h1 class="period-list-heading"></h1>
            <ul id="periods-section">
              <template class="period-template">
                <li class="row period-list-item">
                  <a class="btn btn-default col-sm-12 period-details-link" style="white-space: normal;">
                    <div class="col-sm-2">
                      <img class="period-thumbnail-image">
                    </div>
                    <div class="col-sm-10">
                      <b>
                        <p class="pull-left period-name"></p>
                      </b>
                      <br/>
                      <p style="text-align: justify;" class="period-short-description"></p>
                    </div>
                  </a>
                </li>
              </template>
            </ul>
          </div>
          <div class="col-sm-2"></div>
        </div>
      </div>
    </template>
    <template id="library-admin">
      <div>
        <div class="row">
          <span class="pull-left">
            <u><a href="/library" id="breadcrumb-library"></a></u>
          </span>
          <span class="pull-left">&nbsp;&gt;&nbsp;</span>
          <span class="pull-left">
            <u><a href="/library/admin" id="breadcrumb-admin"></a></u>
          </span>
        </div>
        <h1 id="admin-panel-heading"></h1>
        <div class="row">
          <div class="col-sm-2"></div>
          <div class="col-sm-8">
            <table class="table table-condensed">
              <thead>
                <tr>
                  <th id="user-header"></th>
                  <th id="authority-header"></th>
                  <th id="actions-header"></th>
                </tr>
              </thead>
              <tbody id="requests-section">
              </tbody>
              <template class="request-template">
                <tr>
                  <td>
                    <a class="btn btn-default user"></a>
                  </td>
                  <td>
                    <span class="authority"></span>
                  </td>
                  <td>
                    <span class="btn btn-default accept-request" style="color: green; border-radius: 150px !important;">
                      <span class="glyphicon glyphicon-ok"></span>
                    </span>
                    <span class="btn btn-default reject-request" style="color: red; border-radius: 150px !important;">
                      <span class="glyphicon glyphicon-remove"></span>
                    </span>
                  </td>
                </tr>
              </template>
            </table>
          </div>
          <div class="col-sm-2"></div>
        </div>
      </div>
    </template>
    <template id="library-period">
      <div>
        <div class="row">
          <span class="pull-left">
            <u><a href="/library" id="breadcrumb-library"></a></u>
          </span>
          <span class="pull-left">&nbsp;&gt;&nbsp;</span>
          <span class="pull-left">
            <u><a id="breadcrumb-period"></a></u>
          </span>
        </div>
        <h1 id="period-heading"></h1>
        <div class="row">
          <div class="col-sm-3">
            <img id="period-image">
          </div>
          <div style="text-align: justify; text-justify: inter-word;" id="period-long-description"></div>
        </div>
        <div class="row">
          <br/>
          <hr/>
          <div id="create-source-section">
            <button id="popup-button" class="btn btn-default pull-right" data-toggle="modal" data-target="#create-source-popup">
              <span class="fa fa-plus"></span>
            </button>
            <div id="create-source-popup" class="modal fade" role="dialog">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-body">
                    <div class="row ajax-form">
                      <form action="/api/library/source" onsubmit="library.createSource(this); return false;">
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="period"></label>
                          </div>
                          <div class="col-sm-6">
                            <input class="col-sm-12 tempvs-form-field" type="hidden" name="period">
                            <input class="col-sm-12 tempvs-form-field" type="text" name="fake-period" readonly>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="classification"></label>
                          </div>
                          <div class="col-sm-6">
                            <select class="col-sm-12" name="classification">
                              <option value=""></option>
                              <option value="CLOTHING"></option>
                              <option value="FOOTWEAR"></option>
                              <option value="HOUSEHOLD"></option>
                              <option value="WEAPON"></option>
                              <option value="ARMOR"></option>
                              <option value="OTHER"></option>
                            </select>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="type"></label>
                          </div>
                          <div class="col-sm-6">
                            <select class="col-sm-12" name="type">
                              <option value=""></option>
                              <option value="WRITTEN"></option>
                              <option value="GRAPHIC"></option>
                              <option value="ARCHAEOLOGICAL"></option>
                              <option value="OTHER"></option>
                            </select>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="name"></label>
                          </div>
                          <div class="col-sm-6">
                            <input class="col-sm-12 tempvs-form-field" type="text" name="name">
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="description"></label>
                          </div>
                          <div class="col-sm-6">
                            <input class="col-sm-12 tempvs-form-field" type="text" name="description">
                          </div>
                        </div>
                        <button class="btn btn-default submit-button"></button>
                      </form>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <h1 id="sources"></h1>
        <br/>
        <div class="row" style="border-top: 1px solid #CCC;">
          <div class="col-sm-2" id="search-section" style="border-right: 1px solid #CCC;">
            <form action="/api/library/source" onsubmit="library.search(this); return false;" autocomplete="off">
              <br/>
              <div>
                <input type="text" name="query">
                <button class="btn btn-default pull-right"><span class="fa fa-search"></span></button>
              </div>
              <br/>
              <div id="classification-search">
                <h4 class="well well-sm"></h4>
              </div>
              <div id="type-search">
                <h4 class="well well-sm"></h4>
              </div>
              <template id="search-template">
                <div>
                  <label></label>
                  <input class="pull-right" type="checkbox">
                </div>
              </template>
              <input type="hidden" name="period">
            </form>
          </div>
          <div class="col-sm-10">
            <table class="table table-hover" id="source-table">
              <thead>
                <tr>
                  <th id="table-name"></th>
                  <th id="table-description"></th>
                  <th id="table-classification"></th>
                  <th  id="table-type"></th>
                </tr>
              </thead>
              <tbody></tbody>
              <template id="source-template">
                <tr style="cursor: pointer;">
                  <td class="source-name"></td>
                  <td class="source-description"></td>
                  <td class="source-classification"></td>
                  <td class="source-type"></td>
                </tr>
              </template>
            </table>
            <img class="load-sources-spinner center-block hidden" src="/static/images/spinner.gif" style="margin-top:50px;">
          </div>
        </div>
      </div>
    </template>
    <template id="library-source">
      <div>
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
                        <span id="delete-image-wrapper" class="pull-right hidden">
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
      </div>
    </template>
  </body>
</html>
