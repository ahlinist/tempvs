package com.tempvs.item

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import org.codehaus.groovy.runtime.InvokerHelper

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

    Source editSource(Source source, Map properties) {
        InvokerHelper.setProperties(source, properties)
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
