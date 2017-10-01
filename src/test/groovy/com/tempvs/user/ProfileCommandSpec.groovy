package com.tempvs.user

import com.tempvs.image.ImageUploadBean
import com.tempvs.periodization.Period
import grails.test.mixin.TestFor
import grails.util.ClosureToMapPopulator
import grails.validation.ConstraintsEvaluator
import org.grails.validation.ConstraintsEvaluatorFactoryBean
import spock.lang.Specification

/**
 * Unit-test suite for {@link com.tempvs.user.UserProfileCommand} and {@link com.tempvs.user.ClubProfile}.
 */
@TestFor(UserController)
class ProfileCommandSpec extends Specification {
    private static final String FIRST_NAME = 'firstName'
    private static final String LAST_NAME = 'lastName'
    private static final String NICK_NAME = 'nickName'
    private static final String LOCATION = 'location'
    private static final String PROFILE_ID = 'profileId'
    private static final String NUMERIC_PROFILE_ID = '1234'

    def period = Period.valueOf 'XIX'
    def imageUploadBean = Mock(ImageUploadBean)

    def setupSpec(){
        defineBeans {
            "${ConstraintsEvaluator.BEAN_NAME}"(ConstraintsEvaluatorFactoryBean) {
                def constraintsClosure = {
                    profileId nullable: true, matches: /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/
                }

                defaultConstraints = new ClosureToMapPopulator().populate(constraintsClosure)
            }
        }
    }

    def setup() {

    }

    def cleanup() {

    }

    void "Test invalid UserProfileCommand creation"() {
        expect:
        !new UserProfileCommand().validate()

        and:
        !new ClubProfileCommand().validate()
        !new ClubProfileCommand(firstName: FIRST_NAME).validate()

    }

    void "Test minimal valid profile commands creation"() {
        expect:
        new UserProfileCommand(firstName: FIRST_NAME, lastName: LAST_NAME, imageUploadBean: imageUploadBean).validate()

        and:
        new ClubProfileCommand(firstName: FIRST_NAME, period: period, imageUploadBean: imageUploadBean).validate()
    }

    void "Create full UserProfileCommand"() {
        given:
        Map userProfileProps = [firstName: FIRST_NAME, lastName: LAST_NAME, location: LOCATION, profileId: PROFILE_ID, imageUploadBean: imageUploadBean]
        Map clubProfileProps = [firstName: FIRST_NAME, lastName: LAST_NAME, lastName: NICK_NAME, location: LOCATION, profileId: PROFILE_ID, period: period, imageUploadBean: imageUploadBean]

        expect:
        new UserProfileCommand(userProfileProps).validate()

        and:
        new ClubProfileCommand(clubProfileProps).validate()
    }

    void "Create UserProfileCommand with numeric profileId"() {
        given:
        Map props = [firstName: FIRST_NAME, lastName: LAST_NAME, profileId: NUMERIC_PROFILE_ID, imageUploadBean: imageUploadBean]

        expect:
        !new UserProfileCommand(props).validate()

        and:
        !new ClubProfileCommand(props).validate()
    }
}
