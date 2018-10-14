package club.tempvs.message

import java.time.Instant

class MessageDtoBean {
    Long id
    String text
    Long author
    Long subject
    Instant createdDate
    Set<Long> newFor = new HashSet<>()
    Boolean isSystem
}
