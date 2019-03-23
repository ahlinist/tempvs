package club.tempvs.item

import club.tempvs.communication.Comment
import club.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages operations with {@link Source}
 */
@Transactional
@GrailsCompileStatic
class SourceService {

    Source getSource(Long id) {
        Source.get id
    }

    Source loadSource(Long id) {
        Source.load id
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    List<Source> getSourcesByPeriodAndItemType(Period period, ItemType itemType, List<Source> excludedSources = []) {
        Source.withCriteria {
            and {
                eq 'period', period
                eq 'itemType', itemType

                if (excludedSources) {
                    not {
                        'in' 'id',excludedSources*.id
                    }
                }
            }
        }
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
