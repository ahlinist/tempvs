package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class ConversationsPayload {
    List<ConversationBean> conversations

    @CompileStatic
    class ConversationBean {
        Long id
        String type
        String name
        MessageBean lastMessage
        String conversant
    }
}
