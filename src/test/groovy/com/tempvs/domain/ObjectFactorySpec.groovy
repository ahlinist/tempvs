package com.tempvs.domain

import com.tempvs.user.User
import spock.lang.Specification

/**
 * {@link com.tempvs.domain.ObjectFactory} unit-test suite.
 */
class ObjectFactorySpec extends Specification {

    def setup() {

    }

    def cleanup() {

    }

    void "Test create()"() {
        when:
        def result = new ObjectFactory().create(User.class)

        then:
        result instanceof User
    }
}
