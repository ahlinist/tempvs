package com.tempvs.user

import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import spock.lang.Specification

@TestFor(ProfileController)
class ProfileAvatarCommandSpec extends Specification {

    private static final String AVATAR = 'avatarImage'

    def setup() {

    }

    def cleanup() {

    }

    void "Test empty ProfileAvatarCommand" () {
        expect:
        !new ProfileAvatarCommand().validate()
    }

    void "Test filled ProfileAvatarCommand" () {
        expect:
        new ProfileAvatarCommand(avatarImage: new MockMultipartFile(AVATAR, "1234567" as byte[])).validate()
    }
}
