package com.tempvs.communication

import com.tempvs.periodization.Period
import com.tempvs.user.ClubProfile
import com.tempvs.user.Profile
import com.tempvs.user.ProfileService
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
    private static final String PROFILE_CLASS_FIELD = 'profileClass'
    private static final String PERIOD_MISMATCH = 'period.mismatch.message'
    private static final String PROFILE_CLASSES_MISMATCH = 'profile.classes.mismatch.message'

    ProfileService profileService

    Following getFollowing(Profile followerProfile, Profile followingProfile) {
        if (followerProfile && followingProfile) {
            if (followerProfile.class == followingProfile.class) {
                Following.findByProfileClassNameAndFollowerIdAndFollowingId(followerProfile.class.name, followerProfile.id, followingProfile.id)
            }
        }
    }

    @PreAuthorize('#followerProfile.user.email == authentication.name')
    Following createFollowing(Profile followerProfile, Profile followingProfile) {
        Following following = new Following()

        if (followerProfile?.class != followingProfile?.class) {
            following.errors.rejectValue(PROFILE_CLASS_FIELD, PROFILE_CLASSES_MISMATCH, [] as Object[], PROFILE_CLASSES_MISMATCH)
        }

        if (followerProfile instanceof ClubProfile) {
            Period period = followerProfile.period

            if (period != ((ClubProfile) followingProfile)?.period) {
                following.errors.rejectValue(PERIOD_FIELD, PERIOD_MISMATCH, [] as Object[], PERIOD_MISMATCH)
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

    List<Profile> getFollowerProfiles(Profile profile) {
        Class clazz = profile?.class
        List<Following> followings = Following.findAllByProfileClassNameAndFollowingId(clazz?.name, profile.id)

        followings.collect { Following following ->
            profileService.getProfile(clazz, following.followerId)
        }
    }

    List<Profile> getFollowingProfiles(Profile profile) {
        Class clazz = profile?.class
        List<Following> followings = Following.findAllByProfileClassNameAndFollowerId(clazz?.name, profile.id)

        followings.collect { Following following ->
            profileService.getProfile(clazz, following.followingId)
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