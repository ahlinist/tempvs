package com.tempvs.controllers

import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import javax.imageio.ImageIO

class UserProfileController {
    def springSecurityService
    def userService
    def userProfileService
    String DEFAULT_AVATAR = '/home/albvs/storage/grails/images/defaultAvatar.jpg'

    def index() {
        [userProfile: springSecurityService.currentUser.userProfile]
    }

    def updateUserProfile(UserProfileCommand upc) {
        User currentUser = springSecurityService.currentUser

        if (currentUser.email == upc.profileEmail ||
                currentUser.userProfile.profileEmail == upc.profileEmail ||
                !userService.checkIfUserExists(upc.profileEmail)) {
            if (userProfileService.updateUserProfile(upc.properties)) {
                flash.success = 'user.userProfile.updated'
            } else {
                flash.error = 'user.editUserProfile.failed'
            }
        } else {
            flash.error = 'user.editUserProfile.email.used'
        }

        redirect action: 'index'
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