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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文件表，用于存储文件的基本信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
INSERT INTO `file` VALUES (1,'01-第十六届中国大学生服务外包创新创业大赛参赛手册.pdf','http://121.43.62.36:9000/doc/1736660981975_01-第十六届中国大学生服务外包创新创业大赛参赛手册.pdf',509863,3,'2025-01-12 13:49:43','af7e83ed32e798b8ff73924e4479f149'),(2,'test.mp3','http://121.43.62.36:9000/mp3/1736845832691_test.mp3',201086,0,'2025-01-14 09:10:33','8176cebc4a669345b65a0df11e4e047f');
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
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
INSERT INTO `organization` VALUES (20,'test','test','2025-01-06 17:00:34','2025-01-06 17:00:34',100),(21,'test','test','2025-01-06 17:00:48','2025-01-06 17:00:48',100),(22,'test','test','2025-01-06 17:00:59','2025-01-06 17:00:59',100),(23,'','','2025-01-06 18:28:37','2025-01-06 18:28:37',22),(24,'','','2025-01-06 18:36:44','2025-01-06 18:36:44',22),(25,'','zuzhi','2025-01-06 19:16:53','2025-01-06 19:16:53',50),(26,'','zuzhi','2025-01-06 19:17:05','2025-01-06 19:17:05',999),(27,'testjoin','testjoin','2025-01-07 22:08:48','2025-01-07 22:08:48',10),(28,'testjoin','testjoin','2025-01-07 22:13:26','2025-01-07 22:13:26',1),(37,'ggg','gg','2025-01-14 08:26:16','2025-01-14 08:26:16',2);
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
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_user`
--

LOCK TABLES `organization_user` WRITE;
/*!40000 ALTER TABLE `organization_user` DISABLE KEYS */;
INSERT INTO `organization_user` VALUES (20,20,2,2,'2025-01-09 16:15:57'),(21,21,2,2,'2025-01-09 16:16:01'),(22,22,2,2,'2025-01-09 16:16:03'),(23,23,5,2,'2025-01-09 16:16:02'),(24,24,5,2,'2025-01-09 16:16:04'),(25,25,2,2,'2025-01-09 16:16:06'),(26,26,2,2,'2025-01-09 16:16:05'),(27,27,2,2,'2025-01-09 16:16:07'),(28,27,5,0,'2025-01-09 16:16:09'),(29,28,2,2,'2025-01-09 16:16:08'),(39,37,6,2,'2025-01-14 08:26:16'),(40,37,5,0,'2025-01-14 08:26:34');
/*!40000 ALTER TABLE `organization_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '任务ID，自动递增',
  `task_detail` json NOT NULL COMMENT '任务详细信息，JSON格式',
  `type` int NOT NULL COMMENT '任务类型，VARCHAR类型',
  `status` int NOT NULL COMMENT '任务状态，VARCHAR类型',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，默认为当前时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间，更新时自动更新为当前时间',
  `result` json DEFAULT NULL COMMENT '任务结果',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务表，存储所有任务的详细信息和状态';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (2,'{\"id\": 12, \"status\": 0, \"userId\": 2, \"soundUrl\": \"test\", \"createTime\": \"2025-01-12T23:29:57.170\"}',1,0,'2025-01-12 23:29:57','2025-01-12 23:29:57',NULL),(10,'{\"id\": 21, \"status\": 0, \"userId\": 2, \"soundUrl\": \"test1\", \"createTime\": \"2025-01-13T11:52:51.404\"}',1,0,'2025-01-13 11:52:51','2025-01-13 11:52:51',NULL),(11,'{\"id\": 22, \"status\": 0, \"userId\": 2, \"soundUrl\": \"test11\", \"createTime\": \"2025-01-13T15:05:12.489\"}',1,0,'2025-01-13 15:05:13','2025-01-13 15:05:13',NULL),(12,'{\"id\": 23, \"status\": 0, \"userId\": 2, \"soundUrl\": \"test112\", \"createTime\": \"2025-01-13T15:09:50.584\"}',1,0,'2025-01-13 15:09:51','2025-01-13 15:09:51',NULL),(13,'{\"id\": 24, \"status\": 0, \"userId\": 2, \"soundUrl\": \"test1123\", \"createTime\": \"2025-01-13T15:15:35.360\"}',1,0,'2025-01-13 15:15:35','2025-01-13 15:15:35',NULL),(14,'{\"id\": 25, \"status\": 0, \"userId\": 2, \"soundUrl\": \"123\", \"createTime\": \"2025-01-13T22:18:48.940\"}',1,0,'2025-01-13 22:18:49','2025-01-13 22:18:49','{}'),(15,'{\"id\": 27, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound\", \"createTime\": \"2025-01-14T09:32:13.996\"}',1,0,'2025-01-14 09:32:14','2025-01-14 09:32:14','{}'),(16,'{\"id\": 28, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound1\", \"createTime\": \"2025-01-14T09:40:10.984\"}',1,0,'2025-01-14 09:40:11','2025-01-14 09:40:11','{}'),(17,'{\"id\": 29, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound2\", \"createTime\": \"2025-01-14T09:43:04.750\"}',1,0,'2025-01-14 09:43:05','2025-01-14 09:43:05','{}'),(18,'{\"id\": 30, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound3\", \"createTime\": \"2025-01-14T09:44:07.132\"}',1,0,'2025-01-14 09:44:07','2025-01-14 09:44:07','{}'),(19,'{\"id\": 31, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound4\", \"createTime\": \"2025-01-14T11:35:36.965\"}',1,0,'2025-01-14 11:35:37','2025-01-14 11:35:37','{}'),(20,'{\"id\": 32, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound5\", \"createTime\": \"2025-01-14T11:37:17.235\"}',1,0,'2025-01-14 11:37:17','2025-01-14 11:37:17','{}'),(21,'{\"id\": 33, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound6\", \"createTime\": \"2025-01-14T11:39:27.015\"}',1,0,'2025-01-14 11:39:27','2025-01-14 11:39:27','{}'),(22,'{\"id\": 34, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound7\", \"createTime\": \"2025-01-14T11:43:24.569\"}',1,0,'2025-01-14 11:43:25','2025-01-14 11:43:25','{}'),(23,'{\"id\": 35, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound8\", \"createTime\": \"2025-01-14T11:47:41.356\"}',1,0,'2025-01-14 11:47:41','2025-01-14 11:47:41','{}'),(24,'{\"id\": 36, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound\", \"createTime\": \"2025-01-14T12:15:35.188\"}',1,0,'2025-01-14 12:15:35','2025-01-14 12:15:35','{}'),(25,'{\"id\": 38, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound1\", \"createTime\": \"2025-01-14T12:17:30.290\"}',1,0,'2025-01-14 12:17:30','2025-01-14 12:17:30','{}'),(26,'{\"id\": 39, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound2\", \"createTime\": \"2025-01-14T12:20:57.697\"}',1,0,'2025-01-14 12:20:58','2025-01-14 12:20:58','{}'),(27,'{\"id\": 40, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound3\", \"createTime\": \"2025-01-14T12:23:48.616\"}',1,0,'2025-01-14 12:23:49','2025-01-14 12:23:49','{}'),(28,'{\"id\": 41, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound6\", \"createTime\": \"2025-01-14T12:25:38.991\"}',1,0,'2025-01-14 12:25:39','2025-01-14 12:25:39','{}'),(29,'{\"id\": 42, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound7\", \"createTime\": \"2025-01-14T12:29:13.610\"}',1,0,'2025-01-14 12:29:14','2025-01-14 12:29:14','{}'),(30,'{\"id\": 45, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound9\", \"createTime\": \"2025-01-14T12:52:14.813\"}',1,0,'2025-01-14 12:52:15','2025-01-14 12:52:15','{}'),(31,'{\"id\": 46, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound10\", \"createTime\": \"2025-01-14T13:04:33.148\"}',1,0,'2025-01-14 13:04:33','2025-01-14 13:04:33','{}'),(32,'{\"id\": 48, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound1\", \"createTime\": \"2025-01-14T13:05:57.873\"}',1,0,'2025-01-14 13:05:58','2025-01-14 13:05:58','{}'),(33,'{\"id\": 49, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound2\", \"createTime\": \"2025-01-14T13:16:11.229\"}',1,1,'2025-01-14 13:16:11','2025-01-14 13:16:47','{\"id\": 49, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound2\", \"createTime\": \"2025-01-14T13:16:11.229\"}'),(34,'{\"id\": 51, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound3\", \"createTime\": \"2025-01-14T13:18:00.640\"}',1,1,'2025-01-14 13:18:01','2025-01-14 13:18:14','{\"id\": 51, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound3\", \"createTime\": \"2025-01-14T13:18:00.640\"}'),(35,'{\"id\": 52, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound4\", \"createTime\": \"2025-01-14T13:20:59.208\"}',1,3,'2025-01-14 13:20:59','2025-01-14 13:21:27','{\"id\": 52, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound4\", \"createTime\": \"2025-01-14T13:20:59.208\"}'),(36,'{\"id\": 54, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound5\", \"createTime\": \"2025-01-14T13:22:27.113\"}',1,0,'2025-01-14 13:22:27','2025-01-14 13:22:27','{}'),(37,'{\"id\": 55, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound6\", \"createTime\": \"2025-01-14T13:24:55.420\"}',1,0,'2025-01-14 13:24:55','2025-01-14 13:24:55','{}'),(38,'{\"id\": 57, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound7\", \"createTime\": \"2025-01-14T13:25:47.551\"}',1,0,'2025-01-14 13:25:48','2025-01-14 13:25:48','{}'),(39,'{\"id\": 59, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound8\", \"createTime\": \"2025-01-14T13:27:23.229\"}',1,0,'2025-01-14 13:27:23','2025-01-14 13:27:23','{}'),(40,'{\"id\": 60, \"status\": 0, \"userId\": 5, \"soundUrl\": \"/path/to/sound9\", \"createTime\": \"2025-01-14T13:33:04.272\"}',1,3,'2025-01-14 13:33:04','2025-01-14 13:33:08','{\"message\": \"当前服务器硬件不满足训练条件，训练失败！\"}');
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
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
INSERT INTO `user` VALUES (1,'','1939729609@qq.com','46236141025','','k+glZME5LBcwGi+tx9vmJg==','2025-01-05 17:45:05','2025-01-05 09:45:05',NULL),(2,'liuzhicheng','291396312@qq.com','18570687868','lzc','CwIfXN2Osf90jwST7FA6yg==','2025-01-05 21:15:25','2025-01-05 13:15:24',NULL),(5,'make','484005691@qq.com','13464696188','Make','k+glZME5LBcwGi+tx9vmJg==','2025-01-05 21:28:35','2025-01-05 13:28:35','https://loremflickr.com/400/400?lock=2968877270854327'),(6,'111','3515746178@qq.com','12345678902','111','k+glZME5LBcwGi+tx9vmJg==','2025-01-08 13:22:44','2025-01-08 13:22:44','default'),(7,'make1938','fzw1938@163.com','028 8847 4323','make1938','k+glZME5LBcwGi+tx9vmJg==','2025-01-11 15:11:43','2025-01-11 07:11:43','default');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_file`
--

LOCK TABLES `user_file` WRITE;
/*!40000 ALTER TABLE `user_file` DISABLE KEYS */;
INSERT INTO `user_file` VALUES (1,2,1,'2025-01-12 13:49:43'),(2,5,1,'2025-01-12 13:52:12'),(3,6,2,'2025-01-14 09:10:33');
/*!40000 ALTER TABLE `user_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_ppt_detail`
--

DROP TABLE IF EXISTS `user_ppt_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_ppt_detail` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自增长的唯一标识符',
  `user_ppt_id` int NOT NULL COMMENT '关联user_ppt_rel',
  `ppt_page` int NOT NULL COMMENT 'ppt的页数',
  `summary` varchar(255) NOT NULL COMMENT '该页ppt的摘要',
  `sound_url` varchar(255) NOT NULL COMMENT '该页ppt的音频地址',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录这行数据的创建时间，默认是当前系统时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_ppt_page_url` (`user_ppt_id`,`ppt_page`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_ppt_detail`
--

LOCK TABLES `user_ppt_detail` WRITE;
/*!40000 ALTER TABLE `user_ppt_detail` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_ppt_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_ppt_rel`
--

DROP TABLE IF EXISTS `user_ppt_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_ppt_rel` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键，自增长的唯一标识符',
  `user_id` int NOT NULL COMMENT '关联用户的唯一标识，不能为空',
  `ppt_url` varchar(255) NOT NULL COMMENT '用户ppt文件的存储地址',
  `has_sound` tinyint(1) NOT NULL COMMENT '是否包含ppt',
  `page_count` int NOT NULL COMMENT 'ppt的页数',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录这行数据的创建时间，默认是当前系统时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_ppt_url` (`user_id`,`ppt_url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用于存储用户ppt相关记录的表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_ppt_rel`
--

LOCK TABLES `user_ppt_rel` WRITE;
/*!40000 ALTER TABLE `user_ppt_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_ppt_rel` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用于存储用户声音相关记录的表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_sound_rel`
--

LOCK TABLES `user_sound_rel` WRITE;
/*!40000 ALTER TABLE `user_sound_rel` DISABLE KEYS */;
INSERT INTO `user_sound_rel` VALUES (1,5,'/mp3/123/test.mp3',0,'2025-01-12 14:52:17'),(3,5,'/mp3/123/test2.mp3',0,'2025-01-12 14:59:21'),(10,5,'/mp3/123/test09.mp3',0,'2025-01-12 15:12:01'),(12,2,'test',0,'2025-01-12 23:29:57'),(21,2,'test1',0,'2025-01-13 11:52:51'),(22,2,'test11',0,'2025-01-13 15:05:12'),(23,2,'test112',0,'2025-01-13 15:09:51'),(24,2,'test1123',0,'2025-01-13 15:15:35'),(25,2,'123',0,'2025-01-13 22:18:49'),(46,5,'/path/to/sound10',3,'2025-01-14 13:04:33'),(48,5,'/path/to/sound1',1,'2025-01-14 13:05:58'),(49,5,'/path/to/sound2',1,'2025-01-14 13:16:11'),(51,5,'/path/to/sound3',1,'2025-01-14 13:18:01'),(52,5,'/path/to/sound4',3,'2025-01-14 13:20:59'),(54,5,'/path/to/sound5',3,'2025-01-14 13:22:27'),(55,5,'/path/to/sound6',3,'2025-01-14 13:24:55'),(57,5,'/path/to/sound7',3,'2025-01-14 13:25:48'),(59,5,'/path/to/sound8',3,'2025-01-14 13:27:23'),(60,5,'/path/to/sound9',3,'2025-01-14 13:33:04');
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

-- Dump completed on 2025-01-14 21:29:59
