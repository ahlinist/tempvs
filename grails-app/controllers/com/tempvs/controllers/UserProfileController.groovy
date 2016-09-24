package com.tempvs.controllers

import com.tempvs.ajax.AjaxResponse
import com.tempvs.domain.user.UserProfile
import grails.converters.JSON

import javax.imageio.ImageIO

class UserProfileController {
    def springSecurityService
    def userProfileService
    def imageService
    private static final String USER_PROFILE_UPDATED_MESSAGE = 'user.userProfile.updated'
    private static final String AVATAR_UPDATED_MESSAGE = 'user.profile.update.avatar.success.message'

    def index() {
        [user: springSecurityService.currentUser]
    }

    def updateUserProfile(UserProfileCommand upc) {
        render new AjaxResponse(userProfileService.updateUserProfile(upc.properties), USER_PROFILE_UPDATED_MESSAGE) as JSON
    }

    def updateAvatar() {
        def multiPartFile = request.getFile('avatar')
        render new AjaxResponse(imageService.updateAvatar(multiPartFile), AVATAR_UPDATED_MESSAGE, multiPartFile) as JSON
    }

    def getAvatar() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ImageIO.write(ImageIO.read(new File(imageService.getOwnAvatar().pathToFile)), "jpg", baos)
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