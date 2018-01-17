CREATE DATABASE  IF NOT EXISTS `tempvs` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `tempvs`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acl_class`
--

DROP TABLE IF EXISTS `acl_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_class` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `class` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_iy7ua5fso3il3u3ymoc4uf35w` (`class`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_class`
--

LOCK TABLES `acl_class` WRITE;
/*!40000 ALTER TABLE `acl_class` DISABLE KEYS */;
/*!40000 ALTER TABLE `acl_class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acl_entry`
--

DROP TABLE IF EXISTS `acl_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sid` bigint(20) NOT NULL,
  `audit_failure` bit(1) NOT NULL,
  `granting` bit(1) NOT NULL,
  `acl_object_identity` bigint(20) NOT NULL,
  `audit_success` bit(1) NOT NULL,
  `ace_order` int(11) NOT NULL,
  `mask` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKce200ed06800e5a163c6ab6c0c85` (`acl_object_identity`,`ace_order`),
  KEY `FK9r4mj8ewa904g3wivff0tb5b0` (`sid`),
  CONSTRAINT `FK9r4mj8ewa904g3wivff0tb5b0` FOREIGN KEY (`sid`) REFERENCES `acl_sid` (`id`),
  CONSTRAINT `FKl39t1oqikardwghegxe0wdcpt` FOREIGN KEY (`acl_object_identity`) REFERENCES `acl_object_identity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_entry`
--

LOCK TABLES `acl_entry` WRITE;
/*!40000 ALTER TABLE `acl_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `acl_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acl_object_identity`
--

DROP TABLE IF EXISTS `acl_object_identity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_object_identity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `object_id_identity` bigint(20) NOT NULL,
  `entries_inheriting` bit(1) NOT NULL,
  `object_id_class` bigint(20) NOT NULL,
  `owner_sid` bigint(20) DEFAULT NULL,
  `parent_object` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK56103a82abb455394f8c97a95587` (`object_id_class`,`object_id_identity`),
  KEY `FKikrbtok3aqlrp9wbq6slh9mcw` (`owner_sid`),
  KEY `FK4soxn7uid8qxltqps8kewftx7` (`parent_object`),
  CONSTRAINT `FK4soxn7uid8qxltqps8kewftx7` FOREIGN KEY (`parent_object`) REFERENCES `acl_object_identity` (`id`),
  CONSTRAINT `FKc06nv93ck19el45a3g1p0e58w` FOREIGN KEY (`object_id_class`) REFERENCES `acl_class` (`id`),
  CONSTRAINT `FKikrbtok3aqlrp9wbq6slh9mcw` FOREIGN KEY (`owner_sid`) REFERENCES `acl_sid` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_object_identity`
--

LOCK TABLES `acl_object_identity` WRITE;
/*!40000 ALTER TABLE `acl_object_identity` DISABLE KEYS */;
/*!40000 ALTER TABLE `acl_object_identity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `acl_sid`
--

DROP TABLE IF EXISTS `acl_sid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `acl_sid` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sid` varchar(255) NOT NULL,
  `principal` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1781b9a084dff171b580608b3640` (`sid`,`principal`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acl_sid`
--

LOCK TABLES `acl_sid` WRITE;
/*!40000 ALTER TABLE `acl_sid` DISABLE KEYS */;
/*!40000 ALTER TABLE `acl_sid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `club_profile`
--

DROP TABLE IF EXISTS `club_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `club_profile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `profile_email` varchar(255) DEFAULT NULL,
  `club_name` varchar(255) DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `nick_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_updated` datetime NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `profile_id` varchar(255) DEFAULT NULL,
  `period` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `avatar_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_oaxnl5m27s4vatm173xbvf6do` (`profile_email`),
  UNIQUE KEY `UK_jtd2g8s80maf7xle8hgmmd80o` (`profile_id`),
  KEY `FKjdnh7yo6hupw51uwuah8cslqe` (`user_id`),
  KEY `FK6un39ggsb20886ttw182qymo6` (`avatar_id`),
  CONSTRAINT `FK6un39ggsb20886ttw182qymo6` FOREIGN KEY (`avatar_id`) REFERENCES `image` (`id`),
  CONSTRAINT `FKjdnh7yo6hupw51uwuah8cslqe` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `club_profile`
--

LOCK TABLES `club_profile` WRITE;
/*!40000 ALTER TABLE `club_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `club_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_verification`
--

DROP TABLE IF EXISTS `email_verification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_verification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `instance_id` bigint(20) DEFAULT NULL,
  `action` varchar(12) NOT NULL,
  `email` varchar(255) NOT NULL,
  `verification_code` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKb232bd49e5b8712edc8d6f1868a4` (`action`,`email`),
  UNIQUE KEY `UK_e6eqaykcfaldxspdi41ul34bb` (`verification_code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_verification`
--

LOCK TABLES `email_verification` WRITE;
/*!40000 ALTER TABLE `email_verification` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_verification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `image_info` varchar(255) DEFAULT NULL,
  `last_updated` datetime NOT NULL,
  `object_id` varchar(255) NOT NULL,
  `collection` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `period` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `item_group_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKr4fbv7293k0b5v1qjk5lm6md` (`item_group_id`),
  CONSTRAINT `FKr4fbv7293k0b5v1qjk5lm6md` FOREIGN KEY (`item_group_id`) REFERENCES `item_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item2passport`
--

DROP TABLE IF EXISTS `item2passport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item2passport` (
  `item_id` bigint(20) NOT NULL,
  `passport_id` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `quantity` bigint(20) NOT NULL,
  PRIMARY KEY (`item_id`,`passport_id`),
  KEY `FK1t10y5yqkaadeb71bj4hrxxhg` (`passport_id`),
  CONSTRAINT `FK1t10y5yqkaadeb71bj4hrxxhg` FOREIGN KEY (`passport_id`) REFERENCES `passport` (`id`),
  CONSTRAINT `FK8v1ebuj2ub3jk9j3pouey0h4g` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item2passport`
--

LOCK TABLES `item2passport` WRITE;
/*!40000 ALTER TABLE `item2passport` DISABLE KEYS */;
/*!40000 ALTER TABLE `item2passport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item2source`
--

DROP TABLE IF EXISTS `item2source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item2source` (
  `item_id` bigint(20) NOT NULL,
  `source_id` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  PRIMARY KEY (`item_id`,`source_id`),
  KEY `FKqu63sppr2062vav0ky6rua1m` (`source_id`),
  CONSTRAINT `FKqu63sppr2062vav0ky6rua1m` FOREIGN KEY (`source_id`) REFERENCES `source` (`id`),
  CONSTRAINT `FKtp4u7one6eah0pp4u5ul0ami6` FOREIGN KEY (`item_id`) REFERENCES `item` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item2source`
--

LOCK TABLES `item2source` WRITE;
/*!40000 ALTER TABLE `item2source` DISABLE KEYS */;
/*!40000 ALTER TABLE `item2source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_group`
--

DROP TABLE IF EXISTS `item_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhewu47uoehtk3ilvxuolxiaf1` (`user_id`),
  CONSTRAINT `FKhewu47uoehtk3ilvxuolxiaf1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_group`
--

LOCK TABLES `item_group` WRITE;
/*!40000 ALTER TABLE `item_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item_image`
--

DROP TABLE IF EXISTS `item_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_image` (
  `item_images_id` bigint(20) NOT NULL,
  `image_id` bigint(20) DEFAULT NULL,
  KEY `FKffekuuetvxc58mlha2e9i3tj5` (`image_id`),
  KEY `FK185htxji13r4s2x62eoxjye3k` (`item_images_id`),
  CONSTRAINT `FK185htxji13r4s2x62eoxjye3k` FOREIGN KEY (`item_images_id`) REFERENCES `item` (`id`),
  CONSTRAINT `FKffekuuetvxc58mlha2e9i3tj5` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_image`
--

LOCK TABLES `item_image` WRITE;
/*!40000 ALTER TABLE `item_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `passport`
--

DROP TABLE IF EXISTS `passport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `passport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `club_profile_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKhmv85e2axjr1dh6u0yk1nl3rb` (`club_profile_id`),
  CONSTRAINT `FKhmv85e2axjr1dh6u0yk1nl3rb` FOREIGN KEY (`club_profile_id`) REFERENCES `club_profile` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `passport`
--

LOCK TABLES `passport` WRITE;
/*!40000 ALTER TABLE `passport` DISABLE KEYS */;
/*!40000 ALTER TABLE `passport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requestmap`
--

DROP TABLE IF EXISTS `requestmap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requestmap` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `config_attribute` varchar(255) NOT NULL,
  `last_updated` datetime NOT NULL,
  `http_method` varchar(255) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3d11b687954e6645e90db4e23cb4` (`http_method`,`url`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requestmap`
--

LOCK TABLES `requestmap` WRITE;
/*!40000 ALTER TABLE `requestmap` DISABLE KEYS */;
/*!40000 ALTER TABLE `requestmap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `authority` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_irsamgnera6angm0prq1kemt2` (`authority`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `source`
--

DROP TABLE IF EXISTS `source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `source` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `period` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `source`
--

LOCK TABLES `source` WRITE;
/*!40000 ALTER TABLE `source` DISABLE KEYS */;
/*!40000 ALTER TABLE `source` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `source_image`
--

DROP TABLE IF EXISTS `source_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `source_image` (
  `source_images_id` bigint(20) NOT NULL,
  `image_id` bigint(20) DEFAULT NULL,
  `images_idx` int(11) DEFAULT NULL,
  KEY `FKeu8sa8tipk8chkldwqq0lmtln` (`image_id`),
  CONSTRAINT `FKeu8sa8tipk8chkldwqq0lmtln` FOREIGN KEY (`image_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `source_image`
--

LOCK TABLES `source_image` WRITE;
/*!40000 ALTER TABLE `source_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `source_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `password_expired` bit(1) NOT NULL,
  `last_updated` datetime NOT NULL,
  `account_expired` bit(1) NOT NULL,
  `account_locked` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `last_active` datetime NOT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_profile`
--

DROP TABLE IF EXISTS `user_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_profile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` bigint(20) NOT NULL,
  `profile_email` varchar(255) DEFAULT NULL,
  `date_created` datetime NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_updated` datetime NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `profile_id` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `avatar_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_fq6rfxjvf0fycjvfchovgs371` (`profile_email`),
  UNIQUE KEY `UK_dnnx1gqmln4no0py3hn5fy334` (`profile_id`),
  KEY `FK6kwj5lk78pnhwor4pgosvb51r` (`user_id`),
  KEY `FKqyhgmwqnsk8h682f586o6244n` (`avatar_id`),
  CONSTRAINT `FK6kwj5lk78pnhwor4pgosvb51r` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKqyhgmwqnsk8h682f586o6244n` FOREIGN KEY (`avatar_id`) REFERENCES `image` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_profile`
--

LOCK TABLES `user_profile` WRITE;
/*!40000 ALTER TABLE `user_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `date_created` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
