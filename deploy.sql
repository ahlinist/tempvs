--
-- PostgreSQL database dump
--

-- Dumped from database version 10.2 (Ubuntu 10.2-1.pgdg14.04+1)
-- Dumped by pg_dump version 10.2 (Ubuntu 10.2-1.pgdg16.04+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: tempvs; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA tempvs;


--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: acl_class; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE acl_class (
    id bigint NOT NULL,
    class character varying(255) NOT NULL
);


--
-- Name: acl_entry; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE acl_entry (
    id bigint NOT NULL,
    sid bigint NOT NULL,
    audit_failure boolean NOT NULL,
    granting boolean NOT NULL,
    acl_object_identity bigint NOT NULL,
    audit_success boolean NOT NULL,
    ace_order integer NOT NULL,
    mask integer NOT NULL
);


--
-- Name: acl_object_identity; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE acl_object_identity (
    id bigint NOT NULL,
    object_id_identity bigint NOT NULL,
    entries_inheriting boolean NOT NULL,
    object_id_class bigint NOT NULL,
    owner_sid bigint,
    parent_object bigint
);


--
-- Name: acl_sid; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE acl_sid (
    id bigint NOT NULL,
    sid character varying(255) NOT NULL,
    principal boolean NOT NULL
);


--
-- Name: club_profile; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE club_profile (
    id bigint NOT NULL,
    version bigint NOT NULL,
    profile_email character varying(35),
    club_name character varying(35),
    date_created timestamp without time zone NOT NULL,
    nick_name character varying(35),
    first_name character varying(35) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    location character varying(35),
    profile_id character varying(35),
    active boolean NOT NULL,
    period character varying(255) NOT NULL,
    user_id bigint NOT NULL,
    last_name character varying(35),
    avatar_id bigint
);


--
-- Name: comment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE comment (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    text character varying(2000) NOT NULL,
    club_profile_id bigint,
    user_profile_id bigint
);


--
-- Name: email_verification; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE email_verification (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    instance_id bigint,
    action character varying(12) NOT NULL,
    email character varying(35) NOT NULL,
    verification_code character varying(255) NOT NULL
);


--
-- Name: following; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE following (
    profile_class_name character varying(255) NOT NULL,
    follower_id bigint NOT NULL,
    following_id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    is_new boolean NOT NULL,
    period character varying(255)
);


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: image; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE image (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    image_info character varying(255),
    last_updated timestamp without time zone NOT NULL,
    object_id character varying(255) NOT NULL,
    collection character varying(255) NOT NULL
);


--
-- Name: item; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE item (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name character varying(35) NOT NULL,
    period character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    item_group_id bigint NOT NULL,
    description character varying(2000)
);


--
-- Name: item2passport; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE item2passport (
    item_id bigint NOT NULL,
    passport_id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    quantity bigint NOT NULL
);


--
-- Name: item2source; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE item2source (
    item_id bigint NOT NULL,
    source_id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL
);


--
-- Name: item_comment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE item_comment (
    item_comments_id bigint NOT NULL,
    comment_id bigint,
    comments_idx integer
);


--
-- Name: item_group; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE item_group (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name character varying(35) NOT NULL,
    user_id bigint NOT NULL,
    description character varying(2000)
);


--
-- Name: item_image; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE item_image (
    item_images_id bigint NOT NULL,
    image_id bigint
);


--
-- Name: passport; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE passport (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    club_profile_id bigint NOT NULL,
    name character varying(35) NOT NULL,
    description character varying(2000)
);


--
-- Name: passport_comment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE passport_comment (
    passport_comments_id bigint NOT NULL,
    comment_id bigint,
    comments_idx integer
);


--
-- Name: requestmap; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE requestmap (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    config_attribute character varying(255) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    http_method character varying(255),
    url character varying(255) NOT NULL
);


--
-- Name: role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE role (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    authority character varying(255) NOT NULL
);


--
-- Name: source; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE source (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    name character varying(35) NOT NULL,
    period character varying(255) NOT NULL,
    type character varying(255) NOT NULL,
    description character varying(2000)
);


--
-- Name: source_comment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE source_comment (
    source_comments_id bigint NOT NULL,
    comment_id bigint,
    comments_idx integer
);


--
-- Name: source_image; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE source_image (
    source_images_id bigint NOT NULL,
    image_id bigint
);


--
-- Name: user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "user" (
    id bigint NOT NULL,
    version bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    password_expired boolean NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    account_expired boolean NOT NULL,
    current_profile_class character varying(255),
    account_locked boolean NOT NULL,
    password character varying(35) NOT NULL,
    current_profile_id bigint,
    enabled boolean NOT NULL,
    last_active timestamp without time zone NOT NULL,
    email character varying(35) NOT NULL
);


--
-- Name: user_profile; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_profile (
    id bigint NOT NULL,
    version bigint NOT NULL,
    profile_email character varying(35),
    date_created timestamp without time zone NOT NULL,
    first_name character varying(35) NOT NULL,
    last_updated timestamp without time zone NOT NULL,
    location character varying(35),
    profile_id character varying(35),
    active boolean NOT NULL,
    user_id bigint NOT NULL,
    last_name character varying(35) NOT NULL,
    avatar_id bigint
);


--
-- Name: user_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    date_created timestamp without time zone NOT NULL,
    last_updated timestamp without time zone NOT NULL
);


