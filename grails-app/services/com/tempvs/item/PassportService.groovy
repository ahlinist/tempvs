package com.tempvs.item

import com.tempvs.user.ClubProfile
import grails.gorm.transactions.Transactional
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
    Passport addItem(Passport passport, Item item) {
        passport.addToItems(item)
        passport.save()
        passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    Passport removeItem(Passport passport, Item item) {
        passport.removeFromItems(item)
        passport.save()
        passport
    }

    @PreAuthorize('#passport.clubProfile.user.email == authentication.name')
    void deletePassport(Passport passport) {
        passport.delete()
    }
}
