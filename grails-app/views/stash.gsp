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
  </body>
</html>
