package com.tempvs.user

import grails.test.mixin.TestFor
import grails.util.ClosureToMapPopulator
import grails.validation.ConstraintsEvaluator
import org.grails.validation.ConstraintsEvaluatorFactoryBean
import spock.lang.Specification

@TestFor(UserController)
class UserProfileCommandSpec extends Specification {
    public static final String FIRST_NAME = 'firstName'
    public static final String LAST_NAME = 'lastName'
    public static final String LOCATION = 'location'
    public static final String PROFILE_ID = 'profileId'
    public static final String NUMERIC_PROFILE_ID = '1234'

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

    void "Create empty UserProfileCommand"() {
        expect:
        !new UserProfileCommand().validate()
    }

    void "Create UserProfileCommand with first and last name"() {
        expect:
        new UserProfileCommand(firstName: FIRST_NAME, lastName: LAST_NAME).validate()
    }

    void "Create full UserProfileCommand"() {
        given:
        Map props = [firstName: FIRST_NAME, lastName: LAST_NAME, location: LOCATION, profileId: PROFILE_ID]

        expect:
        new UserProfileCommand(props).validate()
    }

    void "Create UserProfileCommand with numeric profileId"() {
        given:
        Map props = [firstName: FIRST_NAME, lastName: LAST_NAME, profileId: NUMERIC_PROFILE_ID]

        expect:
        !new UserProfileCommand(props).validate()
    }
}
