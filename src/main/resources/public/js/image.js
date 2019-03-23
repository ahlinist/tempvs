var img = {
  setFileUploadPlaceholder: function(element) {
    var fileName = element.value.split(/(\\|\/)/g).pop(); //cutting off "fakepath"
    var placeholder = element.parentNode.querySelector(".placeholder");
    placeholder.innerHTML = "<b>" + fileName + "</b>";
  }
};
