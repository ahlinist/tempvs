package club.tempvs.user

import grails.compiler.GrailsCompileStatic

import javax.servlet.http.HttpServletRequest

@GrailsCompileStatic
class UserInfoHelper {

    private static final String CURRENT_USER = 'currentUser'
    private static final String CURRENT_PROFILE = 'currentProfile'
    private static final String NEW_FOLLOWINGS_COUNT = 'newFollowingsCount'

    User getCurrentUser(HttpServletRequest request) {
        User user = request.getAttribute(CURRENT_USER) as User
        user.merge()
    }

    Profile getCurrentProfile(HttpServletRequest request) {
        Profile profile = request.getAttribute(CURRENT_PROFILE) as Profile

        if (profile instanceof ClubProfile) {
            ClubProfile clubProfile = profile as ClubProfile
            clubProfile.merge()
        } else if (profile instanceof UserProfile) {
            UserProfile userProfile = profile as UserProfile
            userProfile.merge()
        }
    }

    Integer getNewFollowingsCount(HttpServletRequest request) {
        request.getAttribute(NEW_FOLLOWINGS_COUNT) as Integer
    }

    void setCurrentUser(HttpServletRequest request, User user) {
        request.setAttribute(CURRENT_USER, user)
    }

    void setCurrentProfile(HttpServletRequest request, Profile profile) {
        request.setAttribute(CURRENT_PROFILE, profile)
    }

    void setNewFollowingsCount(HttpServletRequest request, Integer value) {
        request.setAttribute(NEW_FOLLOWINGS_COUNT, value)
    }
}
