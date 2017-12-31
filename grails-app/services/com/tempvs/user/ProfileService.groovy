package com.tempvs.user

import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.item.PassportService
import com.tempvs.periodization.Period
import grails.gorm.transactions.Transactional
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service for managing {@link com.tempvs.user.UserProfile} and
 * {@link com.tempvs.user.ClubProfile}.
 */
@Transactional
class ProfileService {

    private static String PERIOD_FIELD = 'period'
    private static String PROFILE_EMAIL_FIELD = 'profileEmail'
    private static String EMAIL_USED_CODE = 'userProfile.profileEmail.used.error'

    UserService userService
    ImageService imageService
    PassportService passportService

    public <T> T getProfile(Class<T> clazz, id) {
        clazz.findByProfileId(id as String) ?: clazz.get(id)
    }

    public <T> T getProfileByProfileEmail(Class<T> clazz, String email) {
        clazz.findByProfileEmail(email)
    }

    BaseProfile createProfile(BaseProfile profile, Image avatar) {
        if (!isProfileEmailUnique(profile, profile.profileEmail)) {
            profile.errors.rejectValue(PROFILE_EMAIL_FIELD, EMAIL_USED_CODE, [profile.profileEmail] as Object[], EMAIL_USED_CODE)
            return profile
        }

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
        if (fieldName == PERIOD_FIELD) {
            try {
                profile.period = Period.valueOf(fieldValue)
            } catch (IllegalArgumentException exception) {
                profile.period = null
            }
        } else if ((fieldName == PROFILE_EMAIL_FIELD) && !isProfileEmailUnique(profile, profile.profileEmail)) {
            profile.errors.rejectValue(PROFILE_EMAIL_FIELD, EMAIL_USED_CODE, [fieldValue] as Object[], EMAIL_USED_CODE)
            return profile
        } else {
            profile."${fieldName}" = fieldValue
        }

        profile.save()
        profile
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    BaseProfile uploadAvatar(BaseProfile profile, Image avatar) {
        profile.avatar = avatar
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
        if (email) {
            User user = userService.getUserByEmail(email)
            User profileUser = profile.user

            if (user && profileUser != user) {
                return Boolean.FALSE
            }

            BaseProfile persistedProfile = profile.class.findByProfileEmail email

            if (persistedProfile && profileUser != persistedProfile.user) {
                return Boolean.FALSE
            }
        }

        return Boolean.TRUE
    }
}
