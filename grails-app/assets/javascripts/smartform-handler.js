var smartformHandler = {
    activateInput: function(initiator, fieldName, fieldType) {
        var inputWrapper = document.querySelector('#' + fieldName + '-input');
        var textField = document.querySelector('#' + fieldName + '-text');
        var pencil = createGlyph('pencil', '#AAA');

        var clickOutEventListener = function(event) {
            if(event.target !== inputWrapper.querySelector('input') && event.target !== initiator) {
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
                textField.classList.remove('hidden');
                var textFieldValue = textField.textContent.trim()
                inputWrapper.querySelector('input').value = textFieldValue ? textFieldValue : '- ';
                window.removeEventListener("click", clickOutEventListener);
                window.removeEventListener("keydown", keyPressEventListener);
            }
        }

        function submitSmartForm() {
            if(!!(inputWrapper.offsetWidth || inputWrapper.offsetHeight || inputWrapper.getClientRects().length)) {
                submitAjaxForm(document.forms[fieldName + '-form'], null, actions);
                window.removeEventListener("click", clickOutEventListener);
                window.removeEventListener("keydown", keyPressEventListener);
                inputWrapper.classList.add('hidden');
                textField.classList.remove('hidden');

                if (fieldType === 'select') {
                    var select = inputWrapper.querySelector('select');
                    textField.innerHTML = select.options[select.selectedIndex].innerHTML + ' ';
                } else {
                    var fieldValue = inputWrapper.querySelector('input').value
                    textField.innerHTML = fieldValue ? fieldValue : '- ';
                }
            }
        }

        var actions = {
            success: successAction,
            formMessage: formMessageAction,
            validationResponse: validationResponseAction,
        };

        function successAction() {
            var okGlyph = createGlyph('ok', 'green');
            textField.appendChild(okGlyph);

            setTimeout(function() {
                okGlyph.remove();
                textField.appendChild(pencil);
            }, 1000);
        }

        function formMessageAction(element, response) {
            textField.classList.add('popped-over', response.success ? 'bg-success' : 'bg-danger');
            textField.setAttribute('data-placement','right');
            textField.setAttribute('data-content', response.message);
            textField.setAttribute('data-html', true);
            $('.popped-over').popover('show');
            textField.appendChild(pencil);
        }

        function validationResponseAction(element, response) {
            textField.classList.add('popped-over', 'bg-danger');
            textField.setAttribute('data-placement','right');
            textField.setAttribute('data-content', response.messages[0].message);
            textField.setAttribute('data-html', true);
            $('.popped-over').popover('show');
            textField.appendChild(pencil);
        }

        function createGlyph(type, color) {
            var span = document.createElement('span');
            span.classList.add('glyphicon', 'glyphicon-' + type);
            span.style.color = color;
            return span;
        }

        textField.classList.add('hidden');
        inputWrapper.classList.remove('hidden');

        pencil.addEventListener('click', function() {
            smartformHandler.activateInput(pencil, fieldName, fieldType);
        });

        window.addEventListener("click", clickOutEventListener);
        window.addEventListener("keydown", keyPressEventListener);
    },
};
