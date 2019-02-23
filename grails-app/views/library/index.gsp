<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title></title>
    <script>
      window.onload = function() {
        library.welcomePage();
        library.renderWelcomeStatic();
      };
    </script>
  </head>
  <body>
    <div class="row">
      <div class="col-sm-8">
        <g:render template="/library/templates/navBar"/>
      </div>
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
              <span class="role-button btn btn-default"></span>
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
                    <p class="pull-left period-heading"></p>
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
  </body>
</html>
