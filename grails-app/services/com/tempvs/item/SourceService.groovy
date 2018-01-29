package com.tempvs.item

import com.tempvs.communication.Comment
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages operations with {@link com.tempvs.item.Source}
 */
@Transactional
@GrailsCompileStatic
class SourceService {

    private static final String TYPE_FIELD = 'type'
    private static final String PERIOD_FIELD = 'period'

    ImageService imageService

    Source getSource(Long id) {
        Source.get id
    }

    List<Source> getSourcesByPeriod(Period period) {
        Source.findAllByPeriod period
    }

    List<Source> getSourcesByPeriodAndType(Period period, Type type) {
        Source.findAllByPeriodAndType period, type
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    Source editSourceField(Source source, String fieldName, String fieldValue) {
        if (fieldName in [PERIOD_FIELD, TYPE_FIELD]) {
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
        List<Item2Source> item2Sources = Item2Source.findAllBySource(source)
        item2Sources*.delete()
        imageService.deleteImages source.images
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

    Source addComment(Source source, Comment comment) {
        source.addToComments(comment)
        source.save()
        source
    }

    @PreAuthorize('(#comment.userProfile != null and #comment.userProfile.user.email == authentication.name) or (#comment.clubProfile != null and #comment.clubProfile.user.email == authentication.name)')
    Source deleteComment(Source source, Comment comment) {
        source.removeFromComments(comment)
        source.save()
        source
    }
}
