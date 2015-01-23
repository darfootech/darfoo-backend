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
  `IMAGE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_oawshgflct2lkk4aj6cx7truw` (`IMAGE_ID`),
  CONSTRAINT `FK_oawshgflct2lkk4aj6cx7truw` FOREIGN KEY (`IMAGE_ID`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `author`
--

LOCK TABLES `author` WRITE;
/*!40000 ALTER TABLE `author` DISABLE KEYS */;
INSERT INTO `author` VALUES (1,'韩国女团组合','T-ara',76),(2,'亚洲天王 R&B，中国风代表人物','周杰伦',77),(3,'日本女歌手','仓木麻衣',75),(4,'日本女歌手','滨崎步',69),(5,'asdkabscla','周杰伦333',67),(7,'台湾人气偶像组合','四号3',66),(11,'qweqweqw','周周',73);
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dancegroup`
--

LOCK TABLES `dancegroup` WRITE;
/*!40000 ALTER TABLE `dancegroup` DISABLE KEYS */;
INSERT INTO `dancegroup` VALUES (1,'大妈队','一号',1416718682931,1),(3,'大爷队','二号',1416718732797,2),(4,'小孩队','三号',1416718763283,3),(5,'小妈队','四号',1416718794946,1),(7,'casdasdcc1','ccc1',1417237336000,2),(8,'casdasdcc2','ccc2',1417237336000,2),(9,'casdasdcc3','ccc3',1417237336000,2),(11,'小妈队','四号33',1417449135576,1),(12,'小妈队','四号2312313123',1417770368012,4);
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dancegroupimage`
--

LOCK TABLES `dancegroupimage` WRITE;
/*!40000 ALTER TABLE `dancegroupimage` DISABLE KEYS */;
INSERT INTO `dancegroupimage` VALUES (1,'dg1'),(4,'dg111'),(5,'dg111123'),(2,'dg2'),(3,'dg3'),(6,'哈哈.png');
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
  `MUSIC_ID` int(11) DEFAULT NULL,
  `Hottest` bigint(64) DEFAULT '0',
  `Hottest` bigint(64) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_k5yi8l1xjutsbx1n9eb6b7fts` (`VIDEO_KEY`),
  KEY `FK_3h09bgoo42fmqdwv3qcbfyowx` (`AUTHOR_ID`),
  KEY `FK_sulggpjwxhfc3crcw9kb8nd0q` (`IMAGE_ID`),
  KEY `FK_49vibr012ja4lnyyyc4ieglw7` (`MUSIC_ID`),
  CONSTRAINT `FK_3h09bgoo42fmqdwv3qcbfyowx` FOREIGN KEY (`AUTHOR_ID`) REFERENCES `author` (`id`),
  CONSTRAINT `FK_49vibr012ja4lnyyyc4ieglw7` FOREIGN KEY (`MUSIC_ID`) REFERENCES `music` (`id`),
  CONSTRAINT `FK_sulggpjwxhfc3crcw9kb8nd0q` FOREIGN KEY (`IMAGE_ID`) REFERENCES `image` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `education`
--

LOCK TABLES `education` WRITE;
/*!40000 ALTER TABLE `education` DISABLE KEYS */;
INSERT INTO `education` VALUES (1,'Dearest',1416720959364,'Dearest.mp4',4,4,NULL,6,0),(5,'Strong Heart',1416721385006,'StrongHeart.mp4',3,3,NULL,1,0),(7,'呵呵',1417959724547,'ccccc.mp4',4,4,NULL,0,0),(12,'ccccc',1417237336000,'ccccc5.mp4',3,3,NULL,0,0),(13,'ccccc1',1418126033664,'ccccc1.flv',3,3,NULL,0,0),(14,'ccccc2',1418126006443,'ccccc2.mp4',3,3,NULL,0,0),(15,'ccccc3',1417237336000,'ccccc3.mp4',3,3,NULL,0,0),(18,'asdasd',1417433889097,'asdasd.mp4',4,7,NULL,0,0),(19,'ccc',1417434050955,'ccc.mp4',2,5,NULL,0,0),(20,'ddd',1417435739967,'ddd.mp4',2,9,NULL,0,0),(21,'Strong Heart123',1417442917351,'Strong Heart123.mp4',2,13,NULL,0,0),(25,'Strong Heart321',1417950382290,'Strong Heart321-25.mp4',2,42,NULL,0,0),(26,'呵呵',1418125997578,'呵呵-26.mp4',2,49,NULL,0,0),(29,'呵呵333',1418126164929,'呵呵333-29.flv',2,78,NULL,0,0),(30,'321321312312312321',1418354402796,'哈哈-30.flv',2,83,NULL,0,0);
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
  CONSTRAINT `FK_mke6t2olugmm8mwrn5ep706ur` FOREIGN KEY (`category_id`) REFERENCES `educationcategory` (`id`),
  CONSTRAINT `FK_sa78me9tvbu943uc59dfgew7a` FOREIGN KEY (`video_id`) REFERENCES `education` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `education_category`
--

LOCK TABLES `education_category` WRITE;
/*!40000 ALTER TABLE `education_category` DISABLE KEYS */;
INSERT INTO `education_category` VALUES (6,7),(16,13),(26,13),(27,13),(10,14),(27,14),(28,14),(6,26),(11,26),(28,26),(11,29),(15,29),(27,29),(6,30),(11,30),(15,30);
/*!40000 ALTER TABLE `education_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `educationcategory`
--

DROP TABLE IF EXISTS `educationcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `educationcategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) NOT NULL,
  `TITLE` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lyop6lv5y9pqlfxh58efqpsa0` (`TITLE`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `educationcategory`
--

LOCK TABLES `educationcategory` WRITE;
/*!40000 ALTER TABLE `educationcategory` DISABLE KEYS */;
INSERT INTO `educationcategory` VALUES (6,'待定','稍难'),(10,'待定','快'),(11,'待定','慢'),(12,'待定','简单'),(15,'待定','背面教学'),(16,'待定','分解教学'),(26,'待定','中'),(27,'待定','适中'),(28,'待定','队形表演');
/*!40000 ALTER TABLE `educationcategory` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES (62,'1223321asdasd'),(17,'123'),(8,'123.jpg'),(46,'123.png'),(65,'123213s啊实打实的'),(77,'123周周.jpg'),(82,'21312312'),(9,'321'),(47,'321.png'),(81,'321333.png'),(83,'323213123123123123'),(21,'33213.png'),(16,'333'),(64,'aasdasdasdasdasdasdasd'),(52,'asdasdas'),(57,'asdasdasd.png'),(27,'ccc.png'),(23,'cleantha.png'),(66,'dg111'),(26,'memda.png'),(73,'neneneneneneneen.png'),(49,'nn.png'),(60,'qweqweqwe'),(1,'T-ara.jpg'),(28,'T-ara123.jpg'),(55,'T-ara123321.jpg'),(54,'T-ara123333.jpg'),(43,'T-ara313.jpg'),(44,'T-ara3133.jpg'),(58,'T-ara321123.jpg'),(56,'T-ara321321.jpg'),(59,'T-ara323323.jpg'),(61,'T-ara325521.jpg'),(11,'T-ara333.jpg'),(74,'么么么.png'),(78,'么么么333.png'),(71,'么么么么么.png'),(48,'么么哒.jpg'),(45,'么么哒.png'),(79,'么么阿萨德卡死了都看见么.png'),(3,'仓木麻衣.jpg'),(41,'仓木麻衣111.jpg'),(67,'仓木麻衣3113.jpg'),(12,'仓木麻衣33.jpg'),(42,'仓木麻衣3321.jpg'),(13,'仓木麻衣333.jpg'),(2,'周杰伦.jpg'),(80,'呵呵呵.png'),(76,'哈哈哈啊哈哈哈哈.jpg'),(53,'啊实打实大师大'),(5,'啊实打实的'),(70,'啊实打实的.png'),(14,'大师赛.png'),(51,'恩呢大.png'),(20,'按时打算'),(7,'撒旦撒'),(6,'撒旦撒.png'),(18,'撒旦撒旦'),(19,'撒旦撒旦.png'),(72,'比比比比比比比.png'),(30,'测试.png'),(29,'测试测试.png'),(4,'滨崎步.jpg'),(37,'滨崎步11.jpg'),(38,'滨崎步111.jpg'),(35,'滨崎步123.jpg'),(36,'滨崎步13.jpg'),(10,'滨崎步3.jpg'),(40,'滨崎步311.jpg'),(69,'滨崎步3113.jpg'),(34,'滨崎步321.jpg'),(31,'滨崎步33.jpg'),(39,'滨崎步3311.jpg'),(32,'滨崎步333.jpg'),(33,'滨崎步33333.jpg'),(75,'阿萨德哈斯科技等哈说空间大花洒.png'),(63,'阿萨是多少');
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
  `Hottest` bigint(64) DEFAULT '0',
  `Hottest` bigint(64) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_cf2agfg6jawkf17jx3dd5vogl` (`AUTHOR_ID`),
  KEY `FK_1n0xyfadw1t7csqer5y3wi0a4` (`IMAGE_ID`),
  CONSTRAINT `FK_1n0xyfadw1t7csqer5y3wi0a4` FOREIGN KEY (`IMAGE_ID`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_cf2agfg6jawkf17jx3dd5vogl` FOREIGN KEY (`AUTHOR_ID`) REFERENCES `author` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music`
--

LOCK TABLES `music` WRITE;
/*!40000 ALTER TABLE `music` DISABLE KEYS */;
INSERT INTO `music` VALUES (1,'Dearest','Dearest',1416708620366,4,4,3,0),(3,'ShiJieMoRi','世界末日',1416709042287,2,2,3,0),(4,'QiLiXiang','七里香',1416709090254,2,2,0,0),(5,'StrongHeart','Strong Heart',1416709523952,3,3,0,0),(6,'SexyLove','Sexy Love',1416711274690,1,1,0,0),(9,'Sexy Love33','Sexy Love33',1417442632276,1,11,0,0),(10,'啊实打实的','呵呵呵',1417960107852,3,3,0,0),(11,'撒旦撒旦','ccc',1417446381033,2,21,0,0),(12,'ccc','memeda',1417495332587,2,26,0,0),(15,'Sexy Love333-15','Sexy Love333',1417961323069,1,44,0,0),(16,'ccc-16','memeda',1418004580134,2,51,0,0),(17,'ccc-17','memeda',1418004699514,2,52,0,0),(18,'ccccc-18','ccccc',1418006886410,2,53,0,0),(19,'Sexy Love33333-19','Sexy Love33333',1418051516722,5,56,0,0),(20,'Sexy Love123333-20','Sexy Love123333',1418052900071,5,58,0,0),(22,'qweqwe-22','qweqwe',1418053237214,5,60,0,0),(23,'Sexy Love33321-23','Sexy Love33321',1418126050418,5,61,0,0),(24,'ccccccccc-24','ccccccccc',1418126060026,5,62,0,0),(25,'33ccc么么-25','33ccc么么',1418355767473,11,79,0,0);
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
INSERT INTO `music_category` VALUES (1,1),(5,1),(16,1),(2,3),(12,3),(31,3),(2,4),(5,4),(29,4),(1,5),(5,5),(15,5),(2,6),(5,6),(31,6),(2,9),(5,9),(31,9),(2,10),(8,10),(29,10),(1,11),(7,11),(13,11),(1,12),(7,12),(15,12),(2,15),(5,15),(31,15),(2,16),(12,16),(31,16),(1,17),(7,17),(13,17),(4,18),(7,18),(27,18),(2,19),(5,19),(31,19),(2,20),(5,20),(31,20),(1,22),(7,22),(32,22),(2,23),(5,23),(31,23),(4,24),(7,24),(32,24),(3,25),(8,25),(34,25);
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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
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
  `MUSIC_ID` int(11) DEFAULT NULL,
  `Hottest` bigint(64) DEFAULT '0',
  `Hottest` bigint(64) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ri41vxbkh8bit6dpbqa9quxep` (`VIDEO_KEY`),
  KEY `FK_g4njk6simvjou38x6hoqbjblo` (`AUTHOR_ID`),
  KEY `FK_fr3ilu8k9b10a2b6pg3npgqoo` (`IMAGE_ID`),
  KEY `FK_5c6euhx7ex1dy5cql2wg3rs0r` (`MUSIC_ID`),
  CONSTRAINT `FK_5c6euhx7ex1dy5cql2wg3rs0r` FOREIGN KEY (`MUSIC_ID`) REFERENCES `music` (`id`),
  CONSTRAINT `FK_fr3ilu8k9b10a2b6pg3npgqoo` FOREIGN KEY (`IMAGE_ID`) REFERENCES `image` (`id`),
  CONSTRAINT `FK_g4njk6simvjou38x6hoqbjblo` FOREIGN KEY (`AUTHOR_ID`) REFERENCES `author` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `video`
--

LOCK TABLES `video` WRITE;
/*!40000 ALTER TABLE `video` DISABLE KEYS */;
INSERT INTO `video` VALUES (1,'sexy love',1416646629950,'SexyLove.mp4',1,1,25,3,0),(2,'Love Dovey',1416651445122,'LoveDovey.mp4',1,1,NULL,0,0),(3,'七里香',1416656252118,'QiLiXiang.mp4',2,2,NULL,7,0),(4,'呵呵',1417959072485,'ShiJieMoRi.mp4',3,3,NULL,0,0),(5,'Strong Heart',1416657813933,'StrongHeart.mp4',3,3,NULL,0,0),(6,'Dearest',1417699910569,'Dearest.mp4',4,4,NULL,0,0),(9,'ccc',1417237336000,'ccc.mp4',2,2,NULL,0,0),(10,'Ivy',1417398286995,'Ivy.mp4',4,4,18,0,0),(14,'ccc',1417404066571,'cleantha.mp4',4,4,18,0,0),(15,'clea',1417409835922,'clea.mp4',4,4,18,0,0),(16,'clea33',1417698434484,'clea33.mp4',2,4,NULL,0,0),(19,'啊实打实的',1417435673983,'啊实打实的.mp4',2,8,NULL,0,0),(21,'按时打算',1417444227403,'按时打算.mp4',2,14,NULL,0,0),(22,'撒旦撒旦',1417446330154,'撒旦撒旦.mp4',2,20,NULL,0,0),(23,'啊实打实的321',1417447922054,'啊实打实的321.mp4',NULL,NULL,NULL,0,0),(24,'cleanthaloveyou',1417770388908,'cleanthaloveyou.mp4',2,23,NULL,0,0),(28,'clea33',1417948013920,'clea33-28.mp4',4,32,NULL,0,0),(29,'asdasdsad',1417948992602,'clea33-29.mp4',4,36,NULL,0,0),(34,'文啊恩',1418088569427,'文啊恩-34',2,63,NULL,0,0),(35,'cccccccc',1418310634434,'cccccccc-35.flv',2,64,NULL,0,0),(37,'么么哒',1418263602090,'么么哒-37.mp4',2,80,25,0,0),(38,'么么哒',1418263992565,'么么哒-38.mp4',11,81,25,0,0),(39,'哈哈',1418352766074,'哈哈-39.mp4',1,82,NULL,0,0);
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
INSERT INTO `video_category` VALUES (1,1),(4,1),(10,1),(36,1),(1,2),(4,2),(10,2),(29,2),(2,3),(4,3),(10,3),(34,3),(2,4),(6,4),(13,4),(34,4),(1,5),(6,5),(10,5),(36,5),(2,6),(4,6),(12,6),(21,6),(1,10),(5,10),(10,10),(21,10),(1,14),(5,14),(10,14),(21,14),(2,15),(10,15),(21,15),(2,16),(6,16),(16,16),(37,16),(1,19),(4,19),(7,19),(18,19),(1,21),(4,21),(43,21),(1,22),(4,22),(7,22),(20,22),(1,23),(4,23),(7,23),(41,23),(2,24),(5,24),(7,24),(20,24),(2,28),(5,28),(10,28),(21,28),(2,29),(5,29),(10,29),(21,29),(2,34),(5,34),(9,34),(37,34),(2,35),(5,35),(7,35),(37,35),(1,37),(4,37),(7,37),(23,37),(1,38),(4,38),(7,38),(37,38),(2,39),(5,39),(11,39),(42,39);
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
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `videocategory`
--

LOCK TABLES `videocategory` WRITE;
/*!40000 ALTER TABLE `videocategory` DISABLE KEYS */;
INSERT INTO `videocategory` VALUES (1,'待定','较快'),(2,'待定','适中'),(3,'待定','较慢'),(4,'待定','简单'),(5,'待定','中等'),(6,'待定','稍难'),(7,'待定','欢快'),(8,'待定','活泼'),(9,'待定','优美'),(10,'待定','情歌风'),(11,'待定','红歌风'),(12,'待定','草原风'),(13,'待定','戏曲风'),(14,'待定','印巴风'),(15,'待定','江南风'),(16,'待定','民歌风'),(17,'待定','儿歌风'),(18,'待定','A'),(19,'待定','B'),(20,'待定','C'),(21,'待定','D'),(22,'待定','E'),(23,'待定','F'),(24,'待定','G'),(25,'待定','H'),(26,'待定','I'),(27,'待定','J'),(28,'待定','K'),(29,'待定','L'),(30,'待定','M'),(31,'待定','N'),(32,'待定','O'),(33,'待定','P'),(34,'待定','Q'),(35,'待定','R'),(36,'待定','S'),(37,'待定','T'),(38,'待定','U'),(39,'待定','V'),(40,'待定','W'),(41,'待定','X'),(42,'待定','Y'),(43,'待定','Z'),(45,'待定','中等backup');
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

-- Dump completed on 2014-12-16 10:58:38
