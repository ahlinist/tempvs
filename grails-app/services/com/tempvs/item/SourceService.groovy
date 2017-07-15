package com.tempvs.item

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
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

    ObjectDAO objectDAO
    ObjectFactory objectFactory

    Source getSource(String id) {
        objectDAO.get(Source, id)
    }

    List<Source> getSourcesByPeriod(Period period) {
        objectDAO.findAll(Source, [period: period])
    }

    Source createSource(Map params) {
        Source source = objectFactory.create(Source)
        editSource(source, params)
    }

    Source editSource(Source source, Map params) {
        InvokerHelper.setProperties(source, params)
        source.save()
        source
    }
}
