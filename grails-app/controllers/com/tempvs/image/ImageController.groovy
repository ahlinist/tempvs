package com.tempvs.image

import org.springframework.util.StreamUtils

class ImageController {

    def imageService
    def assetResourceLocator
    def ajaxResponseService
    def userService
    def springSecurityService

    private static final String AVATAR_UPDATED_MESSAGE = 'user.profile.update.avatar.success.message'
    private static final String AVATAR_UPDATED_FAILED_MESSAGE = 'user.profile.update.avatar.failed.message'
    private static final String IMAGE_EMPTY = 'upload.image.empty'
    private static final String DEFAULT_AVATAR = 'defaultAvatar.jpg'
    private static final String AVATAR_FIELD = 'avatar'

    def updateAvatar() {
        Boolean success
        String message
        def multiPartFile = request.getFile(AVATAR_FIELD)

        if (!multiPartFile?.empty) {
            InputStream inputStream = multiPartFile.inputStream

            try {
                imageService.updateAvatar(inputStream)
                success = Boolean.TRUE
                message = AVATAR_UPDATED_MESSAGE
            } catch (Exception e) {
                success = Boolean.FALSE
                message = AVATAR_UPDATED_FAILED_MESSAGE
            } finally {
                inputStream?.close()
            }
        } else {
            success = Boolean.FALSE
            message = IMAGE_EMPTY
        }

        render ajaxResponseService.renderMessage(success, message)
    }

    def getAvatar(String id) {
        byte[] imageInBytes = imageService.getAvatar(id) ?:
                assetResourceLocator?.findAssetForURI(DEFAULT_AVATAR)?.getInputStream()?.bytes ?:
                        StreamUtils.emptyInput().bytes

        response.with{
            setHeader('Content-length', imageInBytes.length.toString())
            contentType = 'image/jpg' // or the appropriate image content type
            outputStream << imageInBytes
            outputStream.flush()
        }
    }
}
