package club.tempvs.user

import club.tempvs.communication.Following
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.periodization.Period
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.NotTransactional
import grails.gorm.transactions.Transactional
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service for managing {@link UserProfile} and {@link ClubProfile}.
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

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    public <T> T getProfile(Class<T> clazz, id) {
        clazz.findByProfileId(id as String) ?: clazz.get(id)
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    public <T> T getProfileByProfileEmail(Class<T> clazz, String email) {
        clazz.findByProfileEmail(email)
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    List<Profile> getProfilesByFollowings(List<Following> followings) {
        followings.collect { Following following ->
            Class.forName(following.profileClassName).get(following.followingId)
        }
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    List<Profile> getProfilesByFollowers(List<Following> followings) {
        followings.collect { Following following ->
            Class.forName(following.profileClassName).get(following.followerId)
        }
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    List<Profile> searchProfiles(Profile profile, String query, Integer offset) {
        profile.class.createCriteria().list (max: MAX_PROFILES_RETRIEVED, offset: offset) {
            if (profile instanceof UserProfile) {
                ne('id', profile.id)

                if (query) {
                    or {
                        query.tokenize(' ').each { String value ->
                            or {
                                ilike('firstName', "%${value}%")
                                ilike('lastName', "%${value}%")
                            }
                        }
                    }
                }
            } else if (profile instanceof ClubProfile) {
                not {'in'('id', profile.user.clubProfiles.id)}
                eq ('active', Boolean.TRUE)
                eq('period', ((ClubProfile) profile).period)

                if (query) {
                    or {
                        query.tokenize(' ').each { String value ->
                            or {
                                ilike('firstName', "%${value}%")
                                ilike('lastName', "%${value}%")
                                ilike('nickName', "%${value}%")
                            }
                        }
                    }
                }
            }
        }
    }

    void setCurrentProfile(Profile profile) {
        User user = profile.user

        if (user) {
            user.currentProfileClass = profile?.class
            user.currentProfileId = profile?.id
            user.save(flush: true)
        }
    }

    @NotTransactional
    ClubProfile validateClubProfile(ClubProfile clubProfile, User user) {
        clubProfile.user = user
        user.addToClubProfiles(clubProfile)
        clubProfile.validate()

        if (!isProfileEmailUnique(clubProfile, clubProfile.profileEmail)) {
            clubProfile.errors.rejectValue(PROFILE_EMAIL_FIELD, EMAIL_USED_CODE, [clubProfile.profileEmail] as Object[], EMAIL_USED_CODE)
        }

        clubProfile
    }

    @PreAuthorize('#clubProfile.user.email == authentication.name')
    ClubProfile createClubProfile(ClubProfile clubProfile, Image avatar) {
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
        if (email) {
            User user = userService.getUserByEmail(email)
            User profileUser = profile.user

            if (user && profileUser != user) {
                return Boolean.FALSE
            }

            Profile persistedProfile = profile.class.findByProfileEmail email

            if (persistedProfile && profileUser != persistedProfile.user) {
                return Boolean.FALSE
            }
        }

        return Boolean.TRUE
    }
}
