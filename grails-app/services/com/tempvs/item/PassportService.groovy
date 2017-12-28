package com.tempvs.item

import com.tempvs.user.ClubProfile
import grails.transaction.Transactional
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.access.prepost.PreAuthorize

/**
 * Handles the operations related to {@link com.tempvs.item.Passport}.
 */
@Transactional
class PassportService {

    private static final String CLUB_PROFILE = 'clubProfile'

    Passport getPassport(id) {
        Passport.get id
    }

    List<Passport> getPassportsByProfile(ClubProfile clubProfile) {
        Passport.findAllByClubProfile(clubProfile)
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    Passport createPassport(Passport passport) {
        passport.save()
        passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    Passport editPassportField(Passport passport, String fieldName, String fieldValue) {
        if (fieldName in [CLUB_PROFILE]) {
            throw new AccessDeniedException('Operation not supported.')
        } else {
            passport."${fieldName}" = fieldValue
        }

        passport.save()
        passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    Item2Passport addItem(Passport passport, Item item, Long quantity) {
        Item2Passport item2Passport = new Item2Passport(passport: passport, item: item, quantity: quantity)
        item2Passport.save()
        item2Passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    void removeItem(Passport passport, Item item) {
        Item2Passport item2Passport = Item2Passport.findByPassportAndItem(passport, item)
        item2Passport.delete()
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    void deletePassport(Passport passport) {
        Item2Passport.findAllByPassport(passport)*.delete()
        passport.delete()
    }
}
