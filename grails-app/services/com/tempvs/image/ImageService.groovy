package com.tempvs.image

import com.tempvs.user.User
import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional
import groovy.transform.CompileStatic
import org.springframework.util.StreamUtils

@Transactional
@CompileStatic
class ImageService {
    SpringSecurityService springSecurityService
    ImageDAO imageDAO
    private static final String AVATAR_PATH = 'avatar'

    void updateAvatar(InputStream inputStream) {
        User user = springSecurityService.currentUser as User
        String collection = "${AVATAR_PATH}_${user.id}"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        Image currentAvatar = imageDAO.get(collection, query)
        Image newAvatar = imageDAO.create(inputStream, collection, AVATAR_PATH)

        imageDAO.save(currentAvatar, [currentAvatar: null])
        imageDAO.save(newAvatar, [currentAvatar: Boolean.TRUE])
    }

    byte[] getAvatar(String id) {
        String collection = "${AVATAR_PATH}_${id}"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        try {
            imageDAO.get(collection, query).bytes
        } catch (Exception e) {
            StreamUtils.emptyInput().bytes
        }
    }
}
