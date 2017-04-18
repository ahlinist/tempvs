package com.tempvs.image

import com.tempvs.user.BaseProfile
import com.tempvs.user.ProfileHolder
import org.springframework.util.StreamUtils

class ImageController {

    private static final String AVATAR_UPDATED_MESSAGE = 'userProfile.update.avatar.success.message'
    private static final String AVATAR_UPDATED_FAILED_MESSAGE = 'userProfile.update.avatar.failed.message'
    private static final String IMAGE_EMPTY = 'upload.image.empty'
    private static final String DEFAULT_AVATAR = 'defaultAvatar.jpg'
    private static final String AVATAR_FIELD = 'avatar'

    def imageService
    def assetResourceLocator
    def ajaxResponseService
    ProfileHolder profileHolder

    def updateAvatar() {
        Boolean success = Boolean.FALSE
        String message
        def multiPartFile = request.getFile(AVATAR_FIELD)

        if (!multiPartFile?.empty) {
            InputStream inputStream = multiPartFile.inputStream

            BaseProfile profile = profileHolder.profile

            try {
                String imageId = "${profile.user.id}_${profile.class.simpleName}_${profile.id}"
                String collection = "${AVATAR_FIELD}_${imageId}"
                success = imageService.updateAvatar(inputStream, collection)
                message = AVATAR_UPDATED_MESSAGE
            } catch (Exception e) {
                message = AVATAR_UPDATED_FAILED_MESSAGE
            } finally {
                inputStream?.close()
            }
        } else {
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
