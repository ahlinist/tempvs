package com.tempvs.user

import com.tempvs.domain.ObjectDAOService
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
class ProfileService {

    private static final String AVATAR_COLLECTION = 'avatar'

    UserService userService
    ImageService imageService
    ObjectDAOService objectDAOService

    public <T> T getProfile(Class<T> clazz, Object id) {
        objectDAOService.find(clazz, [profileId: id]) ?: objectDAOService.get(clazz, id)
    }

    BaseProfile createProfile(BaseProfile profile, ImageUploadBean imageUploadBean) {
        Image avatar = imageService.updateImage(imageUploadBean, AVATAR_COLLECTION)
        profile.avatar = avatar
        profile.user = userService.currentUser
        objectDAOService.save(profile)
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    void deleteProfile(BaseProfile profile) {
        imageService.deleteImage(profile.avatar)
        profile.delete()
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#profile.user.email == authentication.name')
    BaseProfile editProfileField(BaseProfile profile, String fieldName, String fieldValue) {
        if (fieldName == 'period') {
            try {
                profile.period = Period.valueOf(fieldValue)
            } catch (IllegalArgumentException exception) {
                profile.period = null
            }
        } else {
            profile."${fieldName}" = fieldValue
        }

        objectDAOService.save(profile)
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    BaseProfile uploadAvatar(BaseProfile profile, ImageUploadBean imageUploadBean) {
        profile.avatar = imageService.updateImage(imageUploadBean, AVATAR_COLLECTION, profile.avatar)
        objectDAOService.save(profile)
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    BaseProfile deleteAvatar(BaseProfile profile) {
        imageService.deleteImage(profile.avatar)
        profile.avatar = null
        objectDAOService.save(profile)
    }
}
