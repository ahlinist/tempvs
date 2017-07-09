package com.tempvs.user

import com.tempvs.periodization.Period
import grails.test.mixin.TestFor
import grails.util.ClosureToMapPopulator
import grails.validation.ConstraintsEvaluator
import org.grails.validation.ConstraintsEvaluatorFactoryBean
import spock.lang.Specification

@TestFor(UserController)
class ProfileCommandSpec extends Specification {
    private static final String FIRST_NAME = 'firstName'
    private static final String LAST_NAME = 'lastName'
    private static final String NICK_NAME = 'nickName'
    private static final String LOCATION = 'location'
    private static final String PROFILE_ID = 'profileId'
    private static final String NUMERIC_PROFILE_ID = '1234'

    def period = Period.valueOf 'XIX'

    def setupSpec(){
        defineBeans {
            "${ConstraintsEvaluator.BEAN_NAME}"(ConstraintsEvaluatorFactoryBean) {
                def constraintsClosure = {
                    profileId nullable: true, unique: true, matches: /^(?=.*[a-zA-Z])[a-zA-Z0-9.-_]+$/
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
        new UserProfileCommand(firstName: FIRST_NAME, lastName: LAST_NAME).validate()

        and:
        new ClubProfileCommand(firstName: FIRST_NAME, period: period).validate()
    }

    void "Create full UserProfileCommand"() {
        given:
        Map userProfileProps = [firstName: FIRST_NAME, lastName: LAST_NAME, location: LOCATION, profileId: PROFILE_ID]
        Map clubProfileProps = [firstName: FIRST_NAME, lastName: LAST_NAME, lastName: NICK_NAME, location: LOCATION, profileId: PROFILE_ID, period: period]

        expect:
        new UserProfileCommand(userProfileProps).validate()

        and:
        new ClubProfileCommand(clubProfileProps).validate()
    }

    void "Create UserProfileCommand with numeric profileId"() {
        given:
        Map props = [firstName: FIRST_NAME, lastName: LAST_NAME, profileId: NUMERIC_PROFILE_ID]

        expect:
        !new UserProfileCommand(props).validate()

        and:
        !new ClubProfileCommand(props).validate()
    }
}
