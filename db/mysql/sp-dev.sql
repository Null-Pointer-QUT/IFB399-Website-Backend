-- MySQL dump 10.13  Distrib 5.7.32, for Linux (x86_64)
--
-- Host: localhost    Database: sp-dev
-- ------------------------------------------------------
-- Server version	5.7.32

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

DROP DATABASE IF EXISTS `sp-dev`;

CREATE DATABASE  `sp-dev` DEFAULT CHARACTER SET utf8mb4;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `sp-dev`;

--
-- Table structure for table `sp_admin`
--

DROP TABLE IF EXISTS `sp_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sp_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL ,
  `avatar` varchar(500) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `pw` varchar(50) DEFAULT NULL ,
  `phone` varchar(20) DEFAULT NULL,
  `role_id` int(11) DEFAULT '11' ,
  `status` int(11) DEFAULT '1' ,
  `create_by_aid` bigint(20) DEFAULT '-1' ,
  `create_time` datetime DEFAULT NULL ,
  `login_time` datetime DEFAULT NULL ,
  `login_ip` varchar(50) DEFAULT NULL,
  `login_count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10006 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sp_admin`
--

LOCK TABLES `sp_admin` WRITE;
/*!40000 ALTER TABLE `sp_admin` DISABLE KEYS */;
INSERT INTO `sp_admin` VALUES (10001,'sa','http://demo-jj.dev33.cn/spdj-admin/sa-resources/admin-logo.png','E4EF2A290589A23EFE1565BB698437F5','123456',NULL,1,1,-1,'2021-08-14 18:15:18','2021-09-15 21:55:03','192.168.1.7',87),(10002,'admin','http://demo-jj.dev33.cn/spdj-admin/sa-resources/admin-logo.png','1DE197572C0B23B82BB2F54202E8E00B','admin',NULL,1,1,-1,'2021-08-14 18:15:18',NULL,NULL,0),(10003,'test','http://127.0.0.1:8080/upload/image/2021/08-19/16293678059411546041058.jpeg','057D49AF09D8077077C7137CAD2521F3','123456',NULL,0,1,10001,'2021-08-19 18:10:33',NULL,NULL,0),(10004,'test1','http://test','187AA51DC1D581EA531E0AE6BEA0E2E0','123456',NULL,0,1,10001,'2021-08-19 18:19:28',NULL,NULL,0),(10005,'test2','http://test','A47C07368978296D58C8BFBC76DEDA87','123456',NULL,0,1,10001,'2021-08-19 19:18:35',NULL,NULL,0);
/*!40000 ALTER TABLE `sp_admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sp_apilog`
--

DROP TABLE IF EXISTS `sp_apilog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sp_apilog` (
  `id` bigint(50) NOT NULL AUTO_INCREMENT,
  `req_id` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL ,
  `req_ip` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL ,
  `req_api` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL ,
  `req_parame` mediumtext COLLATE utf8mb4_bin ,
  `req_type` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL ,
  `req_token` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `req_header` mediumtext COLLATE utf8mb4_bin ,
  `res_code` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `res_msg` mediumtext COLLATE utf8mb4_bin ,
  `res_string` mediumtext COLLATE utf8mb4_bin ,
  `user_id` bigint(20) DEFAULT NULL COMMENT 'user_id',
  `admin_id` bigint(20) DEFAULT NULL COMMENT 'admin_id',
  `start_time` datetime(3) DEFAULT NULL ,
  `end_time` datetime(3) DEFAULT NULL ,
  `cost_time` bigint(20) DEFAULT NULL ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28542 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=COMPACT ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sp_apilog`
--

