var img = {
  setFileUploadPlaceholder: function(element) {
    //cutting off "fakepath"
    var fileName = element.value.split(/(\\|\/)/g).pop();
    var placeholder = element.parentNode.querySelector(".placeholder");
    placeholder.innerHTML = "<b>" + fileName + "</b>";
  }
};
