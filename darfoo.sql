-- MySQL dump 10.13  Distrib 5.6.21, for osx10.9 (x86_64)
--
-- Host: localhost    Database: darfoo
-- ------------------------------------------------------
-- Server version	5.6.21

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
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `author` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
INSERT INTO `author` VALUES (1,'韩国女团组合','T-ara'),(2,'亚洲天王 R&B，中国风代表人物','周杰伦'),(3,'日本女歌手','仓木麻衣'),(4,'日本女歌手','滨崎步');
/*!40000 ALTER TABLE `author` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dancegroup`
--

DROP TABLE IF EXISTS `dancegroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dancegroup` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) NOT NULL,
  `NAME` varchar(255) NOT NULL,
  `UPDATE_TIMESTAMP` bigint(64) NOT NULL,
  `IMAGE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ktwgfl0yy41oau2cnqgdan9st` (`NAME`),
  KEY `FK_opb0b654ku6rhgrm028bv86ht` (`IMAGE_ID`),
  CONSTRAINT `FK_opb0b654ku6rhgrm028bv86ht` FOREIGN KEY (`IMAGE_ID`) REFERENCES `dancegroupimage` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dancegroup`
--

LOCK TABLES `dancegroup` WRITE;
/*!40000 ALTER TABLE `dancegroup` DISABLE KEYS */;
INSERT INTO `dancegroup` VALUES (1,'大妈队','一号',1416718682931,1),(3,'大爷队','二号',1416718732797,2),(4,'小孩队','三号',1416718763283,3),(5,'小妈队','四号',1416718794946,1);
/*!40000 ALTER TABLE `dancegroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dancegroupimage`
--

DROP TABLE IF EXISTS `dancegroupimage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dancegroupimage` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `IMAGE_KEY` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_n4ri2p3qjxros5dd3qd00ofgs` (`IMAGE_KEY`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dancegroupimage`
--

LOCK TABLES `dancegroupimage` WRITE;
/*!40000 ALTER TABLE `dancegroupimage` DISABLE KEYS */;
INSERT INTO `dancegroupimage` VALUES (1,'dg1'),(2,'dg2'),(3,'dg3');
/*!40000 ALTER TABLE `dancegroupimage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `education`
--

DROP TABLE IF EXISTS `education`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `education` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(255) NOT NULL,
  `UPDATE_TIMESTAMP` bigint(64) NOT NULL,
  `VIDEO_KEY` varchar(255) NOT NULL,
  `AUTHOR_ID` int(11) DEFAULT NULL,
  `IMAGE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k5yi8l1xjutsbx1n9eb6b7fts` (`VIDEO_KEY`),
  KEY `FK_3h09bgoo42fmqdwv3qcbfyowx` (`AUTHOR_ID`),
  KEY `FK_sulggpjwxhfc3crcw9kb8nd0q` (`IMAGE_ID`),
  CONSTRAINT `FK_3h09bgoo42fmqdwv3qcbfyowx` FOREIGN KEY (`AUTHOR_ID`) REFERENCES `author` (`id`),
  CONSTRAINT `FK_sulggpjwxhfc3crcw9kb8nd0q` FOREIGN KEY (`IMAGE_ID`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `education`
--

LOCK TABLES `education` WRITE;
/*!40000 ALTER TABLE `education` DISABLE KEYS */;
INSERT INTO `education` VALUES (1,'Dearest',1416720959364,'Dearest',4,4),(5,'Strong Heart',1416721385006,'StrongHeart',3,3);
/*!40000 ALTER TABLE `education` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `education_category`
--

DROP TABLE IF EXISTS `education_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `education_category` (
  `category_id` int(11) NOT NULL,
  `video_id` int(11) NOT NULL,
  PRIMARY KEY (`video_id`,`category_id`),
  KEY `FK_sa78me9tvbu943uc59dfgew7a` (`video_id`),
  KEY `FK_mke6t2olugmm8mwrn5ep706ur` (`category_id`),
  CONSTRAINT `FK_mke6t2olugmm8mwrn5ep706ur` FOREIGN KEY (`category_id`) REFERENCES `educationcategroy` (`id`),
  CONSTRAINT `FK_sa78me9tvbu943uc59dfgew7a` FOREIGN KEY (`video_id`) REFERENCES `education` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `education_category`
--

LOCK TABLES `education_category` WRITE;
/*!40000 ALTER TABLE `education_category` DISABLE KEYS */;
INSERT INTO `education_category` VALUES (1,1),(4,1),(7,1),(1,5),(5,5),(9,5);
/*!40000 ALTER TABLE `education_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `educationcategroy`
--

DROP TABLE IF EXISTS `educationcategroy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `educationcategroy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lyop6lv5y9pqlfxh58efqpsa0` (`TITLE`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `educationcategroy`
--

LOCK TABLES `educationcategroy` WRITE;
/*!40000 ALTER TABLE `educationcategroy` DISABLE KEYS */;
INSERT INTO `educationcategroy` VALUES (1,'待定','快'),(2,'待定','中'),(3,'待定','慢'),(4,'待定','简单'),(5,'待定','适中'),(6,'待定','稍难'),(7,'待定','队形表演'),(8,'待定','背面教学'),(9,'待定','分解教学');
/*!40000 ALTER TABLE `educationcategroy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `IMAGE_KEY` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_btc8b2kp40nkikcgs27phuhvu` (`IMAGE_KEY`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES (1,'T-ara.jpg'),(3,'仓木麻衣.jpg'),(2,'周杰伦.jpg'),(4,'滨崎步.jpg');
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `music`
--

DROP TABLE IF EXISTS `music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `music` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `MUSIC_KEY` varchar(255) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  `UPDATE_TIMESTAMP` bigint(64) NOT NULL,
  `AUTHOR_ID` int(11) DEFAULT NULL,
  `IMAGE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_cf2agfg6jawkf17jx3dd5vogl` (`AUTHOR_ID`),
  KEY `FK_1n0xyfadw1t7csqer5y3wi0a4` (`IMAGE_ID`),
  CONSTRAINT `FK_1n0xyfadw1t7csqer5y3wi0a4` FOREIGN KEY (`IMAGE_ID`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_cf2agfg6jawkf17jx3dd5vogl` FOREIGN KEY (`AUTHOR_ID`) REFERENCES `author` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music`
--

LOCK TABLES `music` WRITE;
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` VALUES (1,'Dearest','Dearest',1416708620366,4,4),(2,'Dearest','Dearest',1416708959275,4,4),(3,'ShiJieMoRi','世界末日',1416709042287,2,2),(4,'QiLiXiang','七里香',1416709090254,2,2),(5,'StrongHeart','Strong Heart',1416709523952,3,3),(6,'SexyLove','Sexy Love',1416711274690,1,1),(7,'SexyLove','Sexy Love',1416880488499,1,1),(8,'SexyLove','Sexy Love',1416883831833,1,1);
/*!40000 ALTER TABLE `music` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `music_category`
--

DROP TABLE IF EXISTS `music_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `music_category` (
  `category_id` int(11) NOT NULL,
  `music_id` int(11) NOT NULL,
  PRIMARY KEY (`music_id`,`category_id`),
  KEY `FK_leia7b41xtkbc1hrxm9jdxdbn` (`music_id`),
  KEY `FK_oavu6676xn8hfomrmmlo9qdni` (`category_id`),
  CONSTRAINT `FK_leia7b41xtkbc1hrxm9jdxdbn` FOREIGN KEY (`music_id`) REFERENCES `music` (`id`),
  CONSTRAINT `FK_oavu6676xn8hfomrmmlo9qdni` FOREIGN KEY (`category_id`) REFERENCES `musiccategory` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music_category`
--

LOCK TABLES `music_category` WRITE;
/*!40000 ALTER TABLE `music_category` DISABLE KEYS */;
INSERT INTO `music_category` VALUES (1,1),(5,1),(16,1),(1,2),(5,2),(16,2),(2,3),(12,3),(31,3),(2,4),(5,4),(29,4),(1,5),(5,5),(15,5),(2,6),(5,6),(31,6),(2,7),(5,7),(31,7),(2,8),(5,8),(31,8);
/*!40000 ALTER TABLE `music_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `musiccategory`
--

DROP TABLE IF EXISTS `musiccategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `musiccategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) NOT NULL,
  `Title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_gfd7ay99petp52qo60abbh79e` (`Title`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `musiccategory`
--

LOCK TABLES `musiccategory` WRITE;
/*!40000 ALTER TABLE `musiccategory` DISABLE KEYS */;
INSERT INTO `musiccategory` VALUES (1,'待定','四拍'),(2,'待定','八拍'),(3,'待定','十六拍'),(4,'待定','三十二拍'),(5,'待定','情歌风'),(6,'待定','红歌风'),(7,'待定','草原风'),(8,'待定','戏曲风'),(9,'待定','印巴风'),(10,'待定','江南风'),(11,'待定','民歌风'),(12,'待定','儿歌风'),(13,'待定','A'),(14,'待定','B'),(15,'待定','C'),(16,'待定','D'),(17,'待定','E'),(18,'待定','F'),(19,'待定','G'),(20,'待定','H'),(21,'待定','I'),(22,'待定','J'),(23,'待定','K'),(24,'待定','L'),(25,'待定','M'),(26,'待定','N'),(27,'待定','O'),(28,'待定','P'),(29,'待定','Q'),(30,'待定','R'),(31,'待定','S'),(32,'待定','T'),(33,'待定','U'),(34,'待定','V'),(35,'待定','W'),(36,'待定','X'),(37,'待定','Y'),(38,'待定','Z');
/*!40000 ALTER TABLE `musiccategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video`
--

DROP TABLE IF EXISTS `video`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `video` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(255) NOT NULL,
  `UPDATE_TIMESTAMP` bigint(64) NOT NULL,
  `VIDEO_KEY` varchar(255) NOT NULL,
  `AUTHOR_ID` int(11) DEFAULT NULL,
  `IMAGE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ri41vxbkh8bit6dpbqa9quxep` (`VIDEO_KEY`),
  KEY `FK_g4njk6simvjou38x6hoqbjblo` (`AUTHOR_ID`),
  KEY `FK_fr3ilu8k9b10a2b6pg3npgqoo` (`IMAGE_ID`),
  CONSTRAINT `FK_fr3ilu8k9b10a2b6pg3npgqoo` FOREIGN KEY (`IMAGE_ID`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_g4njk6simvjou38x6hoqbjblo` FOREIGN KEY (`AUTHOR_ID`) REFERENCES `author` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
INSERT INTO `video` VALUES (1,'sexy love',1416646629950,'SexyLove',1,1),(2,'Love Dovey',1416651445122,'LoveDovey',1,1),(3,'七里香',1416656252118,'QiLiXiang',2,2),(4,'世界末日',1416657574624,'ShiJieMoRi',2,2),(5,'Strong Heart',1416657813933,'StrongHeart',3,3),(6,'Dearest',1416657872545,'Dearest',4,4);
/*!40000 ALTER TABLE `video` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `video_category`
--

DROP TABLE IF EXISTS `video_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `video_category` (
  `category_id` int(11) NOT NULL,
  `video_id` int(11) NOT NULL,
  PRIMARY KEY (`video_id`,`category_id`),
  KEY `FK_so046v870uwktu6s53ukm8syi` (`video_id`),
  KEY `FK_32ph02gpmt2rhaapcoxatvs3s` (`category_id`),
  CONSTRAINT `FK_32ph02gpmt2rhaapcoxatvs3s` FOREIGN KEY (`category_id`) REFERENCES `videocategory` (`id`),
  CONSTRAINT `FK_so046v870uwktu6s53ukm8syi` FOREIGN KEY (`video_id`) REFERENCES `video` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video_category`
--

LOCK TABLES `video_category` WRITE;
/*!40000 ALTER TABLE `video_category` DISABLE KEYS */;
INSERT INTO `video_category` VALUES (1,1),(4,1),(10,1),(36,1),(1,2),(4,2),(10,2),(29,2),(2,3),(4,3),(10,3),(34,3),(2,4),(4,4),(10,4),(36,4),(1,5),(6,5),(10,5),(36,5),(1,6),(5,6),(10,6),(21,6);
/*!40000 ALTER TABLE `video_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `videocategory`
--

DROP TABLE IF EXISTS `videocategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `videocategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kw521qb5o9sdhg6q5jdgv8hql` (`TITLE`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videocategory`
--

LOCK TABLES `videocategory` WRITE;
/*!40000 ALTER TABLE `videocategory` DISABLE KEYS */;
INSERT INTO `videocategory` VALUES (1,'待定','较快'),(2,'待定','适中'),(3,'待定','较慢'),(4,'待定','简单'),(5,'待定','普通'),(6,'待定','稍难'),(7,'待定','欢快'),(8,'待定','活泼'),(9,'待定','优美'),(10,'待定','情歌风'),(11,'待定','红歌风'),(12,'待定','草原风'),(13,'待定','戏曲风'),(14,'待定','印巴风'),(15,'待定','江南风'),(16,'待定','民歌风'),(17,'待定','儿歌风'),(18,'待定','A'),(19,'待定','B'),(20,'待定','C'),(21,'待定','D'),(22,'待定','E'),(23,'待定','F'),(24,'待定','G'),(25,'待定','H'),(26,'待定','I'),(27,'待定','J'),(28,'待定','K'),(29,'待定','L'),(30,'待定','M'),(31,'待定','N'),(32,'待定','O'),(33,'待定','P'),(34,'待定','Q'),(35,'待定','R'),(36,'待定','S'),(37,'待定','T'),(38,'待定','U'),(39,'待定','V'),(40,'待定','W'),(41,'待定','X'),(42,'待定','Y'),(43,'待定','Z');
/*!40000 ALTER TABLE `videocategory` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-11-26 14:51:48
