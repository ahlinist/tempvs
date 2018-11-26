databaseChangeLog = {

    changeSet(author: "albvs (generated)", id: "1532526192663-1") {
        createSequence(sequenceName: "hibernate_sequence")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-2") {
        createTable(tableName: "acl_class") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "acl_classPK")
            }

            column(name: "class", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-3") {
        createTable(tableName: "acl_entry") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "acl_entryPK")
            }

            column(name: "sid", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "audit_failure", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "granting", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "acl_object_identity", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "audit_success", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "ace_order", type: "INT") {
                constraints(nullable: "false")
            }

            column(name: "mask", type: "INT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-4") {
        createTable(tableName: "acl_object_identity") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "acl_object_identityPK")
            }

            column(name: "object_id_identity", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "entries_inheriting", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "object_id_class", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "owner_sid", type: "BIGINT")

            column(name: "parent_object", type: "BIGINT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-5") {
        createTable(tableName: "acl_sid") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "acl_sidPK")
            }

            column(name: "sid", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "principal", type: "BOOLEAN") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-6") {
        createTable(tableName: "comment") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "commentPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "profile_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "text", type: "VARCHAR(2000)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-7") {
        createTable(tableName: "email_verification") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "email_verificationPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "instance_id", type: "BIGINT")

            column(name: "action", type: "VARCHAR(12)") {
                constraints(nullable: "false")
            }

            column(name: "email", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "verification_code", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-8") {
        createTable(tableName: "following") {
            column(name: "follower_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "followed_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "is_new", type: "BOOLEAN") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-9") {
        createTable(tableName: "image") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "imagePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "image_info", type: "VARCHAR(255)")

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "object_id", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "collection", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-10") {
        createTable(tableName: "item") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "itemPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "period", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "item_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "item_group_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(2000)")

            column(name: "items_idx", type: "INT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-11") {
        createTable(tableName: "item2passport") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "item2passportPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "item_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "quantity", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "passport_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-12") {
        createTable(tableName: "item2source") {
            column(name: "item_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "source_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-13") {
        createTable(tableName: "item_comment") {
            column(name: "item_comments_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "comment_id", type: "BIGINT")

            column(name: "comments_idx", type: "INT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-14") {
        createTable(tableName: "item_group") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "item_groupPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(2000)")

            column(name: "item_groups_idx", type: "INT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-15") {
        createTable(tableName: "item_image") {
            column(name: "item_images_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "image_id", type: "BIGINT")

            column(name: "images_idx", type: "INT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-16") {
        createTable(tableName: "passport") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "passportPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "profile_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(2000)")

            column(name: "passports_idx", type: "INT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-17") {
        createTable(tableName: "passport_comment") {
            column(name: "passport_comments_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "comment_id", type: "BIGINT")

            column(name: "comments_idx", type: "INT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-18") {
        createTable(tableName: "passport_image") {
            column(name: "passport_images_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "image_id", type: "BIGINT")

            column(name: "images_idx", type: "INT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-19") {
        createTable(tableName: "profile") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "profilePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "profile_email", type: "VARCHAR(35)")

            column(name: "club_name", type: "VARCHAR(35)")

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "nick_name", type: "VARCHAR(35)")

            column(name: "first_name", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "location", type: "VARCHAR(35)")

            column(name: "profile_id", type: "VARCHAR(35)")

            column(name: "active", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "period", type: "VARCHAR(255)")

            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "last_name", type: "VARCHAR(35)")

            column(name: "avatar_id", type: "BIGINT")

            column(name: "profiles_idx", type: "INT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-20") {
        createTable(tableName: "request_map") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "request_mapPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "http_method", type: "VARCHAR(255)")

            column(name: "config_attribute", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "url", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-21") {
        createTable(tableName: "role") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "rolePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "authority", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-22") {
        createTable(tableName: "role_request") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "role_requestPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "role_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-23") {
        createTable(tableName: "source") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "sourcePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "source_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }

            column(name: "period", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "item_type", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(2000)")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-24") {
        createTable(tableName: "source_comment") {
            column(name: "source_comments_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "comment_id", type: "BIGINT")

            column(name: "comments_idx", type: "INT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-25") {
        createTable(tableName: "source_image") {
            column(name: "source_images_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "image_id", type: "BIGINT")

            column(name: "images_idx", type: "INT")
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-26") {
        createTable(tableName: "user") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(primaryKey: "true", primaryKeyName: "userPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "password_expired", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "account_expired", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "account_locked", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "password", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "current_profile_id", type: "BIGINT")

            column(name: "enabled", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "email", type: "VARCHAR(35)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-27") {
        createTable(tableName: "user_role") {
            column(name: "user_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "role_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "last_updated", type: "timestamp") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-28") {
        addPrimaryKey(columnNames: "follower_id, followed_id", constraintName: "followingPK", tableName: "following")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-29") {
        addPrimaryKey(columnNames: "item_id, source_id", constraintName: "item2sourcePK", tableName: "item2source")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-30") {
        addPrimaryKey(columnNames: "user_id, role_id", constraintName: "user_rolePK", tableName: "user_role")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-31") {
        addUniqueConstraint(columnNames: "class", constraintName: "UC_ACL_CLASSCLASS_COL", tableName: "acl_class")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-32") {
        addUniqueConstraint(columnNames: "verification_code", constraintName: "UC_EMAIL_VERIFICATIONVERIFICATION_CODE_COL", tableName: "email_verification")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-33") {
        addUniqueConstraint(columnNames: "profile_id", constraintName: "UC_PROFILEPROFILE_ID_COL", tableName: "profile")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-34") {
        addUniqueConstraint(columnNames: "authority", constraintName: "UC_ROLEAUTHORITY_COL", tableName: "role")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-35") {
        addUniqueConstraint(columnNames: "email", constraintName: "UC_USEREMAIL_COL", tableName: "user")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-36") {
        addUniqueConstraint(columnNames: "sid, principal", constraintName: "UK1781b9a084dff171b580608b3640", tableName: "acl_sid")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-37") {
        addUniqueConstraint(columnNames: "object_id_class, object_id_identity", constraintName: "UK56103a82abb455394f8c97a95587", tableName: "acl_object_identity")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-38") {
        addUniqueConstraint(columnNames: "passport_id, item_id", constraintName: "UK6b2df26b03b2b985bf3c4698f396", tableName: "item2passport")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-39") {
        addUniqueConstraint(columnNames: "user_id, role_id", constraintName: "UKc8fa29d7d73775bc92c5795bc721", tableName: "role_request")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-40") {
        addUniqueConstraint(columnNames: "acl_object_identity, ace_order", constraintName: "UKce200ed06800e5a163c6ab6c0c85", tableName: "acl_entry")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-41") {
        addUniqueConstraint(columnNames: "http_method, url", constraintName: "UKf721bf1f2340334e273dd57aedcb", tableName: "request_map")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-42") {
        addForeignKeyConstraint(baseColumnNames: "passport_id", baseTableName: "item2passport", constraintName: "FK1t10y5yqkaadeb71bj4hrxxhg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "passport")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-43") {
        addForeignKeyConstraint(baseColumnNames: "profile_id", baseTableName: "passport", constraintName: "FK22vmyybh54m73f71xfoaov4js", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-44") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "profile", constraintName: "FK34lmibaadehn191lgf4gl9jk7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-45") {
        addForeignKeyConstraint(baseColumnNames: "parent_object", baseTableName: "acl_object_identity", constraintName: "FK4soxn7uid8qxltqps8kewftx7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "acl_object_identity")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-46") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "item_group", constraintName: "FK67urgr2etxe0afvgjkxxq3lo", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-47") {
        addForeignKeyConstraint(baseColumnNames: "followed_id", baseTableName: "following", constraintName: "FK7aud1pqpivq0ecijth4gegrc1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-48") {
        addForeignKeyConstraint(baseColumnNames: "item_id", baseTableName: "item2passport", constraintName: "FK8v1ebuj2ub3jk9j3pouey0h4g", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "item")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-49") {
        addForeignKeyConstraint(baseColumnNames: "sid", baseTableName: "acl_entry", constraintName: "FK9r4mj8ewa904g3wivff0tb5b0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "acl_sid")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-50") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "role_request", constraintName: "FK9ve684xlarq9w37wj1fxgqtn7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-51") {
        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "user_role", constraintName: "FKa68196081fvovjhkek5m97n3y", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-52") {
        addForeignKeyConstraint(baseColumnNames: "profile_id", baseTableName: "comment", constraintName: "FKa926jdw9ofp44fheygercoe4n", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-53") {
        addForeignKeyConstraint(baseColumnNames: "object_id_class", baseTableName: "acl_object_identity", constraintName: "FKc06nv93ck19el45a3g1p0e58w", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "acl_class")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-54") {
        addForeignKeyConstraint(baseColumnNames: "image_id", baseTableName: "source_image", constraintName: "FKeu8sa8tipk8chkldwqq0lmtln", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "image")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-55") {
        addForeignKeyConstraint(baseColumnNames: "image_id", baseTableName: "item_image", constraintName: "FKffekuuetvxc58mlha2e9i3tj5", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "image")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-56") {
        addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_role", constraintName: "FKfgsgxvihks805qcq8sq26ab7c", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-57") {
        addForeignKeyConstraint(baseColumnNames: "comment_id", baseTableName: "item_comment", constraintName: "FKfqcrpo3y4l5tsko8698ke0r4e", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "comment")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-58") {
        addForeignKeyConstraint(baseColumnNames: "owner_sid", baseTableName: "acl_object_identity", constraintName: "FKikrbtok3aqlrp9wbq6slh9mcw", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "acl_sid")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-59") {
        addForeignKeyConstraint(baseColumnNames: "follower_id", baseTableName: "following", constraintName: "FKktejkg0u0led7oux4kg7fgxjo", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "profile")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-60") {
        addForeignKeyConstraint(baseColumnNames: "acl_object_identity", baseTableName: "acl_entry", constraintName: "FKl39t1oqikardwghegxe0wdcpt", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "acl_object_identity")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-61") {
        addForeignKeyConstraint(baseColumnNames: "comment_id", baseTableName: "source_comment", constraintName: "FKlef4yys4enjfcnngx65ldjwh", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "comment")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-62") {
        addForeignKeyConstraint(baseColumnNames: "avatar_id", baseTableName: "profile", constraintName: "FKpmjjoqxn1ya27uwi5gar8ickc", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "image")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-63") {
        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "role_request", constraintName: "FKq3pmcnl1x9ymtr4ypdboi0lv3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-64") {
        addForeignKeyConstraint(baseColumnNames: "source_id", baseTableName: "item2source", constraintName: "FKqu63sppr2062vav0ky6rua1m", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "source")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-65") {
        addForeignKeyConstraint(baseColumnNames: "item_group_id", baseTableName: "item", constraintName: "FKr4fbv7293k0b5v1qjk5lm6md", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "item_group")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-66") {
        addForeignKeyConstraint(baseColumnNames: "image_id", baseTableName: "passport_image", constraintName: "FKsiok32hg03dnuropo3meotkwm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "image")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-67") {
        addForeignKeyConstraint(baseColumnNames: "item_id", baseTableName: "item2source", constraintName: "FKtp4u7one6eah0pp4u5ul0ami6", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "item")
    }

    changeSet(author: "albvs (generated)", id: "1532526192663-68") {
        addForeignKeyConstraint(baseColumnNames: "comment_id", baseTableName: "passport_comment", constraintName: "FKyyu8k8oa59y5eg4pdonqyu2h", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "comment")
    }
    include file: 'updates/user-add-time-zone.groovy'
}
