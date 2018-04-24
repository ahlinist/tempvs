package club.tempvs.item

import club.tempvs.communication.Comment
import club.tempvs.image.Image
import club.tempvs.image.ImageService
import club.tempvs.user.ClubProfile
import grails.compiler.GrailsCompileStatic
import grails.gorm.transactions.Transactional
import groovy.transform.TypeCheckingMode
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Propagation

/**
 * Handles the operations related to {@link Passport}.
 */
@Transactional
@GrailsCompileStatic
class PassportService {

    private static final String CLUB_PROFILE_FIELD = 'clubProfile'

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

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    Passport validatePassport(Passport passport, ClubProfile clubProfile) {
        passport.clubProfile = clubProfile
        clubProfile.addToPassports(passport)
        passport.validate()
        passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    Passport createPassport(Passport passport, List<Image> images) {
        passport.images = images
        passport.save()
        passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    Passport savePassport(Passport passport) {
        passport.save()
        passport
    }

    @GrailsCompileStatic(TypeCheckingMode.SKIP)
    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    Passport editPassportField(Passport passport, String fieldName, String fieldValue) {
        if (fieldName in [CLUB_PROFILE_FIELD]) {
            throw new AccessDeniedException('Operation not supported.')
        } else {
            passport."${fieldName}" = fieldValue
        }

        passport.save()
        passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    Item2Passport addItem(Passport passport, Item item, Long quantity) {
        Item2Passport item2Passport = new Item2Passport(item: item, passport: passport, quantity: quantity)
        item2Passport.save()
        item2Passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    void removeItem(Passport passport, Item item) {
        Item2Passport.findByItemAndPassport(item, passport)?.delete()
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    void deletePassport(Passport passport, ClubProfile clubProfile) {
        Item2Passport.findByPassport(passport)?.delete()
        clubProfile.removeFromPassports(passport)
        clubProfile.save()
    }

    Passport addComment(Passport passport, Comment comment) {
        passport.addToComments(comment)
        passport.save()
        passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name or (#comment.userProfile != null and #comment.userProfile.user.email == authentication.name) or (#comment.clubProfile != null and #comment.clubProfile.user.email == authentication.name)')
    Passport deleteComment(Passport passport, Comment comment) {
        passport.removeFromComments(comment)
        passport.save()
        passport
    }

    @PreAuthorize('#item2Passport.passport.clubProfile.user.email == authentication.name')
    Item2Passport editQuantity(Item2Passport item2Passport, Long delta) {
        item2Passport.quantity += delta
        item2Passport.save()
        item2Passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    Passport deleteImage(Passport passport, Image image) {
        passport.removeFromImages(image)
        imageService.deleteImage(image)
        passport.save()
        passport
    }
}
