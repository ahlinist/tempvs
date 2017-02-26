package com.tempvs.services

import com.mongodb.gridfs.GridFSFile
import grails.transaction.Transactional

@Transactional
class ImageService {
    def springSecurityService
    def mongoDAOService
    private static final String AVATAR_PATH = 'avatar'

    Boolean updateAvatar(multiPartFile) {
        String collection = "${AVATAR_PATH}_${springSecurityService.currentUser.id}"
        Map metaData = [currentAvatar: Boolean.TRUE]

        InputStream inputStream = multiPartFile.inputStream

        try {
            GridFSFile oldAvatar = mongoDAOService.get(collection, metaData)
            GridFSFile newAvatar = mongoDAOService.create(multiPartFile.inputStream, collection, AVATAR_PATH)
            mongoDAOService.save(oldAvatar, [currentAvatar: null])
            mongoDAOService.save(newAvatar, [currentAvatar: Boolean.TRUE])
        } finally {
            inputStream?.close()
        }
    }

    List<Byte> getOwnAvatar() {
        String collection = "${AVATAR_PATH}_${springSecurityService.currentUser.id}"
        Map metaData = [currentAvatar: Boolean.TRUE]
        List<Byte> fileInBytes

        InputStream inputStream = mongoDAOService.get(collection, metaData)?.inputStream

        if (inputStream) {
            fileInBytes = inputStream.bytes
            inputStream.close()
        }

        fileInBytes
    }
}
