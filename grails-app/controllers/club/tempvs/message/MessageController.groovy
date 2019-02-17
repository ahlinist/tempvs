package club.tempvs.message

import grails.compiler.GrailsCompileStatic
import groovy.util.logging.Slf4j
import org.springframework.security.access.annotation.Secured

@Slf4j
@GrailsCompileStatic
@Secured('isAuthenticated()')
class MessageController {

    static defaultAction = 'conversation'

    def conversation() {
    }
}
