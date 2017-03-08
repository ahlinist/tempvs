package com.tempvs.services

import grails.transaction.Transactional

@Transactional
class ImageService {
    def springSecurityService
    def fileDAOService
    private static final String AVATAR_PATH = 'avatar'

    void updateAvatar(InputStream inputStream) {
        String collection = "${AVATAR_PATH}_${springSecurityService.currentUser.id}"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        fileDAOService.save(fileDAOService.get(collection, query), [currentAvatar: null])
        fileDAOService.save(fileDAOService.create(inputStream, collection, AVATAR_PATH), [currentAvatar: Boolean.TRUE])
    }

    List<Byte> getOwnAvatar() {
        String collection = "${AVATAR_PATH}_${springSecurityService.currentUser.id}"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        try {
            fileDAOService.get(collection, query)?.inputStream?.bytes
        } catch (Exception e) {
            null
        }
    }
}
