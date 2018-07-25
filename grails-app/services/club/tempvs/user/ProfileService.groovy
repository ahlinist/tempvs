package club.tempvs.user

import club.tempvs.communication.Following
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
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

    UserService userService
    ImageService imageService
    LinkGenerator grailsLinkGenerator


    Profile getProfile(id) {
        Profile.findByProfileId(id as String) ?: getProfileById(id as Long)
    }

    Profile getProfileById(Long id) {
        Profile.get(id)
    }

    List<Profile> getProfilesByProfileEmail(String email) {
        Profile.findAllByProfileEmail(email)
    }

    List<Profile> getProfilesByFollowings(List<Following> followings) {
        followings.collect { Following following ->
            Profile.get(following.followed)
        }
    }

    List<Profile> getProfilesByFollowers(List<Following> followings) {
        followings.collect { Following following ->
            Profile.get(following.follower)
        }
    }

    Profile getCurrentProfile() {
        userService.currentUser?.currentProfile
    }

    @PreAuthorize('(#profile == null) or (#profile.user.email == authentication.name)')
    void setCurrentProfile(Profile profile) {
        User user = userService.currentUser

        if (user) {
            user.currentProfileId = profile?.id
            user.save(flush: true)
        }
    }

    Map getProfileDropdown() {
        User currentUser = userService.currentUser

        if (!currentUser) {
            return [:]
        }

        List<Profile> profiles = currentUser.profiles

        Profile userProfile = profiles.find { it.type == ProfileType.USER}
        List<Profile> clubProfiles = profiles.findAll { it.type == ProfileType.CLUB}

        Profile currentProfile = profiles.find {
            if (currentUser.currentProfile) {
                it.id == currentUser.currentProfileId
            } else {
                it.id == currentUser.currentProfile.id
            }
        }

        Map result = [
                current: [currentProfile.toString()],
                user: [userProfile.toString()],
                club: clubProfiles.collect { [id:it.id, name: it.toString()] },
        ]

        return result
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    List<Profile> searchProfiles(Profile profile, String query, Integer offset) {
        Profile.createCriteria().list(max: MAX_PROFILES_RETRIEVED, offset: offset) {
            not {'in'('id', profile.user.profiles.id)}
            eq ('active', Boolean.TRUE)

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

    Profile createClubProfile(Profile profile, Image avatar) {
        User user = userService.currentUser
        profile.user = user
        profile.avatar = avatar
        profile.type = ProfileType.CLUB
        user.addToProfiles(profile)

        if (!isProfileEmailUnique(profile, profile.profileEmail)) {
            profile.validate()
            profile.errors.rejectValue(PROFILE_EMAIL_FIELD, EMAIL_USED_CODE, [profile.profileEmail] as Object[], EMAIL_USED_CODE)
            throw new ValidationException("ProfileEmail is non-unique", profile.errors)
        }

        profile.save()
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

        profile.save()
        profile
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

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    Boolean isProfileEmailUnique(Profile profile, String email) {
        if (!email) {
            return Boolean.TRUE
        }

        User profileUser = profile.user
        User persistentUser = userService.getUserByEmail(email)

        if (persistentUser && (profileUser != persistentUser)) {
            println "returning false"
            return Boolean.FALSE
        }

        List<Profile> persistentProfiles = Profile.findAllByProfileEmail(email)

        return !persistentProfiles.any { it.user != profileUser }
    }
}
