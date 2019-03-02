<g:set var="periodKey" value="${period.id}"/>

<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title></title>
    <script src="/static/js/library.js"></script>
    <script>
      window.onload = function() {
        library.renderPeriodPage('${periodKey}');
      };
    </script>
  </head>
  <body>
    <g:if test="${period}">
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
        <h1 id="source-list"></h1>
        <g:each var="sourceType" in="${sourceTypes}">
          <g:set var="groupedSources" value="${sources.findAll {it.sourceType == sourceType}}"/>
          <div class="col-sm-3">
            <h2><g:message code="source.sourceType.${sourceType.id}.value"/></h2>
            <div>
              <g:if test="${groupedSources}">
                <ul>
                  <g:each in="${groupedSources}" var="source">
                    <g:set var="sourceId" value="${source.id}"/>
                    <g:set var="sourceName" value="${source.name}"/>
                    <li class="row" id="source-${sourceId}">
                      <g:link controller="source" action="show" id="${sourceId}" class="btn btn-default col-sm-12">
                        ${sourceName}
                      </g:link>
                    </li>
                  </g:each>
                </ul>
              </g:if>
              <g:else>
                <div class="text-center">
                  <i><g:message code="source.sourceType.list.empty"/></i>
                </div>
              </g:else>
            </div>
          </div>
        </g:each>
      </div>
      <sec:ifAnyGranted roles="ROLE_CONTRIBUTOR">
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
                        <button class="btn btn-default submit-button">
                          <g:message code="message.send.message.button"/>
                        </button>
                      </form>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </sec:ifAnyGranted>
    </g:if>
    <g:else>
      <g:message code="period.notFound.message"/>
    </g:else>
  </body>
</html>
