package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class AddMessagePayload {
    Participant author
    String text
}