--
-- Name: acl_class acl_class_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_class
    ADD CONSTRAINT acl_class_pkey PRIMARY KEY (id);


--
-- Name: acl_entry acl_entry_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT acl_entry_pkey PRIMARY KEY (id);


--
-- Name: acl_object_identity acl_object_identity_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT acl_object_identity_pkey PRIMARY KEY (id);


--
-- Name: acl_sid acl_sid_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_sid
    ADD CONSTRAINT acl_sid_pkey PRIMARY KEY (id);


--
-- Name: club_profile club_profile_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY club_profile
    ADD CONSTRAINT club_profile_pkey PRIMARY KEY (id);


--
-- Name: comment comment_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY comment
    ADD CONSTRAINT comment_pkey PRIMARY KEY (id);


--
-- Name: email_verification email_verification_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY email_verification
    ADD CONSTRAINT email_verification_pkey PRIMARY KEY (id);


--
-- Name: following following_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY following
    ADD CONSTRAINT following_pkey PRIMARY KEY (profile_class_name, follower_id, following_id);


--
-- Name: image image_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY image
    ADD CONSTRAINT image_pkey PRIMARY KEY (id);


--
-- Name: item2passport item2passport_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item2passport
    ADD CONSTRAINT item2passport_pkey PRIMARY KEY (item_id, passport_id);


--
-- Name: item2source item2source_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item2source
    ADD CONSTRAINT item2source_pkey PRIMARY KEY (item_id, source_id);


--
-- Name: item_group item_group_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item_group
    ADD CONSTRAINT item_group_pkey PRIMARY KEY (id);


--
-- Name: item item_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);


--
-- Name: passport passport_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY passport
    ADD CONSTRAINT passport_pkey PRIMARY KEY (id);


--
-- Name: requestmap requestmap_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY requestmap
    ADD CONSTRAINT requestmap_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: source source_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY source
    ADD CONSTRAINT source_pkey PRIMARY KEY (id);


--
-- Name: acl_sid uk1781b9a084dff171b580608b3640; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_sid
    ADD CONSTRAINT uk1781b9a084dff171b580608b3640 UNIQUE (sid, principal);


--
-- Name: requestmap uk3d11b687954e6645e90db4e23cb4; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY requestmap
    ADD CONSTRAINT uk3d11b687954e6645e90db4e23cb4 UNIQUE (http_method, url);


--
-- Name: acl_object_identity uk56103a82abb455394f8c97a95587; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT uk56103a82abb455394f8c97a95587 UNIQUE (object_id_class, object_id_identity);


--
-- Name: user_profile uk_dnnx1gqmln4no0py3hn5fy334; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile
    ADD CONSTRAINT uk_dnnx1gqmln4no0py3hn5fy334 UNIQUE (profile_id);


--
-- Name: email_verification uk_e6eqaykcfaldxspdi41ul34bb; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY email_verification
    ADD CONSTRAINT uk_e6eqaykcfaldxspdi41ul34bb UNIQUE (verification_code);


