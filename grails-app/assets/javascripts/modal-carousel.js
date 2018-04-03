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
    deleteImage: function(button, controller, objectId) {
        $('.carousel').off('slide.bs.carousel');
        hideModals();
        var imageId = modalCarousel.slideMapping[modalCarousel.currentSlide];
        var url = '/' + controller + '/deleteImage?objectId=' + objectId + '&imageId=' + imageId;
        ajaxHandler.processAjaxRequest(button, url, '', 'DELETE', '#modal-carousel', ajaxHandler.actions);
    }
};
