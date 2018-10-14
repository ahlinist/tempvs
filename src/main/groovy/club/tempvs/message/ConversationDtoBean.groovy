package club.tempvs.message

class ConversationDtoBean {
    Long id
    String type
    String name
    MessageDtoBean lastMessage
    Set<Long> participants
}
