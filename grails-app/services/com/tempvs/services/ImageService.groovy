package com.tempvs.services

import com.tempvs.domain.image.Avatar
import grails.transaction.Transactional

@Transactional
class ImageService {
    def springSecurityService
    private static final String IMAGE_EMPTY = 'upload.image.empty'

    Avatar updateAvatar(multiPartFile) {
        Avatar avatar = springSecurityService.currentUser.userProfile.avatar

        if (!multiPartFile?.empty) {
            String imageName = "${new Date().time}.jpg"
            String destination = "/home/albvs/storage/grails/images/users/${avatar.userProfile.user.id}/avatars/"
            String pathToFile = destination.concat imageName
            File directory = new File(destination)

            if(!directory.exists()){
                directory.mkdirs()
            }

            multiPartFile.transferTo new File(pathToFile)
            avatar.pathToFile = pathToFile
            avatar.save(flush: true)
        } else {
            avatar.errors.rejectValue('pathToFile', IMAGE_EMPTY)
        }

        avatar
    }

    Avatar getOwnAvatar() {
        springSecurityService.currentUser?.userProfile?.avatar
    }
}
