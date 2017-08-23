<script>
    $(function() {
         $('select[name="period"]').change(
             function() {
                var sourcesURL = '${g.createLink(controller: "source", action: "getSourcesByPeriod")}' + '/' + $(this).val();
                var closestForm = $(this).closest("form");
                var sourceSelect = closestForm.find('select[name="source"]');
                sourceSelect.find('option').remove();
                sourceSelect.append(createOption('', '...'));
                closestForm.find('.source-wrapper').css('display', '');
                sendAjaxRequest(sourceSelect, sourcesURL, 'GET', populateSources);
            }
        );
    });

    function populateSources(element, response) {
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
