package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper
import org.springframework.web.multipart.MultipartFile

/**
 * Service for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@Transactional
@GrailsCompileStatic
class ProfileService {

    private static final String AVATAR_COLLECTION = 'avatar'

    UserService userService
    ObjectDAO<BaseProfile> objectDAO
    ObjectFactory objectFactory
    ImageService imageService

    BaseProfile getProfile(Class clazz, String id) {
        objectDAO.find(clazz, [profileId: id]) ?: objectDAO.get(clazz, id)
    }

    BaseProfile updateProfile(BaseProfile profile, Map params) {
        InvokerHelper.setProperties(profile, params)
        profile.save()
        profile
    }

    BaseProfile createClubProfile(Map properties) {
        User user = userService.currentUser
        BaseProfile profile = objectFactory.create(ClubProfile)
        InvokerHelper.setProperties(profile, properties)
        user.addToClubProfiles(profile)?.save()
        profile
    }

    BaseProfile updateProfileEmail(Class clazz, Long instanceId, String profileEmail) {
        BaseProfile profile = objectDAO.get(clazz, instanceId)
        profile.profileEmail = profileEmail
        profile.save()
        profile
    }

    BaseProfile updateAvatar(BaseProfile profile, MultipartFile multipartFile, String imageInfo) {
        Image avatar = profile.avatar ?: objectFactory.create(Image) as Image
        avatar.imageInfo = imageInfo
        avatar.collection = AVATAR_COLLECTION
        avatar.objectId = imageService.replaceImage(multipartFile, avatar)
        profile.avatar = avatar
        profile.save()
        profile
    }

    Boolean deleteProfile(BaseProfile profile) {
        imageService.deleteImage(profile.avatar)

        try {
            profile.delete(failOnError: true)
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }
}
