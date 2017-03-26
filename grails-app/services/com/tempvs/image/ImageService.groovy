package com.tempvs.image

import com.tempvs.image.Image
import grails.transaction.Transactional

@Transactional
class ImageService {
    def springSecurityService
    def imageDAO
    private static final String AVATAR_PATH = 'avatar'

    void updateAvatar(InputStream inputStream) {
        String collection = "${AVATAR_PATH}_${springSecurityService.currentUser.id}"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        Image currentAvatar = imageDAO.get(collection, query)
        Image newAvatar = imageDAO.create(inputStream, collection, AVATAR_PATH)

        imageDAO.save(currentAvatar, [currentAvatar: null])
        imageDAO.save(newAvatar, [currentAvatar: Boolean.TRUE])
    }

    List<Byte> getOwnAvatar() {
        String collection = "${AVATAR_PATH}_${springSecurityService.currentUser.id}"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        try {
            imageDAO.get(collection, query)?.bytes
        } catch (Exception e) {
            null
        }
    }
}
