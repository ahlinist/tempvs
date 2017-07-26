package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Service that manages operations with {@link com.tempvs.item.Source}
 */
@Transactional
@GrailsCompileStatic
class SourceService {

    private static final String SOURCE_COLLECTION = 'source'

    ObjectDAO objectDAO
    ImageService imageService
    ObjectFactory objectFactory

    Source getSource(String id) {
        objectDAO.get(Source, id)
    }

    List<Source> getSourcesByPeriod(Period period) {
        objectDAO.findAll(Source, [period: period])
    }

    Source createSource(Map params) {
        Source source = objectFactory.create(Source)
        Set<Image> sourceImages = imageService.extractImages(params.imageBeans as List<ImageUploadBean>, SOURCE_COLLECTION)

        if (sourceImages) {
            source.images = sourceImages
        }

        editSource(source, params)
    }

    Source editSource(Source source, Map properties) {
        InvokerHelper.setProperties(source, properties)
        source.save()
        source
    }
}
