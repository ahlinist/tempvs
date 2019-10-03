package club.tempvs.item

import club.tempvs.communication.Comment
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.user.Profile
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import groovy.transform.TypeCheckingMode

/**
 * Handles the operations related to {@link Passport}.
 */
@Transactional
@GrailsCompileStatic
class PassportService {

    private static final String PROFILE_FIELD = 'profile'

    ImageService imageService

    Passport getPassport(Long id) {
        Passport.get id
    }

    Item2Passport getItem2Passport(Long id) {
        Item2Passport.get id
    }

    List<Item2Passport> getItem2PassportRelations(Passport passport) {
        Item2Passport.findAllByPassport(passport)
    }

    Passport createPassport(Passport passport, Profile profile, List<Image> images) {
        passport.images = images
        passport.profile = profile
        profile.addToPassports(passport)
        passport.save()
        passport
    }

    Passport savePassport(Passport passport) {
        passport.save()
        passport
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    Passport editPassportField(Passport passport, String fieldName, String fieldValue) {
        if (fieldName in [PROFILE_FIELD]) {

        } else {
            passport."${fieldName}" = fieldValue
        }

        passport.save()
        passport
    }

    Item2Passport addItem(Passport passport, Item item, Long quantity) {
        Item2Passport item2Passport = new Item2Passport(item: item, passport: passport, quantity: quantity)
        item2Passport.save()
        item2Passport
    }

    void removeItem(Passport passport, Item item) {
        Item2Passport.findByItemAndPassport(item, passport)?.delete()
    }

    void deletePassport(Passport passport, Profile profile) {
        Item2Passport.findByPassport(passport)?.delete()
        profile.removeFromPassports(passport)
        profile.save()
    }

    Passport addComment(Passport passport, Comment comment) {
        passport.addToComments(comment)
        passport.save()
        passport
    }

    Passport deleteComment(Passport passport, Comment comment) {
        passport.removeFromComments(comment)
        passport.save()
        passport
    }

    Item2Passport editQuantity(Item2Passport item2Passport, Long delta) {
        item2Passport.quantity += delta
        item2Passport.save()
        item2Passport
    }

    Passport deleteImage(Passport passport, Image image) {
        passport.removeFromImages(image)
        imageService.deleteImage(image)
        passport.save()
        passport
    }
}
