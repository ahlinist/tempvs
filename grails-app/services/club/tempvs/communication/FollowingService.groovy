package club.tempvs.communication

import club.tempvs.object.ObjectFactory
import club.tempvs.user.Profile
import club.tempvs.user.ProfileService
import club.tempvs.user.ProfileType
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import org.springframework.security.access.prepost.PreAuthorize

/**
 * A service to handle {@link Following}-related operations.
 */
@Transactional
@GrailsCompileStatic
class FollowingService {

    ProfileService profileService
    ObjectFactory objectFactory

    Following getFollowing(Profile follower, Profile followed) {
        if (follower && followed) {
            Following.findByFollowerAndFollowed(follower, followed)
        }
    }

    Integer getNewFollowersCount(Profile profile) {
        if (profile) {
            Following.countByFollowedAndIsNew(profile, Boolean.TRUE)
        }
    }

    @PreAuthorize('#follower.user.email == authentication.name')
    Following createFollowing(Profile follower, Profile followed) {
        Following following = objectFactory.getInstance(Following)
        following.follower = follower
        following.followed = followed
        following.save()
        return following
    }

    @PreAuthorize('#follower.user.email == authentication.name')
    void deleteFollowing(Profile follower, Profile followed) {
        Following following = getFollowing(follower, followed)
        following?.delete()
    }

    List<Following> getFollowers(Profile profile) {
        profile ? Following.findAllByFollowed(profile) : []
    }

    List<Following> getFollowings(Profile profile) {
        profile ? Following.findAllByFollower(profile) : []
    }

    void ageFollowings(List<Following> followings) {
        followings.each { Following following ->
            following.isNew = Boolean.FALSE
            following.save()
        }
    }

    Boolean mayBeFollowed(Profile follower, Profile followed) {
        if (follower && followed) {
            if (follower.type == followed.type) {
                if (follower.id != followed.id) {
                    if (follower.period == followed.period) {
                        if (follower.user != followed.user) {
                            return Boolean.TRUE
                        }
                    }
                }
            }
        }

        return Boolean.FALSE
    }
}
