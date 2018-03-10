var imageUploader = {
        count: {},
        createFields: function createFields(fieldName) {
            var container = document.querySelector('.' + fieldName + '-container');
            container.appendChild(imageUploader.cloneField(container, "image", fieldName));
            container.appendChild(imageUploader.cloneField(container, "imageInfo", fieldName));
            container.appendChild(document.createElement('hr'));
            imageUploader.count[fieldName] += 1;
        },
        cloneField: function cloneField(container, fieldClass, fieldName) {
            var nameValue = fieldName + "[" + imageUploader.count[fieldName] + "]." + fieldClass;
            var section = container.getElementsByClassName(fieldClass)[0].cloneNode(true);
            var inputField = section.querySelector('input');
            inputField.id = nameValue;
            inputField.name = nameValue;
            section.classList.remove('hidden');
            return section;
        },
};
