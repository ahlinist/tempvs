var modalCarousel = {
  init: function(slideMapping) {
    var carousel = $('.carousel');
    modalCarousel.currentSlide = 0;
    modalCarousel.slideMapping = slideMapping;
    carousel.carousel(0);
    carousel.on('slide.bs.carousel', function(event) {
        modalCarousel.currentSlide = $(event.relatedTarget).index();
    });
  },
  deleteImage: function(form, actions) {
    $('.carousel').off('slide.bs.carousel');
    ajaxHandler.hideModals();
    ajaxHandler.blockUI();
    var objectId = modalCarousel.slideMapping[modalCarousel.currentSlide];
    var url = form.action + '/' + objectId;
    ajaxHandler.fetch(form, url, {method: 'DELETE'}, actions);
  }
};
