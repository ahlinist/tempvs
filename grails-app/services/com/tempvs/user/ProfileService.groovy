package com.tempvs.user

import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.transaction.Transactional
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@Transactional
class ProfileService {

    private static final String AVATAR_COLLECTION = 'avatar'

    UserService userService
    ImageService imageService

    public <T> T getProfile(Class<T> clazz, id) {
        clazz.findByProfileId(id as String) ?: clazz.get(id)
    }

    BaseProfile getProfileByProfileEmail(Class clazz, String email) {
        clazz.findByProfileEmail(email)
    }

    BaseProfile createProfile(BaseProfile profile, ImageUploadBean imageUploadBean) {
        Image avatar = imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION)
        profile.avatar = avatar
        profile.user = userService.currentUser
        profile.save()
        profile
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    void deleteProfile(BaseProfile profile) {
        imageService.deleteImage(profile.avatar)
        profile.delete()
    }

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

        profile.save()
        profile
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    BaseProfile uploadAvatar(BaseProfile profile, ImageUploadBean imageUploadBean) {
        profile.avatar = imageService.uploadImage(imageUploadBean, AVATAR_COLLECTION, profile.avatar)
        profile.save()
        profile
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    BaseProfile deleteAvatar(BaseProfile profile) {
        imageService.deleteImage(profile.avatar)
        profile.avatar = null
        profile.save()
        profile
    }

    Boolean isProfileEmailUnique(BaseProfile profile, String email) {
        User user = userService.getUserByEmail(email)
        User profileUser = profile.user

        if (user && profileUser != user) {
            return Boolean.FALSE
        }

        BaseProfile persistedProfile = profile.class.findByProfileEmail email

        if (persistedProfile && profileUser != persistedProfile.user) {
            return Boolean.FALSE
        }

        return Boolean.TRUE
    }
}
