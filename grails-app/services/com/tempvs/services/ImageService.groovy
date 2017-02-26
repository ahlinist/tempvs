package com.tempvs.services

import com.mongodb.BasicDBObject
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import grails.transaction.Transactional
import org.apache.commons.io.IOUtils

@Transactional
class ImageService {
    def springSecurityService
    def mongoDB
    private static final String AVATAR_PATH = 'avatar'
    private static final String METADATA = 'metadata'
    private static final String CURRENT_AVATAR = 'currentAvatar'

    Boolean updateAvatar(multiPartFile) {
        try {
            GridFS gfsPhoto = new GridFS(mongoDB, "${AVATAR_PATH}_${springSecurityService.currentUser.id}")
            GridFSInputFile newAvatar = gfsPhoto.createFile(multiPartFile.bytes)
            newAvatar.setFilename(AVATAR_PATH)
            newAvatar.setMetaData(new BasicDBObject(CURRENT_AVATAR, Boolean.TRUE.toString()))
            GridFSDBFile currentAvatar = currentAvatar
            currentAvatar?.setMetaData(new BasicDBObject(METADATA, new BasicDBObject(CURRENT_AVATAR, new String())))
            currentAvatar?.save()
            newAvatar.save()
        } catch (Exception e) {
            return Boolean.FALSE
        }

        return Boolean.TRUE
    }

    byte[] getOwnAvatar() {
        IOUtils.toByteArray(currentAvatar.inputStream)
    }

    private GridFSDBFile getCurrentAvatar() {
        GridFS gfsPhoto = new GridFS(mongoDB, "${AVATAR_PATH}_${springSecurityService.currentUser.id}")
        BasicDBObject query = new BasicDBObject(METADATA, new BasicDBObject(CURRENT_AVATAR, Boolean.TRUE.toString()));
        gfsPhoto.findOne(query)
    }
}
