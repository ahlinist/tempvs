package com.tempvs.controllers

import com.tempvs.domain.user.UserProfile
import javax.imageio.ImageIO

class UserProfileController {
    def springSecurityService
    def userProfileService
    String DEFAULT_AVATAR = '/home/albvs/storage/grails/images/defaultAvatar.jpg'

    def index() {
        [userProfile: springSecurityService.currentUser.userProfile]
    }

    def updateUserProfile(UserProfileCommand upc) {
        UserProfile userProfile = userProfileService.updateUserProfile(upc.properties)
        render view: 'index', model: [userProfile: userProfile,
                                      success: userProfile.hasErrors() ? '' : 'user.userProfile.updated']
    }

    def updateAvatar() {
        def multiPartFile = request.getFile('avatar')

        if (!multiPartFile?.empty) {
            if (userProfileService.updateAvatar(multiPartFile)) {
                flash.avatarSuccess = 'user.profile.update.avatar.success.message'
            } else {
                flash.avatarError = 'user.profile.update.avatar.error.message'
            }
        } else {
            flash.avatarError = 'user.profile.update.avatar.empty.message'
        }

        redirect action: 'index'
    }

    def getAvatar() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ImageIO.write(ImageIO.read(new File(userProfileService.getOwnAvatar() ?: DEFAULT_AVATAR)), "jpg", baos)
        byte[] imageInByte = baos.toByteArray()
        response.with{
            setHeader('Content-length', imageInByte.length.toString())
            contentType = 'image/jpg' // or the appropriate image content type
            outputStream << imageInByte
            outputStream.flush()
        }
    }
}

class UserProfileCommand {
    String firstName
    String lastName
    String profileEmail
    String location
    String customId

    static constraints = {
        importFrom UserProfile
    }
}