--
-- Name: user_profile uk_fq6rfxjvf0fycjvfchovgs371; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile
    ADD CONSTRAINT uk_fq6rfxjvf0fycjvfchovgs371 UNIQUE (profile_email);


--
-- Name: role uk_irsamgnera6angm0prq1kemt2; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY role
    ADD CONSTRAINT uk_irsamgnera6angm0prq1kemt2 UNIQUE (authority);


--
-- Name: acl_class uk_iy7ua5fso3il3u3ymoc4uf35w; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_class
    ADD CONSTRAINT uk_iy7ua5fso3il3u3ymoc4uf35w UNIQUE (class);


--
-- Name: club_profile uk_jtd2g8s80maf7xle8hgmmd80o; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY club_profile
    ADD CONSTRAINT uk_jtd2g8s80maf7xle8hgmmd80o UNIQUE (profile_id);


--
-- Name: club_profile uk_oaxnl5m27s4vatm173xbvf6do; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY club_profile
    ADD CONSTRAINT uk_oaxnl5m27s4vatm173xbvf6do UNIQUE (profile_email);


--
-- Name: user uk_ob8kqyqqgmefl0aco34akdtpe; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email);


--
-- Name: email_verification ukb232bd49e5b8712edc8d6f1868a4; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY email_verification
    ADD CONSTRAINT ukb232bd49e5b8712edc8d6f1868a4 UNIQUE (action, email);


--
-- Name: acl_entry ukce200ed06800e5a163c6ab6c0c85; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT ukce200ed06800e5a163c6ab6c0c85 UNIQUE (acl_object_identity, ace_order);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user_profile user_profile_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile
    ADD CONSTRAINT user_profile_pkey PRIMARY KEY (id);


--
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: item_image fk185htxji13r4s2x62eoxjye3k; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item_image
    ADD CONSTRAINT fk185htxji13r4s2x62eoxjye3k FOREIGN KEY (item_images_id) REFERENCES item(id);


--
-- Name: item2passport fk1t10y5yqkaadeb71bj4hrxxhg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item2passport
    ADD CONSTRAINT fk1t10y5yqkaadeb71bj4hrxxhg FOREIGN KEY (passport_id) REFERENCES passport(id);


--
-- Name: acl_object_identity fk4soxn7uid8qxltqps8kewftx7; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT fk4soxn7uid8qxltqps8kewftx7 FOREIGN KEY (parent_object) REFERENCES acl_object_identity(id);


--
-- Name: club_profile fk5y6d942owugpoycjt59bmxlpd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY club_profile
    ADD CONSTRAINT fk5y6d942owugpoycjt59bmxlpd FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: item_group fk67urgr2etxe0afvgjkxxq3lo; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item_group
    ADD CONSTRAINT fk67urgr2etxe0afvgjkxxq3lo FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: club_profile fk6un39ggsb20886ttw182qymo6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY club_profile
    ADD CONSTRAINT fk6un39ggsb20886ttw182qymo6 FOREIGN KEY (avatar_id) REFERENCES image(id);


--
-- Name: item2passport fk8v1ebuj2ub3jk9j3pouey0h4g; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item2passport
    ADD CONSTRAINT fk8v1ebuj2ub3jk9j3pouey0h4g FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: acl_entry fk9r4mj8ewa904g3wivff0tb5b0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT fk9r4mj8ewa904g3wivff0tb5b0 FOREIGN KEY (sid) REFERENCES acl_sid(id);


--
-- Name: user_role fka68196081fvovjhkek5m97n3y; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES role(id);


--
-- Name: acl_object_identity fkc06nv93ck19el45a3g1p0e58w; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT fkc06nv93ck19el45a3g1p0e58w FOREIGN KEY (object_id_class) REFERENCES acl_class(id);


--
-- Name: comment fkd8gq9g90ee6ewk6aw9e2se5u; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY comment
    ADD CONSTRAINT fkd8gq9g90ee6ewk6aw9e2se5u FOREIGN KEY (club_profile_id) REFERENCES club_profile(id);


--
-- Name: source_image fkeu8sa8tipk8chkldwqq0lmtln; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY source_image
    ADD CONSTRAINT fkeu8sa8tipk8chkldwqq0lmtln FOREIGN KEY (image_id) REFERENCES image(id);


