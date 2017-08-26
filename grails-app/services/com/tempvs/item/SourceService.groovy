package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional

/**
 * Service that manages operations with {@link com.tempvs.item.Source}
 */
@Transactional
@GrailsCompileStatic
class SourceService {

    ObjectDAO objectDAO
    ImageService imageService
    ObjectFactory objectFactory

    Source getSource(Object id) {
        objectDAO.get(Source, id)
    }

    List<Source> getSourcesByPeriod(Period period) {
        objectDAO.findAll(Source, [period: period])
    }

    Source saveSource(Source source, List<Image> images = null) {
        images?.each { Image image ->
            source.addToImages(image)
        }

        source.save()
        source
    }
}
