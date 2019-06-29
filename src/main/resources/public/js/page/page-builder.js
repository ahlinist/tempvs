import {formValidator} from '../validation/form-validator.js';

export const pageBuilder = {
  initPage: function(selector, url, title) {
    const content = document.querySelector('content');
    content.innerHTML = '';
    const template = document.querySelector(selector);
    const page = template.content.querySelector('div');
    const pageNode = document.importNode(page, true);
    content.appendChild(pageNode);
    window.history.pushState('', '', url);
    document.querySelector('title').innerHTML = title;
  },
  breadcrumb: function(content) {
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
  },
  smartForm: function(container, label, value, name, formAction, isEditable) {
    const div = document.createElement('div');
    div.classList.add('row');

    buildLabelWrapper();
    buildValueWrapper();

    container.appendChild(div);

    function buildLabelWrapper() {
      const labelWrapper = document.createElement('span');
      labelWrapper.classList.add('col-sm-5');
      labelWrapper.style.height = '32px';
      const b = document.createElement('b');
      b.innerHTML = label;
      labelWrapper.appendChild(b);
      div.appendChild(labelWrapper);
    }

    function buildValueWrapper() {
      const valueWrapper = document.createElement('span');
      valueWrapper.classList.add('col-sm-7');
      valueWrapper.style.height = '32px';

      if (!isEditable) {
        const textHolder = document.createElement('span');
        textHolder.style.paddingLeft = '15px';
        textHolder.innerHTML = value;
        valueWrapper.appendChild(textHolder);
        div.appendChild(valueWrapper);
        return;
      }

      const form = document.createElement('form');
      form.classList.add('smart-form');
      form.action = formAction;
      const textWrapper = document.createElement('span');
      textWrapper.classList.add('hovering');
      const textHolder = document.createElement('span');
      textHolder.style.lineHeight = '32px';
      textHolder.style.paddingLeft = '15px';
      textHolder.innerHTML = value;
      textHolder.classList.add('text-holder');
      textHolder.onclick = function() {
        activateSmartForm(this, form);
      };
      const formActivator = document.createElement('span');
      formActivator.classList.add('fa', 'fa-pencil', 'hidden');
      formActivator.classList.remove('hidden');

      const inputWrapper = document.createElement('span');
      inputWrapper.classList.add('hidden');
      const input = document.createElement('input');
      input.style.margin = '4px 0px 3px 0px;';
      input.type = 'text';
      input.name = name;
      input.autocomplete = 'off';
      input.value = value;
      const spinner = new Image();
      spinner.setAttribute('style', 'width: 15px; height: 15px;');
      spinner.src = '/static/images/spinner-sm.gif';
      spinner.classList.add('hidden');
      inputWrapper.appendChild(input);
      form.appendChild(inputWrapper);
      textWrapper.appendChild(textHolder);
      textWrapper.appendChild(formActivator);
      form.appendChild(textWrapper);
      form.appendChild(spinner);
      valueWrapper.appendChild(form);
      div.appendChild(valueWrapper);

      function hideSpinner() {
        const elementsToHide = document.querySelectorAll('.hide-me');
        elementsToHide.forEach(function(element) {
          element.classList.add('hidden');
          element.classList.remove('hide-me');
        });
      }

      function activateSmartForm(editButton, form) {
        const originalValue = textHolder.innerHTML;

        textWrapper.classList.add('hidden');
        inputWrapper.classList.remove('hidden');

        const clickOutEventListener = function(event) {
          if((event.target !== input) && (event.target !== editButton)) {
            submitSmartForm();
          }
        }

        const keyPressEventListener = function(event) {
          if (event.key === 'Enter') {
            event.preventDefault();
            submitSmartForm();
          }

          if (event.key === 'Escape') {
            cancelEdit();
          }
        }

        function cancelEdit() {
          inputWrapper.classList.add('hidden');
          textWrapper.classList.remove('hidden');
          textHolder.innerHTML = originalValue;
          input.value = originalValue;
          window.removeEventListener("click", clickOutEventListener);
          window.removeEventListener("keydown", keyPressEventListener);
        }

        function submitSmartForm() {
          if(!!(inputWrapper.offsetWidth || inputWrapper.offsetHeight || inputWrapper.getClientRects().length)) {
            const value = input.value;
            textHolder.innerHTML = value;
            inputWrapper.classList.add('hidden');
            textWrapper.classList.remove('hidden');
            window.removeEventListener("click", clickOutEventListener);
            window.removeEventListener("keydown", keyPressEventListener);

            let object = {};

            new FormData(form).forEach(function(value, key){
              object[key] = value;
            });

            const payload = {
              method: 'PATCH',
              headers:{
                'Content-Type': 'application/json'
              },
              body: JSON.stringify(object)
            }

            spinner.classList.remove('hidden');
            spinner.classList.add('hide-me');

            const actions = {
              200: function() {
                hideSpinner();
              },
              400: function(response, form) {
                hideSpinner();
                cancelEdit();
                formValidator.handleBadRequest(response, form);
              }
            }

            ajaxHandler.fetch(form, form.action, payload, actions);
          }
        }

        window.addEventListener("click", clickOutEventListener);
        window.addEventListener("keydown", keyPressEventListener);
      }
    }
  },
  imageSection: function(imageContainer, uploadAction, images, messageSource, onSubmitFunction) {
    imageContainer.innerHTML = '';
    const template = document.querySelector('template#image-section');
    const imageSection = template.content.querySelector('div');
    const imageSectionNode = document.importNode(imageSection, true);
    imageContainer.appendChild(imageSectionNode);

    const imageUploadForm = document.querySelector('form.image-upload-form');
    imageUploadForm.action = uploadAction;
    imageUploadForm.onsubmit = onSubmitFunction;
    imageUploadForm.querySelector('label[for=image]').innerHTML = messageSource.uploadImage.imageLabel;
    imageUploadForm.querySelector('label[for=imageInfo]').innerHTML = messageSource.uploadImage.imageInfoLabel;
    imageUploadForm.querySelector('span#select-file-button i').innerHTML = messageSource.uploadImage.selectFileButton;
    imageUploadForm.querySelector('button.submit-button').innerHTML = messageSource.uploadImage.submitButton;

    const carouselInner = imageContainer.querySelector('div.carousel-inner');
    const modalActivateButton = imageContainer.querySelector('div#modal-activate-button');
    const carouselIndicatorList = imageContainer.querySelector('ol.carousel-indicators');
    const imageIndicatorTemplate = imageContainer.querySelector('template#image-indicator');
    const imageIndicatorItem = imageIndicatorTemplate.content.querySelector('li');
    const carouselInnerTemplate = imageContainer.querySelector('template#carousel-inner');
    const carouselInnerItem = carouselInnerTemplate.content.querySelector('div');

    carouselInner.innerHTML = '';
    carouselIndicatorList.innerHTML = '';

    const firstImageHolder = modalActivateButton.querySelector('div#first-image-holder');

    if (images.length) {
      imageContainer.querySelector('div#image-carousel').classList.remove('hidden');
      imageContainer.querySelector('img#default-image').classList.add('hidden');
      modalActivateButton.querySelector('.badge-notify').innerHTML = images.length;

      images.forEach(function(image, index) {
        var indicatorNode = document.importNode(imageIndicatorItem, true);
        var carouselInnerNode = document.importNode(carouselInnerItem, true);

        indicatorNode.setAttribute('data-slide-to', index);
        carouselInnerNode.querySelector('p.image-info').innerHTML = image.imageInfo;
        var htmlImage = new Image();
        htmlImage.setAttribute("style", "height: 90vh; max-width: 90vw; width: auto; margin-left: auto; margin-right: auto;");
        htmlImage.src = "/api/image/image/" + image.objectId;

        if (index === 0) {
          indicatorNode.classList.add('active');
          carouselInnerNode.classList.add('active');

          const htmlFirstImage = new Image();
          htmlFirstImage.setAttribute("style", "width: 30vw;");
          htmlFirstImage.src = "/api/image/image/" + image.objectId;
          firstImageHolder.innerHTML = '';
          firstImageHolder.appendChild(htmlFirstImage);
        }

        carouselInnerNode.insertBefore(htmlImage, carouselInnerNode.firstChild);
        carouselIndicatorList.appendChild(indicatorNode);
        carouselInner.appendChild(carouselInnerNode);
      });

      const slideMapping = {};

      images.forEach(function(entry, index) {
        slideMapping[index] = entry.objectId;
      });

      modalCarousel.init(slideMapping);
    }
  }
};
