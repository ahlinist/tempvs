package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * Service for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@Transactional
@GrailsCompileStatic
class ProfileService {

    private static final String AVATAR_COLLECTION = 'avatar'

    UserService userService
    ObjectDAO objectDAO
    ObjectFactory objectFactory
    ImageService imageService

    public <T> T getProfile(Class<T> clazz, String id) {
        objectDAO.find(clazz, [profileId: id]) ?: objectDAO.get(clazz, id)
    }

    BaseProfile updateProfile(BaseProfile profile, Map properties) {
        InvokerHelper.setProperties(profile, properties)
        Image extractedImage = imageService.extractImage(properties.avatarBean as ImageUploadBean, AVATAR_COLLECTION, profile.avatar)

        if (extractedImage) {
            profile.avatar = extractedImage
        }

        profile.save()
        profile
    }

    BaseProfile createClubProfile(Map properties) {
        ClubProfile clubProfile = objectFactory.create(ClubProfile)
        InvokerHelper.setProperties(clubProfile, properties)
        clubProfile.avatar = imageService.extractImage(properties.avatarBean as ImageUploadBean, AVATAR_COLLECTION)
        userService.currentUser.addToClubProfiles(clubProfile)?.save()
        clubProfile
    }

    BaseProfile updateProfileEmail(Class clazz, Long instanceId, String profileEmail) {
        BaseProfile profile = objectDAO.get(clazz, instanceId)
        profile.profileEmail = profileEmail
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
