var smartformHandler = {
    activateInput: function(initiator, fieldName, fieldType) {
        var form = initiator.closest('form');
        var inputWrapper = form.querySelector('#' + fieldName + '-input');
        var inputField = inputWrapper.querySelector('input');
        var select = inputWrapper.querySelector('select');
        var textWrapper = form.querySelector('#' + fieldName + '-text');
        var textValue = textWrapper.textContent.trim();
        var pencil = createGlyph('pencil', '#AAA');

        var clickOutEventListener = function(event) {
            if((event.target !== inputField) && (event.target !== initiator)) {
                submitSmartForm();
            }
        }

        var keyPressEventListener = function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                submitSmartForm();
            }

            if (event.key === 'Escape') {
                inputWrapper.classList.add('hidden');
                textWrapper.classList.remove('hidden');
                inputField.value = textValue;
                window.removeEventListener("click", clickOutEventListener);
                window.removeEventListener("keydown", keyPressEventListener);
            }
        }

        function submitSmartForm() {
            if(!!(inputWrapper.offsetWidth || inputWrapper.offsetHeight || inputWrapper.getClientRects().length)) {
                if (validateForm()) {
                    ajaxHandler.processAjaxRequest(form, form.action, new FormData(form), 'POST', null, actions);

                    if (fieldType === 'select') {
                        textWrapper.innerHTML = select.options[select.selectedIndex].innerHTML + ' ';
                    } else {
                        var fieldValue = inputField.value;
                        textWrapper.innerHTML = fieldValue.trim() ? fieldValue + ' ' : '- ';
                    }
                } else {
                    if (fieldType === 'select') {
                        select.options[select.selectedIndex].innerHTML.textContent = textValue;
                    } else {
                        inputField.value = textValue;
                    }

                    textWrapper.querySelector('.glyphicon-pencil').remove();
                    appendResultGlyph('remove', 'red');
                }

                window.removeEventListener("click", clickOutEventListener);
                window.removeEventListener("keydown", keyPressEventListener);
                inputWrapper.classList.add('hidden');
                textWrapper.classList.remove('hidden');
            }
        }

        function validateForm() {
            var fieldValue;
            var isMandatory = inputWrapper.classList.contains('mandatory');

            if (isMandatory) {
                if (fieldType === 'select') {
                    fieldValue = select.options[select.selectedIndex].innerHTML.textContent.trim();
                } else {
                    fieldValue = inputField.value.trim();
                }

                return fieldValue.length > 0;
            }

            return true;
        }

        var actions = {
            success: successAction,
            formMessage: formMessageAction,
            validationResponse: validationResponseAction,
        };

        function successAction() {
            appendResultGlyph('ok', 'green');
        }

        function formMessageAction(element, response) {
            textWrapper.classList.add('popped-over', response.success ? 'bg-success' : 'bg-danger');
            textWrapper.setAttribute('data-placement','right');
            textWrapper.setAttribute('data-content', response.message);
            textWrapper.setAttribute('data-html', true);
            $('.popped-over').popover('show');
            textWrapper.appendChild(pencil);
        }

        function validationResponseAction(element, response) {
            textWrapper.classList.add('popped-over', 'bg-danger');
            textWrapper.setAttribute('data-placement','right');
            textWrapper.setAttribute('data-content', response.messages[0].message);
            textWrapper.setAttribute('data-html', true);
            $('.popped-over').popover('show');
            textWrapper.appendChild(pencil);
        }

        function createGlyph(type, color) {
            var span = document.createElement('span');
            span.classList.add('glyphicon', 'glyphicon-' + type);
            span.style.color = color;
            return span;
        }

        function appendResultGlyph(type, color) {
            var glyph = createGlyph(type, color);
            textWrapper.appendChild(glyph);

            setTimeout(function() {
                glyph.remove();
                textWrapper.appendChild(pencil);
            }, 1000);
        }

        textWrapper.classList.add('hidden');
        inputWrapper.classList.remove('hidden');

        pencil.addEventListener('click', function() {
            smartformHandler.activateInput(pencil, fieldName, fieldType);
        });

        window.addEventListener("click", clickOutEventListener);
        window.addEventListener("keydown", keyPressEventListener);
    },
};
