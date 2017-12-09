package com.tempvs.item

import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.periodization.Period
import grails.transaction.Transactional
import org.springframework.security.access.AccessDeniedException

/**
 * Service that manages operations with {@link com.tempvs.item.Source}
 */
@Transactional
class SourceService {

    private static final String PERIOD_FIELD = 'period'

    ImageService imageService

    Source getSource(Long id) {
        Source.get id
    }

    List<Source> getSourcesByPeriod(Period period) {
        Source.findAllByPeriod period
    }

    Source editSourceField(Source source, String fieldName, String fieldValue) {
        if (fieldName == PERIOD_FIELD) {
            throw new AccessDeniedException('Operation not supported.')
        } else {
            source."${fieldName}" = fieldValue
        }

        source.save()
        source
    }

    Source updateSource(Source source, List<Image> images = []) {
        images.each { Image image ->
            if (image) {
                source.addToImages image
            }
        }

        source.save()
        source
    }

    void deleteSource(Source source) {
        imageService.deleteImages source.images
        Item2Source.findAllBySource(source)*.delete()
        source.delete()
    }

    Source deleteImage(Source source, Image image) {
        if (!source.images.contains(image)) {
            throw new AccessDeniedException('Source does not contain the given image.')
        }

        source.removeFromImages(image)
        imageService.deleteImage image
        source.save()
        source
    }
}
