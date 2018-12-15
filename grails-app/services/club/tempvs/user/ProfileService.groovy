package club.tempvs.user

import club.tempvs.ampq.AmqpSender
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.object.ObjectFactory
import club.tempvs.profile.ProfileDto
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.gorm.transactions.Transactional
import grails.validation.ValidationException
import grails.web.mapping.LinkGenerator
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service for {@link Profile} managing.
 */
@Transactional
@GrailsCompileStatic
class ProfileService {

    private static final String PERIOD_FIELD = 'period'
    private static final Integer MAX_PROFILES_RETRIEVED = 10
    private static final String PROFILE_EMAIL_FIELD = 'profileEmail'
    private static final String EMAIL_USED_CODE = 'userProfile.profileEmail.used.error'
    private static final String MESSAGE_PARTICIPANT_AMPQ_QUEUE = 'message.participant'

    UserService userService
    ImageService imageService
    LinkGenerator grailsLinkGenerator
    AmqpSender amqpSender
    ObjectFactory objectFactory

    Profile getProfile(id) {
        Profile profile = Profile.findByProfileId(id as String)

        try {
            if (!profile) {
                profile = getProfileById(id as Long)
            }
        } catch (Exception e) {
            return null
        }

        return profile
    }

    Profile getProfileById(Long id) {
        Profile.get(id)
    }

    List<Profile> getProfilesByProfileEmail(String email) {
        Profile.findAllByProfileEmail(email)
    }

    Profile getCurrentProfile() {
        userService.currentUser?.currentProfile
    }

    @PreAuthorize('(#profile == null) or (#user.email == authentication.name)')
    void setCurrentProfile(User user, Profile profile) {
        if (user) {
            user.currentProfileId = profile?.id
            user.save(flush: true)
        }
    }

    Map getProfileDropdown(User currentUser) {
        if (!currentUser) {
            return [:]
        }

        Profile userProfile = currentUser.userProfile
        List<Profile> clubProfiles = currentUser.clubProfiles.findAll { it.active }
        Profile currentProfile = currentUser.currentProfile

        Map result = [
                current: [currentProfile.toString()],
                user: [userProfile.toString()],
                club: clubProfiles.collect { [id: it.id, name: it.toString()] },
        ]

        return result
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    List<Profile> searchProfiles(Profile profile, String query, Integer offset) {
        Profile.createCriteria().list(max: MAX_PROFILES_RETRIEVED, offset: offset) {
            not {'in'('id', profile.user.profiles.id)}
            eq ('active', Boolean.TRUE)
            eq ('type', profile.type)

            if (profile.type == ProfileType.CLUB) {
                eq('period', profile.period)
            }

            if (query) {
                or {
                    query.tokenize(' ').each { String value ->
                        or {
                            ilike('firstName', "%${value}%")
                            ilike('lastName', "%${value}%")

                            if (profile.type == ProfileType.CLUB) {
                                ilike('nickName', "%${value}%")
                            }
                        }
                    }
                }
            }
        }
    }

    Profile createProfile(User user, Profile profile, Image avatar) {
        profile.user = user
        profile.avatar = avatar
        user.addToProfiles(profile)

        if (!isProfileEmailUnique(profile, profile.profileEmail)) {
            profile.validate()
            profile.errors.rejectValue(PROFILE_EMAIL_FIELD, EMAIL_USED_CODE, [profile.profileEmail] as Object[], EMAIL_USED_CODE)
            throw new ValidationException("ProfileEmail is non-unique", profile.errors)
        }

        if (profile.save()) {
            ProfileDto profileDto = objectFactory.getInstance(ProfileDto, profile)
            JSON jsonPayload = profileDto as JSON
            amqpSender.send(MESSAGE_PARTICIPANT_AMPQ_QUEUE, jsonPayload.toString())
        }

        return profile
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    Profile deactivateProfile(Profile profile) {
        profile.active = Boolean.FALSE
        profile.save()
        profile
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    Profile activateProfile(Profile profile) {
        profile.active = Boolean.TRUE
        profile.save()
        profile
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#profile.user.email == authentication.name')
    Profile editProfileField(Profile profile, String fieldName, String fieldValue) {
        if (fieldName == PERIOD_FIELD) {
            throw new IllegalArgumentException("Forbidden operation")
        } else if ((fieldName == PROFILE_EMAIL_FIELD) && !isProfileEmailUnique(profile, profile.profileEmail)) {
            profile.errors.rejectValue(PROFILE_EMAIL_FIELD, EMAIL_USED_CODE, [fieldValue] as Object[], EMAIL_USED_CODE)
            throw new ValidationException("ProfileEmail is non-unique", profile.errors)
        } else {
            profile."${fieldName}" = fieldValue
        }

        if (profile.save()) {
            ProfileDto profileDto = objectFactory.getInstance(ProfileDto, profile)
            JSON jsonPayload = profileDto as JSON
            amqpSender.send(MESSAGE_PARTICIPANT_AMPQ_QUEUE, jsonPayload.toString())
        }

        return profile
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    Profile uploadAvatar(Profile profile, Image avatar) {
        Image currentAvatar = profile.avatar

        if (currentAvatar) {
            imageService.deleteImage(currentAvatar)
        }

        profile.avatar = avatar
        profile.save()
        profile
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    Profile deleteAvatar(Profile profile) {
        imageService.deleteImage(profile.avatar)
        profile.avatar = null
        profile.save()
        profile
    }

    boolean isProfileEmailUnique(Profile profile, String email) {
        if (!email) {
            return true
        }

        User profileUser = profile.user
        User persistentUser = userService.getUserByEmail(email)

        if (persistentUser && (profileUser != persistentUser)) {
            return false
        }

        List<Profile> persistentProfiles = Profile.findAllByProfileEmail(email) as List<Profile>

        return !persistentProfiles.any { it.user != profileUser }
    }
}
