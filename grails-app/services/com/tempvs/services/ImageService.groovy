package com.tempvs.services

import com.tempvs.domain.image.Avatar
import grails.transaction.Transactional

@Transactional
class ImageService {
    def springSecurityService

    Avatar updateAvatar(multiPartFile) {
        if (!multiPartFile?.empty) {
            Avatar avatar = springSecurityService.currentUser.userProfile.avatar
            String imageName = "${new Date().time}.jpg"
            String destination = "/home/albvs/storage/grails/images/users/${avatar.userProfile.user.id}/avatars/"
            String pathToFile = destination.concat imageName
            File directory = new File(destination)

            if(!directory.exists()){
                directory.mkdirs()
            }

            multiPartFile.transferTo new File(pathToFile)
            avatar.pathToFile = pathToFile
            avatar.save()
        }
    }

    Avatar getOwnAvatar() {
        springSecurityService.currentUser?.userProfile?.avatar
    }
}
