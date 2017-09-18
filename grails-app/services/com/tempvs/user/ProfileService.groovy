package com.tempvs.user

import com.tempvs.domain.ObjectDAO
import com.tempvs.domain.ObjectFactory
import com.tempvs.image.Image
import com.tempvs.image.ImageService
import com.tempvs.image.ImageUploadBean
import grails.compiler.GrailsCompileStatic
import grails.transaction.Transactional
import org.springframework.security.access.prepost.PreAuthorize

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

    public <T> T getProfile(Class<T> clazz, Object id) {
        objectDAO.find(clazz, [profileId: id]) ?: objectDAO.get(clazz, id)
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    BaseProfile saveProfile(BaseProfile profile) {
        profile.save()
        profile
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    Boolean deleteProfile(BaseProfile profile) {
        deleteAvatar(profile)

        try {
            profile.delete(failOnError: true)
            Boolean.TRUE
        } catch (Throwable e) {
            Boolean.FALSE
        }
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    void deleteAvatar(BaseProfile profile) {
        imageService.deleteImage(profile.avatar)
        profile.avatar = null
        profile.save()
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    BaseProfile editAvatar(BaseProfile profile, ImageUploadBean imageUploadBean) {
        Image avatar = imageService.updateImage(imageUploadBean, AVATAR_COLLECTION, profile.avatar)

        if (avatar) {
            profile.avatar = avatar
            profile.save()
        }

        profile
    }
}
