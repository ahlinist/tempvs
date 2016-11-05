package com.tempvs.services

import com.tempvs.domain.image.Avatar
import com.tempvs.domain.user.User
import com.tempvs.domain.user.UserProfile
import com.tempvs.domain.user.verification.EmailVerification
import grails.transaction.Transactional
import org.codehaus.groovy.runtime.InvokerHelper

@Transactional
class UserService {
    def springSecurityService
    def mailService

    User getUser(String id) {
        try {
            UserProfile.findByCustomId(id)?.user ?: User.get(id as Long)
        } catch (NumberFormatException e) {}
    }

    EmailVerification createEmailVerification(Map properties) {
        String verificationCode = properties.destination.encodeAsMD5() + new Date().time
        EmailVerification emailVerification = new EmailVerification(properties + [verificationCode: verificationCode])

        if (emailVerification.save(flush: true)) {
            mailService.sendMail {
                to properties.destination
                from 'no-reply@tempvs.com'
                subject 'Tempvs'
                body(view: "/user/emailTemplates/${properties.action}", model: emailVerification.properties)
            }
        }

        emailVerification
    }

    User createUser(Map properties) {
        User user = new User(properties)

        user.with{
            password = springSecurityService.encodePassword(user.password)
            userProfile = new UserProfile(properties + [avatar: new Avatar()])
            save(flush: true)
        }

        user
    }

    User updateEmail(Long userId, String newEmail) {
        User user = User.get(userId)

        if (user) {
            user.email = newEmail
            user.save(flush: true)
        }

        user
    }

    User updatePassword(String newPassword) {
        User user = springSecurityService.currentUser
        user.password = springSecurityService.encodePassword(newPassword)
        user.save(flush: true)
        user
    }

    void updateLastActive(){
        User user = springSecurityService.currentUser

        if (user) {
            user.lastActive = new Date()
            user.save(flush: true)
        }
    }

    UserProfile updateProfileEmail(Long userId, String newProfileEmail) {
        UserProfile userProfile = User.get(userId).userProfile

        if (userProfile) {
            userProfile.profileEmail = newProfileEmail
            userProfile.save(flush: true)
        }

        userProfile
    }

    UserProfile updateUserProfile(Map params) {
        UserProfile userProfile = springSecurityService.currentUser?.userProfile
        InvokerHelper.setProperties(userProfile, params)
        userProfile.save(flush: true)
        userProfile
    }
}