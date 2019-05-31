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
            <ul id="group-list" class="col-sm-8">
              <g:each var="itemGroup" in="${itemGroups}">
                <g:set var="itemGroupId" value="${itemGroup.id}"/>
                <g:set var="itemGroupName" value="${itemGroup.name}"/>
                <li id="itemGroup-${itemGroupId}">
                  <div class="row">
                    <g:link class="btn btn-default col-sm-3" controller="item" action="group" id="${itemGroupId}" data-toggle="tooltip" data-placement="bottom" title="${itemGroup.description}">
                        ${itemGroupName}
                    </g:link>
                  </div>
                </li>
              </g:each>
            </ul>
            <div class="col-sm-2"></div>
          </div>
          <template id="group-list-item-template">
            <li>
              <div>
                <a class="btn btn-default col-sm-4">
                  <b class="group-name"></b>
                  <p class="group-description"></p>
                </a>
              </div>
            </li>
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
        </div>
        <div>
          <h1 id="item-list-heading"></h1>
        </div>
      </div>
    </template>
  </body>
</html>