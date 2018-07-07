package club.tempvs.communication

import club.tempvs.object.ObjectFactory
import club.tempvs.periodization.Period
import club.tempvs.user.ClubProfile
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import org.springframework.security.access.prepost.PreAuthorize

/**
 * A service to handle {@link Following}-related operations.
 */
@Transactional
@GrailsCompileStatic
class FollowingService {

    private static final String PERIOD_FIELD = 'period'
    private static final String PROFILE_CLASS_NAME_FIELD = 'profileClassName'
    private static final String FOLLOWING_ID_FIELD = 'followingId'
    private static final String PERIOD_MISMATCH = 'period.mismatch.message'
    private static final String USER_MISMATCH = 'following.user.mismatch.message'
    private static final String PROFILE_CLASSES_MISMATCH = 'profile.classes.mismatch.message'

    ProfileService profileService
    ObjectFactory objectFactory

    Following getFollowing(Profile followerProfile, Profile followingProfile) {
        if (followerProfile && followingProfile) {
            if (followerProfile.class == followingProfile.class) {
                Following.findByProfileClassNameAndFollowerIdAndFollowingId(followerProfile.class.name, followerProfile.id, followingProfile.id)
            }
        }
    }

    Integer getNewFollowersCount(Profile profile) {
        if (profile) {
            String profileClassName = profile.class.name
            Long profileId = profile.id
            Following.countByProfileClassNameAndFollowingIdAndIsNew(profileClassName, profileId, Boolean.TRUE)
        }
    }

    @PreAuthorize('#followerProfile.user.email == authentication.name')
    Following createFollowing(Profile followerProfile, Profile followingProfile) {
        Following following = objectFactory.getInstance(Following)

        if (followerProfile?.class != followingProfile?.class) {
            following.errors.rejectValue(PROFILE_CLASS_NAME_FIELD, PROFILE_CLASSES_MISMATCH, PROFILE_CLASSES_MISMATCH)
        }

        if (followerProfile instanceof ClubProfile) {
            Period period = followerProfile.period

            if (period != ((ClubProfile) followingProfile)?.period) {
                following.errors.rejectValue(PERIOD_FIELD, PERIOD_MISMATCH, PERIOD_MISMATCH)
            }

            if (followerProfile.user == followingProfile.user) {
                following.errors.rejectValue(FOLLOWING_ID_FIELD, USER_MISMATCH, USER_MISMATCH)
            }

            following.period = period
        }

        following.followerId = followerProfile?.id
        following.followingId = followingProfile?.id
        following.profileClassName = followerProfile?.class?.name
        following.save()
        following
    }

    @PreAuthorize('#followerProfile.user.email == authentication.name')
    void deleteFollowing(Profile followerProfile, Profile followingProfile) {
        Following following = getFollowing(followerProfile, followingProfile)
        following?.delete()
    }

    List<Following> getFollowers(Profile profile) {
        profile ? Following.findAllByProfileClassNameAndFollowingId(profile.class.name, profile.id) : []
    }

    List<Following> getFollowings(Profile profile) {
        profile ? Following.findAllByProfileClassNameAndFollowerId(profile.class.name, profile.id) : []
    }

    void ageFollowings(List<Following> followings) {
        followings.each { Following following ->
            following.isNew = Boolean.FALSE
            following.save()
        }
    }

    Boolean mayBeFollowed(Profile followerProfile, Profile followingProfile) {
        if (followerProfile && followingProfile) {
            if (followerProfile.class == followingProfile.class) {
                if (followerProfile.id != followingProfile.id) {
                    if (followerProfile instanceof ClubProfile) {
                        if (((ClubProfile) followerProfile).period == ((ClubProfile) followingProfile).period) {
                            return Boolean.TRUE
                        }
                    } else {
                        return Boolean.TRUE
                    }
                }
            }
        }

        return Boolean.FALSE
    }
}