--
-- Name: item_image fkffekuuetvxc58mlha2e9i3tj5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item_image
    ADD CONSTRAINT fkffekuuetvxc58mlha2e9i3tj5 FOREIGN KEY (image_id) REFERENCES image(id);


--
-- Name: user_role fkfgsgxvihks805qcq8sq26ab7c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fkfgsgxvihks805qcq8sq26ab7c FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: item_comment fkfqcrpo3y4l5tsko8698ke0r4e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item_comment
    ADD CONSTRAINT fkfqcrpo3y4l5tsko8698ke0r4e FOREIGN KEY (comment_id) REFERENCES comment(id);


--
-- Name: passport fkhmv85e2axjr1dh6u0yk1nl3rb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY passport
    ADD CONSTRAINT fkhmv85e2axjr1dh6u0yk1nl3rb FOREIGN KEY (club_profile_id) REFERENCES club_profile(id);


--
-- Name: acl_object_identity fkikrbtok3aqlrp9wbq6slh9mcw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_object_identity
    ADD CONSTRAINT fkikrbtok3aqlrp9wbq6slh9mcw FOREIGN KEY (owner_sid) REFERENCES acl_sid(id);


--
-- Name: acl_entry fkl39t1oqikardwghegxe0wdcpt; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY acl_entry
    ADD CONSTRAINT fkl39t1oqikardwghegxe0wdcpt FOREIGN KEY (acl_object_identity) REFERENCES acl_object_identity(id);


--
-- Name: source_comment fklef4yys4enjfcnngx65ldjwh; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY source_comment
    ADD CONSTRAINT fklef4yys4enjfcnngx65ldjwh FOREIGN KEY (comment_id) REFERENCES comment(id);


--
-- Name: comment fknxgp01rkhaqkd8h8y3vgmttpn; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY comment
    ADD CONSTRAINT fknxgp01rkhaqkd8h8y3vgmttpn FOREIGN KEY (user_profile_id) REFERENCES user_profile(id);


--
-- Name: user_profile fkqcd5nmg7d7ement27tt9sf3bi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile
    ADD CONSTRAINT fkqcd5nmg7d7ement27tt9sf3bi FOREIGN KEY (user_id) REFERENCES "user"(id);


--
-- Name: item2source fkqu63sppr2062vav0ky6rua1m; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item2source
    ADD CONSTRAINT fkqu63sppr2062vav0ky6rua1m FOREIGN KEY (source_id) REFERENCES source(id);


--
-- Name: source_image fkqw9wsnm87haq5kr2bttw4bxnw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY source_image
    ADD CONSTRAINT fkqw9wsnm87haq5kr2bttw4bxnw FOREIGN KEY (source_images_id) REFERENCES source(id);


--
-- Name: user_profile fkqyhgmwqnsk8h682f586o6244n; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_profile
    ADD CONSTRAINT fkqyhgmwqnsk8h682f586o6244n FOREIGN KEY (avatar_id) REFERENCES image(id);


--
-- Name: item fkr4fbv7293k0b5v1qjk5lm6md; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item
    ADD CONSTRAINT fkr4fbv7293k0b5v1qjk5lm6md FOREIGN KEY (item_group_id) REFERENCES item_group(id);


--
-- Name: item2source fktp4u7one6eah0pp4u5ul0ami6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY item2source
    ADD CONSTRAINT fktp4u7one6eah0pp4u5ul0ami6 FOREIGN KEY (item_id) REFERENCES item(id);


--
-- Name: passport_comment fkyyu8k8oa59y5eg4pdonqyu2h; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY passport_comment
    ADD CONSTRAINT fkyyu8k8oa59y5eg4pdonqyu2h FOREIGN KEY (comment_id) REFERENCES comment(id);


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM postgres;
REVOKE ALL ON SCHEMA public FROM PUBLIC;
GRANT ALL ON SCHEMA public TO froixxbjnzmsou;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: LANGUAGE plpgsql; Type: ACL; Schema: -; Owner: -
--

GRANT ALL ON LANGUAGE plpgsql TO froixxbjnzmsou;


--
-- PostgreSQL database dump complete
--