LOCK TABLES `sp_apilog` WRITE;
/*!40000 ALTER TABLE `sp_apilog` DISABLE KEYS */;
/*!40000 ALTER TABLE `sp_apilog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sp_cfg`
--

DROP TABLE IF EXISTS `sp_cfg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sp_cfg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cfg_name` varchar(50) NOT NULL ,
  `cfg_value` text ,
  `remarks` varchar(255) DEFAULT NULL ,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `cfg_name` (`cfg_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sp_cfg`
--

LOCK TABLES `sp_cfg` WRITE;
/*!40000 ALTER TABLE `sp_cfg` DISABLE KEYS */;
INSERT INTO `sp_cfg` VALUES (1,'app_cfg','{}','public'),(2,'server_cfg','{}','private');
/*!40000 ALTER TABLE `sp_cfg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sp_role`
--

DROP TABLE IF EXISTS `sp_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sp_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) NOT NULL ,
  `role_info` varchar(200) DEFAULT NULL ,
  `is_lock` int(11) NOT NULL DEFAULT '1' ,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `name` (`role_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sp_role`
--

LOCK TABLES `sp_role` WRITE;
/*!40000 ALTER TABLE `sp_role` DISABLE KEYS */;
INSERT INTO `sp_role` VALUES (1,'SuperAdmin','Super',1,'2021-08-14 10:15:17'),(2,'SecondAdmin','SecondAdmin',2,'2021-08-14 10:15:17'),(11,'common','common',1,'2021-08-14 10:15:17'),(12,'test','test',2,'2021-08-14 10:15:17');
/*!40000 ALTER TABLE `sp_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sp_role_permission`
--

DROP TABLE IF EXISTS `sp_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sp_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `permission_code` varchar(50) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sp_role_permission`
--

LOCK TABLES `sp_role_permission` WRITE;
/*!40000 ALTER TABLE `sp_role_permission` DISABLE KEYS */;
INSERT INTO `sp_role_permission` VALUES (1,1,'bas','2021-08-14 10:15:17'),(2,1,'1','2021-08-14 10:15:17'),(3,1,'11','2021-08-14 10:15:17'),(4,1,'99','2021-08-14 10:15:17'),(5,1,'console','2021-08-14 10:15:17'),(6,1,'sql-console','2021-08-14 10:15:17'),(7,1,'redis-console','2021-08-14 10:15:17'),(8,1,'apilog-list','2021-08-14 10:15:17'),(9,1,'form-generator','2021-08-14 10:15:17'),(10,1,'auth','2021-08-14 10:15:17'),(11,1,'role-list','2021-08-14 10:15:17'),(12,1,'menu-list','2021-08-14 10:15:17'),(13,1,'admin-list','2021-08-14 10:15:17'),(14,1,'admin-add','2021-08-14 10:15:17'),(15,1,'sp-cfg','2021-08-14 10:15:17'),(16,1,'sp-cfg-app','2021-08-14 10:15:17'),(17,1,'sp-cfg-server','2021-08-14 10:15:17'),(18,11,'11','2021-08-23 15:26:11');
/*!40000 ALTER TABLE `sp_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sp_user`
--

DROP TABLE IF EXISTS `sp_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sp_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `avatar` varchar(500) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `pw` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `role_id` int(11) DEFAULT '11',
  `status` int(11) DEFAULT '1',
  `create_by_aid` bigint(20) DEFAULT '-1',
  `create_time` datetime DEFAULT NULL,
  `login_time` datetime DEFAULT NULL,
  `login_ip` varchar(50) DEFAULT NULL,
  `login_count` int(11) DEFAULT '0',
  `uuid` varchar(255) DEFAULT NULL,
  `source` varchar(31) DEFAULT NULL,
  `about` text,
  `organization` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sp_user_name_uindex` (`name`),
  UNIQUE KEY `sp_user_email_uindex` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=20023 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sp_user`
--

LOCK TABLES `sp_user` WRITE;
/*!40000 ALTER TABLE `sp_user` DISABLE KEYS */;
INSERT INTO `sp_user` VALUES (20009,'test1','test1@np.com','https://minio.juntao.life/ifb399bucket1/test/1631775145277_aviator.jpeg','F17D72A6A3FE9B0687EB60E554061774','123456',NULL,1,1,-1,NULL,'2021-10-18 07:56:27','180.111.70.9',185,NULL,NULL,'This is a test account.','QUT'),(20010,'test2','test2@np.com','https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80','315127F2AC410343CCEBB019A0020A19','123456',NULL,11,1,-1,NULL,'2021-10-01 17:58:35','180.111.71.223',35,NULL,NULL,'This is a test account.','QUT'),(20011,'test3','test3@np.com','https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80','603F75DA4B7934A804D31ED092A6630C','123456',NULL,11,1,-1,NULL,'2021-09-16 04:52:41','45.134.169.3',10,NULL,NULL,'A teacher from the University of Melbourne.','UM'),(20012,'stephen','n10889299@qut.edu.au','https://portrait.gitee.com/uploads/avatars/user/1598/4795569_StephenEvenson_1614863692.png!avatar200','8CEAF549061368D3DFE12E1D34FE7C93','123456',NULL,1,1,-1,NULL,'2021-10-09 16:12:12','45.134.169.3',20,NULL,NULL,'Hi, I\'m Stephen and I like Java and python.',NULL),(20013,'CJ','chenjun@live.com','https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80','D6D7E797F6A7B29065524E53995C4CFA','Jjam123007',NULL,11,1,-1,NULL,'2021-08-30 03:22:40','45.134.169.3',7,NULL,NULL,'QUT student','QUT'),(20014,'ralph','ralph0813.ljt@gmail.com','https://minio.juntao.life:443/ifb399/test/1632987899500_1630338225860_np_logo.png','612DB0099FBA5692D30AB2E6A93FFD10','123456',NULL,11,1,-1,NULL,'2021-09-30 16:15:13','180.111.71.223',12,'105530371145493415334','GOOGLE',NULL,NULL),(20015,'ralph1','ralph1@np.com',NULL,'FB8D21A510766A6B6877D2F2F2C48E21','123456',NULL,11,1,-1,NULL,'2021-08-30 03:00:10','180.111.69.149',1,NULL,NULL,NULL,NULL),(20016,'test4','test4@np.com1',NULL,'352AD7FCAAFABAB4BF71E9393EE14291','123456',NULL,11,1,-1,NULL,'2021-10-01 17:59:55','45.134.169.3',6,NULL,NULL,NULL,NULL),(20017,'test5','test5@np.com',NULL,'C1845155833C4A5A17676A38D556BEB4','123456',NULL,11,1,-1,NULL,'2021-10-01 17:59:41','45.134.169.3',2,NULL,NULL,NULL,NULL),(20018,'test6','test6@np.com',NULL,'3BE2AC3D62BC80593FE231C07EF83743','123456',NULL,11,1,-1,NULL,'2021-09-22 17:47:23','180.111.70.184',1,NULL,NULL,NULL,NULL),(20019,'cynthiapanzj@gmail.com','cynthiapanzj@gmail.com','https://lh3.googleusercontent.com/a/AATXAJy6-X2UyYjpQWy9tOpneKJTN97qgs_yElF-RSIV=s96-c','********',NULL,NULL,11,1,-1,NULL,'2021-09-29 14:11:49','203.175.12.50',1,'110802468800756902249','google',NULL,NULL),(20020,'ralphlu',NULL,'https://portrait.gitee.com/uploads/avatars/user/1901/5704994_ralphlu_1581940899.png','********',NULL,NULL,11,1,-1,NULL,'2021-09-30 07:35:06','180.111.71.223',2,'5704994','gitee',NULL,NULL),(20021,'test10','test10@np.com','https://minio.juntao.life:443/ifb399/test/1634526209170_np_logo.png','BBA9DA8C1F55870E20C24D2DD931D17F','123456',NULL,11,1,20009,NULL,'2021-10-18 04:33:40','45.134.169.3',2,NULL,NULL,'test10',NULL),(20022,'chenjunjunjun@live.com','chenjunjunjun@live.com',NULL,'FF9376E7E1CDDFCE6FC70AFE3C85E9EA','1234',NULL,11,1,-1,NULL,'2021-10-18 03:40:37','45.134.169.3',1,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `sp_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-10-18 16:49:03
SET FOREIGN_KEY_CHECKS = 1;