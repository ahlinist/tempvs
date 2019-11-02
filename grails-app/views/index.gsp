<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="/static/css/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script type="module" src="/static/js/application.js"></script>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <link rel="shortcut icon" type="image/x-icon" href="/static/favicon.ico">
  </head>
  <body>
    <header class="row" style="height:35px;">
      <sec:ifLoggedIn>
        <div class="col-sm-4">
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'profile.show.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="profile" action=" ">
              <span class="glyphicon glyphicon-user"></span>
            </g:link>
          </span>
          <span id="profile-dropdown" class="dropdown pull-left">
            <button style="width: 180px;" id="current-profile" class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
              <span id="current-profile-name"></span>
              <span class="caret"></span>
            </button>
            <ul id="profiles" class="dropdown-menu list-group">
              <li id="user-profile">
                <a class="list-group-item disableable" href=""></a>
              </li>
            </ul>
          </span>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'following.list.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="following" action=" ">
              <span class="fa fa-users">
                <span id="new-followings" class="badge badge-notify hidden counter" style="position: absolute;"></span>
              </span>
            </g:link>
          </span>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'item.stash.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="stash" action=" ">
              <span class="glyphicon glyphicon-tent"></span>
            </g:link>
          </span>
          <span class="pull-left" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'message.tooltip')}">
            <g:link class="btn btn-secondary disableable" controller="messaging" action=" ">
              <span class="fa fa-envelope">
                <span id="new-conversations" class="badge badge-notify hidden counter" style="position: absolute;"></span>
              </span>
            </g:link>
          </span>
        </div>
        <div class="col-sm-4">
          <span class="dropdown" style="margin:10px;">
            <input style="width: 300px;" placeholder="${g.message(code: 'profile.search.placeholder')}" type="text" class="profile-search-box" name="query"/>
            <button class="btn btn-default dropdown-toggle profile-search-button" onclick="profileSearcher.search(this, 0, profileSearcher.actions);">
              <span class="glyphicon glyphicon-search"></span>
            </button>
            <div class="dropdown-menu" style="width: 300px;">
              <ul class="profile-search-result">
              </ul>
              <button class="btn btn-secondary col-sm-12 load-more-button" onclick="profileSearcher.search(this, 10, profileSearcher.actions);">
                <i><g:message code="profile.search.loadMore.link"/></i>
              </button>
            </div>
          </span>
        </div>
      </sec:ifLoggedIn>
      <div class="col-sm-4">
        <span class="pull-left user hidden" data-toggle="tooltip" data-placement="bottom" title="${g.message(code: 'settings.tooltip')}">
          <g:link class="btn btn-default disableable" controller="user" action="edit">
            <span class="glyphicon glyphicon-cog"></span>
          </g:link>
        </span>
        <span class="pull-left library" data-toggle="tooltip" data-placement="bottom">
          <a href="/library" class="btn btn-default">
            <span class="glyphicon glyphicon-book"></span>
          </a>
        </span>
        <span class="pull-right logout hidden">
          <a class="btn btn-secondary">
            <span class="fa fa-sign-out"></span>
          </a>
        </span>
        <span class="pull-right login hidden" data-toggle="tooltip" data-placement="bottom">
          <button class="btn btn-default login-popup" data-toggle="modal" data-target="#login-popup">
            <span class="fa fa-sign-in"></span>
          </button>
          <div id="login-popup" class="modal fade" role="dialog">
            <div class="modal-dialog">
              <div class="modal-content">
                <div class="modal-body">
                  <ul class="nav nav-tabs">
                    <li style="padding:0px; margin:0px;" class="col-sm-6 pull-left active">
                      <a data-toggle="tab" href="#login-tab"></a>
                    </li>
                    <li style="padding:0px; margin:0px;" class="col-sm-6 pull-right">
                      <a data-toggle="tab" href="#register-tab"></a>
                    </li>
                  </ul>
                  <div class="tab-content">
                    <div id="login-tab" class="tab-pane fade in active">
                      <form class="ajax-form login">
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="email"></label>
                          </div>
                          <div class="col-sm-6">
                            <input class="col-sm-12 tempvs-form-field" type="email" name="email">
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="password"></label>
                          </div>
                          <div class="col-sm-6">
                            <input class="col-sm-12 tempvs-form-field" type="password" name="password">
                          </div>
                        </div>
                        <button class="btn btn-light submit-button login"></button>
                      </form>
                    </div>
                    <div id="register-tab" class="tab-pane fade">
                      <form class="ajax-form registration">
                        <div class="row">
                          <div class="col-sm-6">
                            <label for="email"></label>
                          </div>
                          <div class="col-sm-6">
                            <input class="col-sm-12 tempvs-form-field" type="email" name="email">
                          </div>
                        </div>
                        <button class="btn btn-light submit-button register"></button>
                      </form>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </span>
      </div>
    </header>
    <hr/>
    <content></content>
    <template id="unauthorized">
      <div class="row">
        <div class="col-sm-4"></div>
        <div class="col-sm-4 message text-center"></div>
        <div class="col-sm-4"></div>
      </div>
    </template>
    <template id="create-user">
      <div class="row">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
          <form class="ajax-form register-user">
            <div class="row">
              <div class="col-sm-6">
                <label for="password"></label>
              </div>
              <div class="col-sm-6">
                <input class="col-sm-12 tempvs-form-field" type="text" name="password">
              </div>
            </div>
            <button class="btn btn-light submit-button"></button>
          </form>
        </div>
        <div class="col-sm-4"></div>
      </div>
    </template>
    <template id="library">
      <div>
        <div class="row">
          <breadcrumb></breadcrumb>
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
          <breadcrumb><breadcrumb>
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
          <breadcrumb></breadcrumb>
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
                      <form action="/api/library/source">
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
            <form class="search-form" autocomplete="off">
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
          <breadcrumb></breadcrumb>
          <span id="delete-source-button" class="pull-right"></span>
        </div>
        <div class="row">
          <div class="col-sm-4">
            <div id="source-form" class="ajax-form"></div>
          </div>
          <div class="col-sm-4">
          </div>
          <div class="col-sm-4">
            <div id="image-container"></div>
          </div>
        </div>
      </div>
    </template>
    <template id="stash">
      <div id="stash-section">
        <div class="row">
          <breadcrumb></breadcrumb>
          <div id="create-group-section" class="pull-right hidden">
            <button id="popup-button" class="btn btn-default pull-right" data-toggle="modal" data-target="#create-source-popup">
              <span class="fa fa-plus"></span>
            </button>
            <div id="create-source-popup" class="modal fade" role="dialog">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-body">
                    <div class="row ajax-form">
                      <form>
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
          <breadcrumb></breadcrumb>
        </div>
        <div class="row">
          <div class="ajax-form group-form col-sm-6"></div>
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
    <template id="item">
      <div id="item-section">
        <div class="row">
          <breadcrumb></breadcrumb>
          <span id="delete-item-button" class="pull-right"></span>
        </div>
        <div class="row">
          <div class="ajax-form item-form col-sm-4"></div>
          <div class="ajax-form linked-sources col-sm-4">
            <h2 class="linked-sources-heading"></h2>
            <ul class="linked-sources-list"></ul>
            <template class="linked-source-item">
              <li class="row linked-source-list-item">
                <a class="btn btn-default col-sm-11 source-link"></a>
                <a class="btn btn-default fa fa-unlink col-sm-1 hidden"></a>
              </li>
            </template>
            <p class="text-center message-container hidden"></p>
            <div class="row find-sources-container hidden" style="margin:10px 0px;">
              <button class="btn btn-default col-sm-6 pull-right" data-toggle="modal" data-target="#find-sources-popup">
                <span class="pull-left find-sources-popup-text"></span>
                <span class="pull-right">
                  <span class="fa fa-plus"></span>
                </span>
              </button>
              <div id="find-sources-popup" class="modal fade" role="dialog">
                <div class="modal-dialog">
                  <div class="modal-content">
                    <div class="modal-body">
                      <div class="row">
                        <form class="search-form" autocomplete="off">
                          <div>
                            <input type="text" name="query" style="width: 525px;">
                            <button class="btn btn-default pull-right"><span class="fa fa-search"></span></button>
                          </div>
                          <div class="find-source-type row" style="margin: 10px 0 10px 0;">
                            <h4 class="well well-sm"></h4>
                            <span class="col-sm-3 table-cell">
                              <label class="type-written"></label>
                              <input class="pull-right" name="type" value="WRITTEN" type="checkbox">
                            </span>
                            <span class="col-sm-3 table-cell">
                              <label class="type-graphic"></label>
                              <input class="pull-right" name="type" value="GRAPHIC" type="checkbox">
                            </span>
                            <span class="col-sm-3 table-cell">
                              <label class="type-archaeological"></label>
                              <input class="pull-right" name="type" value="ARCHAEOLOGICAL" type="checkbox">
                            </span>
                            <span class="col-sm-3 table-cell">
                              <label class="type-other"></label>
                              <input class="pull-right" name="type" value="OTHER" type="checkbox">
                            </span>
                          </div>
                          <input type="hidden" name="period">
                          <input type="hidden" name="classification">
                        </form>
                        <p class="text-center no-results-message hidden"></p>
                        <img class="center-block spinner hidden" src="/static/images/spinner.gif">
                        <table class="table table-hover result-table hidden">
                          <thead>
                            <tr>
                              <th></th>
                              <th class="source-name"></th>
                              <th class="source-description"></th>
                              <th class="source-type"></th>
                            </tr>
                          </thead>
                          <tbody></tbody>
                          <template class="source-template">
                            <tr style="cursor: pointer;">
                              <td class="link-button"><span class="fa fa-link btn btn-default"></span></td>
                              <td class="source-name"></td>
                              <td class="source-description"></td>
                              <td class="source-type"></td>
                            </tr>
                          </template>
                        </table>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="col-sm-4">
            <div id="image-container"></div>
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
    <template id="message">
      <div class="message-container">
        <div class="col-sm-3" style="border-right: 1px solid #AAA; height: calc(100vh - 75px); overflow: auto;">
          <div class="row" style="margin:10px 0px;">
            <button class="btn btn-default col-sm-12" data-toggle="modal" data-target="#create-conversation-popup">
              <span class="pull-left create-conversation-button"></span>
              <span class="pull-right">
                <span class="fa fa-plus"></span>
              </span>
            </button>
            <div id="create-conversation-popup" class="modal fade" role="dialog">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-body">
                    <template id="conversation-popup-template">
                      <div>
                        <span class="dropdown" style="margin:10px 0px;">
                          <input style="width: 524px;" type="text" class="profile-search-box" name="query">
                          <button class="btn btn-default dropdown-toggle profile-search-button">
                            <span class="fa fa-search"></span>
                          </button>
                          <div class="dropdown-menu" style="width: 300px;">
                            <ul class="profile-search-result"></ul>
                            <button class="btn btn-secondary col-sm-12 load-more-button">
                              <i></i>
                            </button>
                          </div>
                        </span>
                        <form class="create-conversation-form">
                          <template class="profile-search-template">
                            <li>
                              <a class="btn btn-default col-sm-12 search-result"></a>
                            </li>
                          </template>
                          <ul class="create-conversation-participants-container" class="row"></ul>
                          <div class="new-conversation-name-container" class="row" style="display: none;">
                            <hr style="margin: 10px 0px;"/>
                            <input class="col-sm-12" type="text" name="name">
                          </div>
                          <hr style="margin: 10px 0px;"/>
                          <textarea name="text" style="width: 100%; height: 100px;"></textarea>
                          <button class="btn btn-default submit-button"></button>
                        </form>
                      </div>
                    </template>
                    <div id="conversation-popup-wrapper"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <hr/>
          <div id="conversations-container">
            <template id="conversation-template">
              <li class="btn btn-default col-sm-12">
                <div class="row">
                  <div class="col-sm-10">
                    <b class="pull-left conversation-name"></b>
                    <br>
                    <i class="pull-left last-message"></i>
                  </div>
                  <div class="col-sm-2" style="padding: 12px;">
                    <span class="badge badge-notify counter pull-right"></span>
                  </div>
                </div>
              </li>
            </template>
            <ul style="margin:10px 0px;" id="conversations-list" class="row"></ul>
            <div id="load-more-conversations" class="row hidden">
              <button class="btn btn-default center-block"></button>
            </div>
            <div class="row">
              <img class="spinner load-more hidden center-block" src="/static/images/spinner.gif">
            </div>
          </div>
        </div>
        <div class="col-sm-9">
          <div class="row hidden" id="conversation-details">
            <div id="messages-container" class="col-sm-8" style="position: relative; height: calc(100vh - 75px); overflow: auto;">
              <div class="row">
                <img class="load-more-messages-spinner hidden center-block" src="/static/images/spinner.gif">
              </div>
              <template id="message-template">
                <li>
                  <div style="margin: 5px; padding: 5px;">
                    <a class="message-author"><b></b></a>:
                    <span class="message-text"></span>
                    <a class="message-subject"><b></b></a>
                    <span class="pull-right message-created-date"></span>
                  </div>
                </li>
              </template>
              <div id="load-more-messages" class="row hidden" style="margin: 10px 0 11px 0;">
                <button class="btn btn-default center-block" id="load-more-messages-button"></button>
              </div>
              <ul id="messages-list"></ul>
              <form id="message-form">
                <input type="text" style="width: calc(100% - 45px);" name="message" autocomplete="off">
                <button class="btn btn-default">
                  <span class="fa fa-paper-plane" aria-hidden="true"></span>
                </button>
              </form>
            </div>
            <div class="col-sm-4">
              <div class="row conversation-name"></div>
              <div class="row participants-container">
                <b></b>
                <template class="participant-template">
                  <li class="row">
                    <a class="btn btn-default col-sm-10 active-conversation-participant"></a>
                    <span class="hidden remove-participant-button">
                      <button class="btn btn-default fa fa-trash" data-toggle="modal" data-target="#removeParticipantModal-"></button>
                      <div class="modal fade" role="dialog">
                        <div class="modal-dialog modal-sm">
                          <div class="modal-content">
                            <div class="modal-body remove-participant-confirmation">
                              <span class="confirmation-text"></span>
                              <br/>
                              <form class="participant-deletion-form">
                                <button class="btn btn-default submit-button"></button>
                                <span class="btn btn-default" data-dismiss="modal">
                                </span>
                              </form>
                            </div>
                          </div>
                        </div>
                      </div>
                    </span>
                  </li>
                </template>
                <ul id="participants-list"></ul>
              </div>
              <button class="btn btn-default fa fa-plus" data-toggle="modal" data-target="#add-participant-profile-search"></button>
              <div id="add-participant-profile-search" class="modal fade" role="dialog">
                <div class="modal-dialog modal-sm">
                  <div class="modal-content">
                    <div class="modal-body">
                      <template id="add-participant-template">
                        <div>
                          <span class="dropdown" style="margin:10px 0px;">
                            <input style="width: 524px;" type="text" class="profile-search-box" name="query"/>
                            <button class="btn btn-default dropdown-toggle profile-search-button">
                              <span class="fa fa-search"></span>
                            </button>
                            <div class="dropdown-menu" style="width: 300px;">
                              <template class="profile-search-template">
                                <li class="row">
                                  <a class="btn btn-default col-sm-12 search-result"></a>
                                </li>
                              </template>
                              <ul class="profile-search-result"></ul>
                              <button class="btn btn-secondary col-sm-12 load-more-button">
                                <i></i>
                              </button>
                            </div>
                          </span>
                          <div id="add-participant-to-conversation-container">
                            <form class="add-participant-to-conversation-form">
                              <ul></ul>
                              <div style="height: 35px;">
                                <button type="submit" class="btn btn-default hidden submit-button add-participant-button"></button>
                              </div>
                            </form>
                          </div>
                        </div>
                      </template>
                      <div id="add-participant-wrapper"></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <template id="profile-search-result-template">
          <li>
            <a class="btn btn-default search-result-link" style="width: 524px;"></a>
            <span class="btn btn-default remove-participant" style="width: 39px;">
              <span class="fa fa-remove" style="color: red;"></span>
            </span>
            <input type="hidden" name="participants">
          </li>
        </template>
      </div>
    </template>
    <template id="create-user-profile">
      <div class="row">
        <div class="col-sm-4"></div>
        <div class="col-sm-4 ajax-form">
          <h1 class="create-user-profile-heading"></h1>
          <form class="create-user-profile-form" action="/api/profile/profile">
            <div class="row">
              <div class="col-sm-6">
                <label for="firstName"></label>
              </div>
              <div class="col-sm-6">
                <input class="col-sm-12 tempvs-form-field" type="text" name="firstName">
              </div>
            </div>
            <div class="row">
              <div class="col-sm-6">
                <label for="lastName"></label>
              </div>
              <div class="col-sm-6">
                <input class="col-sm-12 tempvs-form-field" type="text" name="lastName">
              </div>
            </div>
            <div class="row">
              <div class="col-sm-6">
                <label for="nickName"></label>
              </div>
              <div class="col-sm-6">
                <input class="col-sm-12 tempvs-form-field" type="text" name="nickName">
              </div>
            </div>
            <button class="btn btn-default submit-button"></button>
          </form>
        </div>
        <div class="col-sm-4"></div>
      </div>
    </template>
    <template id="profile">
      <div class="row">
        <div class="col-sm-4"></div>
        <div class="col-sm-4">
          <div class="ajax-form profile-form"></div>
        </div>
        <div class="col-sm-4"></div>
      </div>
    </template>
    <template id="image-section">
      <div>
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
            <form class="ajax-form image-upload-form">
              <div class="row">
                <div class="col-sm-5">
                  <label for="image"></label>
                </div>
                <div class="col-sm-7">
                  <label class="btn btn-default col-sm-12">
                    <span id="select-file-button" class="placeholder"><i></i></span>
                    <input type="file" name="image" hidden>
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
    </template>
    <template id="modal-button">
      <span>
        <button class="btn btn-default" data-toggle="modal"></button>
        <div class="modal fade" role="dialog">
          <div class="modal-dialog modal-sm">
            <div class="modal-content">
              <div class="modal-body">
                <span class="message"></span>
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
    </template>
  </body>
</html>
