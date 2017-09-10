<script>
    $(function() {
         $('select[name="period"]').change(populateSources);
         $('#period-input > select[name="fieldValue"]').change(populateSmartSources);
    });

    function populateSources() {
        var sourcesURL = '${g.createLink(controller: "source", action: "getSourcesByPeriod")}' + '/' + $(this).val();
        var closestForm = $(this).closest("form");
        var sourceSelect = closestForm.find('select[name="source"]');

        sourceSelect.find('option').remove();
        sourceSelect.append(createOption('', '-'));
        closestForm.find('.source-wrapper').css('display', '');
        sendAjaxRequest(sourceSelect, sourcesURL, 'GET', successAction);
    }

    function populateSmartSources() {
        var sourcesURL = '${g.createLink(controller: "source", action: "getSourcesByPeriod")}' + '/' + $(this).val();
        var sourceSelectInput = $('#source-input > select[name="fieldValue"]');
        var sourceSelectText = $('#source-text');
        sourceSelectText.html('-');
        sourceSelectInput.find('option').remove();
        sourceSelectInput.append(createOption('', '-'));
        sendAjaxRequest(sourceSelectInput, sourcesURL, 'GET', successAction);
    }

    function successAction(element, response) {
        if (Object.keys(response).length) {
            $.each(response, function(i, value) {
                element.append(createOption(value.id, value.name));
            });
        }
    }

    function createOption(id, name) {
        var option = document.createElement('option');
        option.innerHTML = name;
        $(option).prop('value', id);
        return option;
    }
</script>
