package com.tempvs.item

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import groovy.transform.TypeCheckingMode

/**
 * Service that manages operations with {@link com.tempvs.item.Source}
 */
@GrailsCompileStatic
class SourceService {

    private static final String SOURCE_COLLECTION = 'source'

    ImageService imageService
    ObjectDAOService objectDAOService

    Source getSource(Object id) {
        objectDAOService.get(Source, id)
    }

    List<Source> getSourcesByPeriod(Period period) {
        Source.findAllByPeriod(period)
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    Source editSourceField(Source source, String fieldName, String fieldValue) {
        source."${fieldName}" = fieldValue
        objectDAOService.save(source)
    }

    Source createSource(Source source, List<ImageUploadBean> imageUploadBeans) {
        List<Image> images = imageService.uploadImages(imageUploadBeans, SOURCE_COLLECTION)

        images?.each { Image image ->
            source.addToImages(image)
        }

        objectDAOService.save(source)
    }
}
