var imageUploader = {
        min: {},
        max: {},
        count: {},
        init: function(fieldName, min, max) {
            imageUploader.min[fieldName] = min || 0;
            imageUploader.max[fieldName] = max || Infinity;
            imageUploader.count[fieldName] = 0;

            for (var i = 0; i < min; i++) {
                imageUploader.createFields(fieldName);
            }
        },
        createFields: function(fieldName) {
            if (imageUploader.count[fieldName] < imageUploader.max[fieldName]) {
                var container = document.querySelector('.' + fieldName + '-container');
                container.appendChild(imageUploader.cloneField(container, "image", fieldName));
                container.appendChild(imageUploader.cloneField(container, "imageInfo", fieldName));
                container.appendChild(document.createElement('hr'));
                imageUploader.count[fieldName]++;
            }

            if (imageUploader.count[fieldName] === imageUploader.max[fieldName]) {
                var addButton = document.querySelector('.' + fieldName + '-image-uploader-add-button');
                addButton.parentNode.removeChild(addButton);
            }
        },
        cloneField: function(container, fieldClass, fieldName) {
            var nameValue = fieldName + "[" + imageUploader.count[fieldName] + "]." + fieldClass;
            var section = container.getElementsByClassName(fieldClass)[0].cloneNode(true);
            var inputField = section.querySelector('input');
            inputField.id = nameValue;
            inputField.name = nameValue;
            section.classList.remove('hidden');
            return section;
        },
};
