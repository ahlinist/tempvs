package club.tempvs.item

import club.tempvs.communication.Comment
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.user.ClubProfile
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class PassportServiceSpec extends Specification implements ServiceUnitTest<PassportService>, DomainUnitTest<Passport> {

    private static final Long LONG_ONE = 1L
    private static final String DESCRIPTION = 'description'
    private static final String FIELD_VALUE = 'fieldValue'

    def item = Mock Item
    def image = Mock Image
    def comment = Mock Comment
    def passport = Mock Passport
    def clubProfile = Mock ClubProfile
    def imageService = Mock ImageService
    def item2Passport = Mock Item2Passport

    void setupSpec() {
        mockDomain Item2Passport
    }

    def setup() {
        service.imageService = imageService

        GroovySpy(Passport, global: true)
        GroovySpy(Item2Passport, global: true)
    }

    def cleanup() {
    }

    void "Test getItem2PassportRelations()"() {
        when:
        def result = service.getItem2PassportRelations(passport)

        then:
        1 * Item2Passport.findAllByPassport(passport) >> [item2Passport]
        0 * _

        and:
        result == [item2Passport]
    }

    void "Test validatePassport()"() {
        when:
        def result = service.validatePassport(passport, clubProfile)

        then:
        1 * passport.setClubProfile(clubProfile)
        1 * clubProfile.addToPassports(passport)
        1 * passport.validate() >> passport
        0 * _

        and:
        result == passport
    }

    void "Test createPassport()"() {
        when:
        def result = service.createPassport(passport, [image])

        then:
        1 * passport.setImages([image])
        1 * passport.save() >> passport
        0 * _

        and:
        result == passport
    }

    void "Test savePassport()"() {
        when:
        def result = service.savePassport(passport)

        then:
        1 * passport.save() >> passport
        0 * _

        and:
        result == passport
    }

    void "Test editPassportField()"() {
        when:
        def result = service.editPassportField(passport, DESCRIPTION, FIELD_VALUE)

        then:
        1 * passport.setDescription(FIELD_VALUE)
        1 * passport.save()
        0 * _

        and:
        result == passport
    }

    void "Test removeItem()"() {
        when:
        service.removeItem(passport, item)

        then:
        1 * Item2Passport.findByItemAndPassport(item, passport) >> item2Passport
        1 * item2Passport.delete()
        0 * _
    }

    void "Test deletePassport()"() {
        when:
        service.deletePassport(passport)

        then:
        1 * Item2Passport.findByPassport(passport) >> item2Passport
        1 * item2Passport.delete()
        1 * passport.delete()
        0 * _
    }

    void "Test addComment()"() {
        when:
        def result = service.addComment(passport, comment)

        then:
        1 * passport.addToComments(comment) >> passport
        1 * passport.save()
        0 * _

        and:
        result == passport
    }

    void "Test deleteComment()"() {
        when:
        def result = service.deleteComment(passport, comment)

        then:
        1 * passport.removeFromComments(comment) >> passport
        1 * passport.save()
        0 * _

        and:
        result == passport
    }

    void "Test editQuantity()"() {
        when:
        def result = service.editQuantity(item2Passport, LONG_ONE)

        then:
        1 * item2Passport.quantity >> 0L
        1 * item2Passport.setQuantity(LONG_ONE)
        1 * item2Passport.save() >> item2Passport
        0 * _

        and:
        result == item2Passport
    }

    void "Test deleteImage()"() {
        when:
        def result = service.deleteImage(passport, image)

        then:
        1 * passport.removeFromImages(image) >> passport
        1 * imageService.deleteImage(image)
        1 * passport.save()
        0 * _

        and:
        result == passport
    }
}
