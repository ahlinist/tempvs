<g:set var="src" value="${g.createLink(controller: 'image', action: 'get', id: image?.objectId,
    params: [collection: image?.collection])}"/>
<img class="center-block" style="${styles}" src="${src}" alt="${image?.imageInfo}"/>
