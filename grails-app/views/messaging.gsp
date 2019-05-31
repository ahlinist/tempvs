<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <script type="module" src="/static/js/messaging.js"></script>
  </head>
  <body>
    <template id="profile-search-result-template">
      <li>
        <a class="btn btn-default search-result-link" style="width: 524px;"></a>
        <span class="btn btn-default remove-participant" style="width: 39px;">
          <span class="fa fa-remove" style="color: red;"></span>
        </span>
        <input type="hidden" name="participants">
      </li>
    </template>
    <div class="container">
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
            <div class="row hidden conversation-name">
              <b class="conversation-name-label"></b>
              <form class="conversation-name-form" style="display: inline;">
                <span class="hovering text-wrapper">
                  <span class="text-holder" style="line-height: 40px; padding-left: 15px;"></span>
                  <span class="fa fa-pencil smart-form-activator hidden"></span>
                </span>
                <span class="hidden input-wrapper">
                  <input style="margin: 4px 0px 3px 0px;" type="text" name="name" value="" autocomplete="off">
                </span><img style="width: 15px; height: 15px;" class="spinner hidden" src="/static/images/spinner-sm.gif">
              </form>
            </div>
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
    </div>
  </body>
</html>