export const breadcrumb = {
  build: function(content) {
    const breadcrumb = document.querySelector('content breadcrumb');

    const breadcrumbElements = content.map(renderBreadcrumb)

    for (let i = 0; i < breadcrumbElements.length; i++) {
      breadcrumb.appendChild(breadcrumbElements[i]);

      if (i != (breadcrumbElements.length -1)) {
        breadcrumb.appendChild(renderSeparator());
      }
    }

    function renderBreadcrumb(entry) {
      const link = document.createElement('a');
      link.href = entry.url;
      link.innerHTML = entry.text;
      link.style.textDecoration = "underline";
      return link;
    }

    function renderSeparator() {
      const span = document.createElement('span');
      span.innerHTML = '&nbsp;>&nbsp;';
      return span;
    }
  }
};
