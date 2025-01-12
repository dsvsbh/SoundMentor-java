-- MySQL dump 10.13  Distrib 8.4.0, for Win64 (x86_64)
--
-- Host: 121.43.62.36    Database: sound_mentor
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '文件唯一标识',
  `origin_name` varchar(255) NOT NULL COMMENT '文件原始名称',
  `path` varchar(255) NOT NULL COMMENT '文件存储路径',
  `file_size` bigint NOT NULL COMMENT '文件大小',
  `file_type` int NOT NULL COMMENT '文件类型',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '文件创建时间',
  `md5` varchar(255) NOT NULL COMMENT '文件的唯一标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `path` (`path`),
  UNIQUE KEY `md5` (`md5`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件表，用于存储文件的基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` VALUES (1,'01-第十六届中国大学生服务外包创新创业大赛参赛手册.pdf','http://121.43.62.36:9000/doc/1736660981975_01-第十六届中国大学生服务外包创新创业大赛参赛手册.pdf',509863,3,'2025-01-12 13:49:43','af7e83ed32e798b8ff73924e4479f149');
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) NOT NULL,
  `description` text,
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `capacity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
INSERT INTO `organization` VALUES (20,'test','test','2025-01-06 17:00:34','2025-01-06 17:00:34',100),(21,'test','test','2025-01-06 17:00:48','2025-01-06 17:00:48',100),(22,'test','test','2025-01-06 17:00:59','2025-01-06 17:00:59',100),(23,'','','2025-01-06 18:28:37','2025-01-06 18:28:37',22),(24,'','','2025-01-06 18:36:44','2025-01-06 18:36:44',22),(25,'','zuzhi','2025-01-06 19:16:53','2025-01-06 19:16:53',50),(26,'','zuzhi','2025-01-06 19:17:05','2025-01-06 19:17:05',999),(27,'testjoin','testjoin','2025-01-07 22:08:48','2025-01-07 22:08:48',10),(28,'testjoin','testjoin','2025-01-07 22:13:26','2025-01-07 22:13:26',1),(29,'test','111','2025-01-12 06:36:55','2025-01-12 06:36:55',3);
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_file`
--

DROP TABLE IF EXISTS `organization_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization_file` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '记录唯一标识',
  `organization_id` int NOT NULL COMMENT '组织ID',
  `file_id` int NOT NULL COMMENT '文件ID，关联文件表',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织文件关系表，用于存储组织与文件之间的关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_file`
--

LOCK TABLES `organization_file` WRITE;
/*!40000 ALTER TABLE `organization_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_user`
--

DROP TABLE IF EXISTS `organization_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `organization_id` int NOT NULL,
  `user_id` int NOT NULL,
  `organization_role` int NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_user`
--

LOCK TABLES `organization_user` WRITE;
/*!40000 ALTER TABLE `organization_user` DISABLE KEYS */;
INSERT INTO `organization_user` VALUES (20,20,2,2,'2025-01-09 16:15:57'),(21,21,2,2,'2025-01-09 16:16:01'),(22,22,2,2,'2025-01-09 16:16:03'),(23,23,5,2,'2025-01-09 16:16:02'),(24,24,5,2,'2025-01-09 16:16:04'),(25,25,2,2,'2025-01-09 16:16:06'),(26,26,2,2,'2025-01-09 16:16:05'),(27,27,2,2,'2025-01-09 16:16:07'),(28,27,5,0,'2025-01-09 16:16:09'),(29,28,2,2,'2025-01-09 16:16:08'),(30,29,6,2,'2025-01-12 06:36:55');
/*!40000 ALTER TABLE `organization_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(100) NOT NULL,
  `username` varchar(100) NOT NULL COMMENT '/',
  `password` varchar(255) NOT NULL,
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `head_img` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'','1939729609@qq.com','46236141025','','k+glZME5LBcwGi+tx9vmJg==','2025-01-05 17:45:05','2025-01-05 09:45:05',NULL),(2,'liuzhicheng','291396312@qq.com','18570687868','lzc','CwIfXN2Osf90jwST7FA6yg==','2025-01-05 21:15:25','2025-01-05 13:15:24',NULL),(5,'make','484005691@qq.com','13464696188','Make','k+glZME5LBcwGi+tx9vmJg==','2025-01-05 21:28:35','2025-01-05 13:28:35','https://loremflickr.com/400/400?lock=2968877270854327'),(6,'name','3515746178@qq.com','12345678901','111','k+glZME5LBcwGi+tx9vmJg==','2025-01-08 13:22:44','2025-01-08 13:22:44','default'),(7,'make1938','fzw1938@163.com','028 8847 4323','make1938','k+glZME5LBcwGi+tx9vmJg==','2025-01-11 15:11:43','2025-01-11 07:11:43','default');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_file`
--

DROP TABLE IF EXISTS `user_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_file` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `file_id` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_file`
--

LOCK TABLES `user_file` WRITE;
/*!40000 ALTER TABLE `user_file` DISABLE KEYS */;
INSERT INTO `user_file` VALUES (1,2,1,'2025-01-12 13:49:43'),(2,5,1,'2025-01-12 13:52:12');
/*!40000 ALTER TABLE `user_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_sound_rel`
--

DROP TABLE IF EXISTS `user_sound_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_sound_rel` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自增长的唯一标识符',
  `user_id` int NOT NULL COMMENT '关联用户的唯一标识，不能为空',
  `sound_url` varchar(255) NOT NULL COMMENT '用户声音文件的存储地址',
  `status` int DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录这行数据的创建时间，默认是当前系统时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_sound_url` (`user_id`,`sound_url`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用于存储用户声音相关记录的表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_sound_rel`
--

LOCK TABLES `user_sound_rel` WRITE;
/*!40000 ALTER TABLE `user_sound_rel` DISABLE KEYS */;
INSERT INTO `user_sound_rel` VALUES (1,5,'/mp3/123/test.mp3',0,'2025-01-12 14:52:17'),(3,5,'/mp3/123/test2.mp3',0,'2025-01-12 14:59:21'),(10,5,'/mp3/123/test09.mp3',0,'2025-01-12 15:12:01');
/*!40000 ALTER TABLE `user_sound_rel` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-12 16:22:35
