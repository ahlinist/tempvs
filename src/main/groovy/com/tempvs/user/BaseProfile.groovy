package com.tempvs.user

import com.tempvs.domain.BasePersistent
import groovy.transform.CompileStatic

@CompileStatic
abstract class BaseProfile extends BasePersistent {

    String firstName
    String lastName
    String profileEmail
    String location
    String profileId

    String getIdentifier() {
        profileId ?: id as String
    }
}
