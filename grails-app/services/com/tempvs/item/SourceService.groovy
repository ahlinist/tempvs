package com.tempvs.item

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.AccessDeniedException

/**
 * Service that manages operations with {@link com.tempvs.item.Source}
 */
@GrailsCompileStatic
class SourceService {

    private static final String PERIOD_FIELD = 'period'

    ImageService imageService
    ObjectDAOService objectDAOService

    Source getSource(Object id) {
        if (id) {
            objectDAOService.get(Source, id)
        }
    }

    List<Source> getSourcesByPeriod(Period period) {
        Source.findAllByPeriod(period)
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    Source editSourceField(Source source, String fieldName, String fieldValue) {
        if (fieldName == PERIOD_FIELD) {
            throw new AccessDeniedException('Operation not supported.')
        } else {
            source."${fieldName}" = fieldValue
        }

        objectDAOService.save(source)
    }

    Source updateSource(Source source, List<Image> images = []) {
        images.each { Image image ->
            if (image) {
                source.addToImages(image)
            }
        }

        objectDAOService.save(source)
    }

    void deleteItem(Source source) {
        imageService.deleteImages(source.images)
        objectDAOService.delete(source)
    }

    Source deleteImage(Source source, Image image) {
        if (!source.images.contains(image)) {
            throw new AccessDeniedException('Source does not contain the given image.')
        }

        source.removeFromImages(image)
        imageService.deleteImage(image)
        objectDAOService.save(source)
    }
}
