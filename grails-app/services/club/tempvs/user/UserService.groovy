package club.tempvs.user

import club.tempvs.ampq.AmqpSender
import club.tempvs.object.ObjectFactory
import club.tempvs.profile.ProfileDto
import grails.compiler.GrailsCompileStatic
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.SpringSecurityUtils
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Service that manages operations with {@link User} entitites.
 */
@Transactional
@GrailsCompileStatic
class UserService {

    private static final String MESSAGE_PARTICIPANT_AMPQ_QUEUE = 'message.participant'

    ProfileService profileService
    VerifyService verifyService
    SpringSecurityService springSecurityService
    AmqpSender amqpSender
    ObjectFactory objectFactory

    User getUser(Long id) {
        User.get id
    }

    boolean isLoggedIn() {
        springSecurityService.loggedIn
    }

    User getCurrentUser() {
        springSecurityService.currentUser as User
    }

    Long getCurrentUserId() {
        springSecurityService.currentUserId as Long
    }

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    User register(User user) {
        if (user.save()) {
            EmailVerification emailVerification = verifyService.getRegistrationVerificationByUser(user)
            emailVerification.delete()

            //TODO: should be moved further to user profile creation page
            Profile profile = user.userProfile
            ProfileDto profileDto = objectFactory.getInstance(ProfileDto, [id: profile.id, name: profile.toString()])
            JSON jsonPayload = profileDto as JSON
            amqpSender.send(MESSAGE_PARTICIPANT_AMPQ_QUEUE, jsonPayload.toString())
        }

        return user
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#user.email == authentication.name')
    User editUserField(User user, String fieldName, Object fieldValue) {
        user."${fieldName}" = fieldValue
        user.save()
        user
    }

    boolean isEmailUnique(String email, Long id = null) {
        List<Profile> profiles = profileService.getProfilesByProfileEmail(email)

        if (id) {
            User persistentUser = getUser(id)

            return !profiles?.any {
                User profileUser = it.user
                (profileUser.id != id) && (profileUser != persistentUser)
            }
        } else {
            return !profiles
        }
    }

    boolean ifAnyRoleGranted(String roles) {
        SpringSecurityUtils.ifAnyGranted(roles)
    }
}
