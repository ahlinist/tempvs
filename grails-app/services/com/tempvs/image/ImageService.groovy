package com.tempvs.image

import grails.transaction.Transactional
import groovy.transform.CompileStatic
import org.springframework.util.StreamUtils

@Transactional
@CompileStatic
class ImageService {

    private static final String AVATAR_PATH = 'avatar'

    ImageDAO imageDAO

    Boolean updateAvatar(InputStream inputStream, String collection) {
        Image currentAvatar = imageDAO.get(collection, [metadata: [currentAvatar: Boolean.TRUE]])
        Image newAvatar = imageDAO.create(inputStream, collection, AVATAR_PATH)

        imageDAO.save(currentAvatar, [currentAvatar: null])
        imageDAO.save(newAvatar, [currentAvatar: Boolean.TRUE])

        Boolean.TRUE
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
