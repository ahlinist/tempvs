package com.tempvs.communication

import com.tempvs.user.ClubProfile
import com.tempvs.user.ProfileService
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class CommentServiceSpec extends Specification implements ServiceUnitTest<CommentService>, DomainUnitTest<Comment> {

    private static final String TEXT = 'text'

    def clubProfile = Mock ClubProfile
    def profileService = Mock ProfileService

    def setup() {
        service.profileService = profileService
    }

    def cleanup() {
    }

    void "Test createComment()"() {
        when:
        def result = service.createComment(TEXT)

        then:
        1 * profileService.currentProfile >> clubProfile
        1 * clubProfile.asType(ClubProfile) >> clubProfile
        0 * _

        and:
        result instanceof Comment
    }
}
