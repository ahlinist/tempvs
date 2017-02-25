package com.tempvs.services

import com.mongodb.DB
import com.mongodb.Mongo
import com.mongodb.gridfs.GridFS
import com.mongodb.gridfs.GridFSDBFile
import com.mongodb.gridfs.GridFSInputFile
import com.tempvs.domain.user.User
import grails.transaction.Transactional
import org.apache.commons.io.IOUtils

@Transactional
class ImageService {
    def springSecurityService
    Mongo mongo = new Mongo("localhost", 27017);
    DB db = mongo.getDB("imagedb");

    Boolean updateAvatar(multiPartFile) {
        User user = springSecurityService.currentUser

        String fileName = "user/avatar/${user.id}"
        GridFS gfsPhoto = new GridFS(db, "photo");
        GridFSInputFile gfsFile = gfsPhoto.createFile(multiPartFile.bytes);
        gfsFile.setFilename(fileName);
        gfsFile.save();
        return true
    }

    byte[] getOwnAvatar() {
        User user = springSecurityService.currentUser
        String fileName = "user/avatar/${user.id}"
        GridFS gfsPhoto = new GridFS(db, "photo");
        GridFSDBFile imageForOutput = gfsPhoto.findOne(fileName);
        IOUtils.toByteArray(imageForOutput.inputStream);
    }
}
