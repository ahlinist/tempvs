<!DOCTYPE html>
<html>
  <head>
    <script type="module" src="/static/js/stash.js"></script>
    <meta name="layout" content="main"/>
  </head>
  <body>
    <content></content>
    <template id="stash">
      <div id="stash-section">
        <div class="row">
          <u><a href="/stash" id="breadcrumb-stash"></a></u>
          <div id="create-group-section" class="pull-right hidden">
            <button id="popup-button" class="btn btn-default pull-right" data-toggle="modal" data-target="#create-source-popup">
              <span class="fa fa-plus"></span>
            </button>
            <div id="create-source-popup" class="modal fade" role="dialog">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-body">
                    <div class="row ajax-form">
                      <form action="/api/stash/group" onsubmit="stash.createGroup(this); return false;">
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
        <div class="row">
          <h1 id="group-list-heading"></h1>
          <div class="row">
            <div class="col-sm-2"></div>
            <div id="group-list" class="col-sm-8"></div>
            <div class="col-sm-2"></div>
          </div>
          <template id="group-list-item-template">
            <div>
              <a class="btn btn-default col-sm-4">
                <b class="group-name"></b>
                <p class="group-description"></p>
              </a>
            </div>
          </template>
        </div>
      </div>
    </template>
    <template id="item-group">
      <div id="group-section">
        <div class="row">
          <u><a id="breadcrumb-stash"></a></u>&nbsp;>&nbsp;
          <u><a id="breadcrumb-item-group"></a></u>
        </div>
        <div class="row">
          <div class="ajax-form group-form col-sm-6">
            <div class="row group-name">
              <span class="col-sm-5">
                <b class="group-name-label"></b> *
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
            <div class="row group-description">
              <span class="col-sm-5">
                <b class="group-description-label"></b>
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
          </div>
          <div class="col-sm-6">
            <button style="position: absolute; right: 0;" class="create-item-button btn btn-default hidden" data-toggle="modal" data-target="#create-item-popup">
              <span class="fa fa-plus"></span>
              <span class="text-holder"></span>
            </button>
            <div id="create-item-popup" class="modal fade" role="dialog">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-body">
                    <div class="row ajax-form">
                      <form class="create-item-form">
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="name"></label> *
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
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="period"></label> *
                          </div>
                          <div class="col-sm-6">
                            <select class="col-sm-12" name="period">
                              <option value=""></option>
                              <option value="ANCIENT"></option>
                              <option value="ANTIQUITY"></option>
                              <option value="EARLY_MIDDLE_AGES"></option>
                              <option value="HIGH_MIDDLE_AGES"></option>
                              <option value="LATE_MIDDLE_AGES"></option>
                              <option value="RENAISSANCE"></option>
                              <option value="MODERN"></option>
                              <option value="WWI"></option>
                              <option value="WWII"></option>
                              <option value="CONTEMPORARY"></option>
                              <option value="OTHER"></option>
                            </select>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="classification"></label> *
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
                        <button class="btn btn-default submit-button"></button>
                      </form>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <h1 class="item-list-heading"></h1>
        </div>
        <div class="row">
          <div class="col-sm-3"></div>
          <div class="col-sm-8">
            <div class="item-list-block"></div>
          </div>
          <template class="item-list-template">
            <li>
              <div>
                <a class="btn btn-default col-sm-4">
                  <b class="item-name"></b>
                  <p class="item-description"></p>
                </a>
              </div>
            </li>
          </template>
          <div class="col-sm-3"></div>
        </div>
      </div>
    </template>
  </body>
</html>
