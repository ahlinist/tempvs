var smartformHandler = {
    activateInput: function(fieldName) {
        document.getElementById(fieldName + '-text').classList.add('hidden');
        document.getElementById(fieldName + '-input').classList.remove('hidden');
    },
    createGlyph: function(type, color) {
        var span = document.createElement('span');
        span.classList.add('glyphicon', 'glyphicon-' + type);
        span.style.color = color;
        return span;
    },
    waitForClickOut: function(fieldName, fieldType) {
        var inputSelector = '#' + fieldName + '-input';
        var inputField = document.querySelector(inputSelector);
        var textField = document.querySelector('#' + fieldName + '-text');
        var pencil = smartformHandler.createGlyph('pencil', '#AAA');

        var submitFormEventListener = function(event) {
            if(!event.target.closest(inputSelector)) {
                if(!!(inputField.offsetWidth || inputField.offsetHeight || inputField.getClientRects().length)) {
                    submitAjaxForm(document.forms[fieldName + '-form'], null, actions);
                    window.removeEventListener("click", submitFormEventListener);
                    inputField.classList.add('hidden');
                    textField.classList.remove('hidden');

                    if (fieldType === 'select') {
                        var select = document.querySelector(inputSelector + ' > select');
                        textField.innerHTML = select.options[select.selectedIndex].innerHTML + ' ';
                    } else {
                        var fieldValue = document.querySelector(inputSelector + ' > input').value
                        textField.innerHTML = fieldValue ? fieldValue : '- ';
                    }
                }
            }
        }

        var actions = {
            success: successAction,
            formMessage: formMessageAction,
            validationResponse: validationResponseAction,
        };

        function successAction() {
            var okGlyph = smartformHandler.createGlyph('ok', 'green');
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

        pencil.addEventListener('click', function() {
            smartformHandler.activateInput(fieldName);
        });

        window.addEventListener("click", submitFormEventListener);
    }
};
