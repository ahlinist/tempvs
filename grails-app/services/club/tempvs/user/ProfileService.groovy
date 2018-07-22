package club.tempvs.user

import club.tempvs.communication.Following
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import grails.web.mapping.LinkGenerator
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation

/**
 * Service for {@link Profile} managing.
 */
@Transactional
@GrailsCompileStatic
class ProfileService {

    private static String PERIOD_FIELD = 'period'
    private static Integer MAX_PROFILES_RETRIEVED = 10
    private static String PROFILE_EMAIL_FIELD = 'profileEmail'
    private static String EMAIL_USED_CODE = 'userProfile.profileEmail.used.error'

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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    Profile validateClubProfile(Profile clubProfile, User user) {

        throw new RuntimeException("implement rest-call-driven image deletion in case if failing validation")

        clubProfile.user = user
        user.addToProfiles(clubProfile)
        clubProfile.validate()

        if (!isProfileEmailUnique(clubProfile, clubProfile.profileEmail)) {
            clubProfile.errors.rejectValue(PROFILE_EMAIL_FIELD, EMAIL_USED_CODE, [clubProfile.profileEmail] as Object[], EMAIL_USED_CODE)
        }

        clubProfile
    }

    @PreAuthorize('#profile.user.email == authentication.name')
    Profile createClubProfile(Profile clubProfile, Image avatar) {
        clubProfile.avatar = avatar
        clubProfile.save()
        clubProfile
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
    Profile uploadAvatar(Profile profile, Image avatar) {
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
            return Boolean.FALSE
        }

        User profileUser = profile.user
        User persistentUser = userService.getUserByEmail(email)

        if (persistentUser && profileUser != persistentUser) {
            return Boolean.FALSE
        }

        List<Profile> persistentProfiles = Profile.findAllByProfileEmail(email)

        return !persistentProfiles.any { it.user != profileUser }
    }
}
