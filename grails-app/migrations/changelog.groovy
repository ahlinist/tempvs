databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1523810664995-1") {
        createSequence(sequenceName: "hibernate_sequence")
    }

    include file: 'tables/user.groovy'
    include file: 'tables/club-profile.groovy'
    include file: 'tables/user-profile.groovy'
    include file: 'tables/comment.groovy'
    include file: 'tables/email-verification.groovy'
    include file: 'tables/following.groovy'
    include file: 'tables/image.groovy'
    include file: 'tables/item.groovy'
    include file: 'tables/item2passport.groovy'
    include file: 'tables/item2source.groovy'
    include file: 'tables/item-comment.groovy'
    include file: 'tables/item-group.groovy'
    include file: 'tables/item-image.groovy'
    include file: 'tables/passport.groovy'
    include file: 'tables/passport-comment.groovy'
    include file: 'tables/passport-image.groovy'
    include file: 'tables/request-map.groovy'
    include file: 'tables/role.groovy'
    include file: 'tables/source.groovy'
    include file: 'tables/source-comment.groovy'
    include file: 'tables/source-image.groovy'
    include file: 'tables/user-role.groovy'
    include file: 'tables/acl-sid.groovy'
    include file: 'tables/acl-object-identity.groovy'
    include file: 'tables/acl-entry.groovy'
    include file: 'tables/acl-class.groovy'
    include file: 'tables/rolerequest.groovy'

    //foreign keys
    include file: 'foreign-keys/club-profile.groovy'
    include file: 'foreign-keys/user-profile.groovy'
    include file: 'foreign-keys/comment.groovy'
    include file: 'foreign-keys/item.groovy'
    include file: 'foreign-keys/item2passport.groovy'
    include file: 'foreign-keys/item2source.groovy'
    include file: 'foreign-keys/item-comment.groovy'
    include file: 'foreign-keys/item-group.groovy'
    include file: 'foreign-keys/item-image.groovy'
    include file: 'foreign-keys/passport.groovy'
    include file: 'foreign-keys/passport-comment.groovy'
    include file: 'foreign-keys/passport-image.groovy'
    include file: 'foreign-keys/source-comment.groovy'
    include file: 'foreign-keys/source-image.groovy'
    include file: 'foreign-keys/user-role.groovy'
    include file: 'foreign-keys/acl-object-identity.groovy'
    include file: 'foreign-keys/acl-entry.groovy'
    include file: 'foreign-keys/rolerequest.groovy'

    //updates
    include file: 'updates/make-role-basepersistent.groovy'
    include file: 'updates/add-rolerequest-constraints.groovy'
    include file: 'updates/make-userrole-basepersistent.groovy'
}
