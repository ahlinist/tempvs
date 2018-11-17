package club.tempvs.message

import groovy.transform.CompileStatic

@CompileStatic
class ConversationListItem {
    Long id
    String type;
    String name;
    MessageBean lastMessage;
    String conversant;
}
