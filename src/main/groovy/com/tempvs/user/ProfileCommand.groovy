package com.tempvs.user

import com.tempvs.image.ImageUploadBean
import grails.compiler.GrailsCompileStatic
import grails.validation.Validateable

/**
 * An abstract command to be extended by {@link com.tempvs.user.UserProfile} or {@link com.tempvs.user.ClubProfile}.
 */
@GrailsCompileStatic
abstract class ProfileCommand implements Validateable {

    String firstName
    String lastName
    String location
    String profileId
    ImageUploadBean imageUploadBean
}
