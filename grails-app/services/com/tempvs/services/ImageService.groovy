package com.tempvs.services

import com.mongodb.gridfs.GridFSFile
import grails.transaction.Transactional

@Transactional
class ImageService {
    def springSecurityService
    def fileDAOService
    private static final String AVATAR_PATH = 'avatar'

    Boolean updateAvatar(InputStream inputStream) {
        String collection = "${AVATAR_PATH}_${springSecurityService.currentUser.id}"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]

        GridFSFile oldAvatar = fileDAOService.get(collection, query)
        GridFSFile newAvatar = fileDAOService.create(inputStream, collection, AVATAR_PATH)
        fileDAOService.save(oldAvatar, [currentAvatar: null])
        fileDAOService.save(newAvatar, [currentAvatar: Boolean.TRUE])
    }

    List<Byte> getOwnAvatar() {
        String collection = "${AVATAR_PATH}_${springSecurityService.currentUser.id}"
        Map query = [metadata: [currentAvatar: Boolean.TRUE]]
        List<Byte> fileInBytes

        InputStream inputStream = fileDAOService.get(collection, query)?.inputStream

        //throw new RuntimeException(String.valueOf(inputStream.bytes.length))

        if (inputStream) {
            fileInBytes = inputStream.bytes
            inputStream.close()
        }

        fileInBytes
    }
}
