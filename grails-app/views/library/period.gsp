<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title></title>
    <script src="/static/js/library.js"></script>
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
  </body>
</html>
