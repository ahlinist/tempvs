package com.tempvs.services

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

    User getUserByEmail(String email) {
        User.findByEmail(email)
    }

    User getUserByProfileEmail(String profileEmail) {
        UserProfile.findByProfileEmail(profileEmail)?.user
    }

    EmailVerification getVerification(String id) {
        EmailVerification.findByVerificationCode(id)
    }

    EmailVerification createEmailVerification(Map properties) {
        String verificationCode = properties.email + new Date().time
        EmailVerification emailVerification = new EmailVerification(properties + [verificationCode: verificationCode.encodeAsMD5()])

        if (emailVerification.save(flush: true)) {
            mailService.sendMail {
                to properties.email
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
            userProfile = new UserProfile(properties)
            save(flush: true)
        }

        user
    }

    User updateEmail(Long userId, String email) {
        User user = User.get(userId)

        if (user) {
            user.email = email
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

    UserProfile updateProfileEmail(Long userId, String profileEmail) {
        UserProfile userProfile = User.get(userId).userProfile

        if (userProfile) {
            userProfile.profileEmail = profileEmail
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
