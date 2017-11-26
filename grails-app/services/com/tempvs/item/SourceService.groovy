package com.tempvs.item

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import groovy.transform.TypeCheckingMode

/**
 * Service that manages operations with {@link com.tempvs.item.Source}
 */
@GrailsCompileStatic
class SourceService {

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
        source."${fieldName}" = fieldValue
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
}
