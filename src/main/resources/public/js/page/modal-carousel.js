export const modalCarousel = {
  init: function(slideMapping) {
    const carousel = $('.carousel');
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
    const objectId = modalCarousel.slideMapping[modalCarousel.currentSlide];
    const url = form.action + '/' + objectId;
    ajaxHandler.fetch(form, url, {method: 'DELETE'}, actions);
  }
};
