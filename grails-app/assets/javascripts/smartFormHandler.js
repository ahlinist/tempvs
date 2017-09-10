function activateInput(fieldName) {
    document.getElementById(fieldName + '-text').classList.add('hidden');
    document.getElementById(fieldName + '-input').classList.remove('hidden');
}

function waitForClickOut(fieldName, fieldType) {
    var textSelector = '#' + fieldName + '-text';
    var inputSelector = '#' + fieldName + '-input';
    var textField = document.querySelector(textSelector);
    var inputField = document.querySelector(inputSelector);

    function successFieldUpdate(element, response) {
        if (!response.success) {
            var field = document.querySelector(textSelector);
            field.classList.add('popped-over', 'bg-danger');
            field.setAttribute('data-placement','right');
            field.setAttribute('data-content', response[0].message);   
            field.setAttribute('data-html', true);   
            $('.popped-over').popover('show');
        } else {
            var okGlyph = createGlyph('ok', 'green');
            textField.appendChild(okGlyph);
            setTimeout(function() {okGlyph.remove();} , 3000);
        }

        textField.appendChild(createGlyph('pencil', '#AAA'));
    }

    function removeListener() {
        window.removeEventListener('click', eventListener);
    }

    var eventListener =  function(event) {
        if(!event.target.closest(inputSelector)) {
            if(!!(inputField.offsetWidth || inputField.offsetHeight || inputField.getClientRects().length)) {
                submitAjaxForm(document.forms[fieldName + '-form'], successFieldUpdate);
                removeListener();
                inputField.classList.add('hidden');
                textField.classList.remove('hidden');

                if (fieldType === 'select') {
                    var select = document.querySelector(inputSelector + ' > select');
                    textField.innerHTML = select.options[select.selectedIndex].innerHTML + ' ';
                } else {
                    textField.innerHTML = document.querySelector(inputSelector + ' > input').value + ' ';
                }
            }
        } 
    }
              
    window.addEventListener("click", eventListener);
}

function createGlyph(type, color) {
    var span = document.createElement('span');
    span.classList.add('glyphicon', 'glyphicon-' + type);
    span.style.color = color;
    return span;
}