var imageUploader = {
        min: {},
        max: {},
        count: {},
        init: function(fieldName, min, max) {
            imageUploader.destroy(fieldName);
            imageUploader.min[fieldName] = parseInt(min) || 0;
            imageUploader.max[fieldName] = parseInt(max) === 0 ? 0 : (parseInt(max) || Infinity);
            imageUploader.count[fieldName] = 0;

            for (var i = 0; i < min; i++) {
                imageUploader.createFields(fieldName);
            }
        },
        createFields: function(fieldName) {
            if (imageUploader.count[fieldName] < imageUploader.max[fieldName]) {
                var container = document.querySelector('.' + fieldName + '-mock-container');
                var facet = document.createElement('div');
                facet.classList.add('image-uploader-facet');
                facet.appendChild(imageUploader.cloneField(container, "image", fieldName));
                facet.appendChild(imageUploader.cloneField(container, "imageInfo", fieldName));
                facet.appendChild(document.createElement('hr'));
                container.appendChild(facet);
                imageUploader.count[fieldName]++;
            }

            if (imageUploader.count[fieldName] >= imageUploader.max[fieldName]) {
                var addButton = document.querySelector('.' + fieldName + '-image-uploader-add-button');
                addButton.classList.add('hidden');
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
        destroy: function(fieldName) {
            var container = document.querySelector('.' + fieldName + '-mock-container');
            var facets = container.getElementsByClassName('image-uploader-facet');
            var position = facets.length;

            while (position--) {
                container.removeChild(facets[position]);
            }
        },
};
