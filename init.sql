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
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '组织ID',
  `name` varchar(100) NOT NULL COMMENT '组织名称',
  `description` text COMMENT '组织描述',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `capacity` int NOT NULL COMMENT '组织的容量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
INSERT INTO `organization` VALUES (20,'test','test','2025-01-06 17:00:34','2025-01-06 17:00:34',100),(21,'test','test','2025-01-06 17:00:48','2025-01-06 17:00:48',100),(22,'test','test','2025-01-06 17:00:59','2025-01-06 17:00:59',100),(23,'廖天娇','常起任华表。程达传收。老气海我对按物美是。酸方新没备无王些支。下则京打。参完一些油第中物安。种什和了何接列。心产题样研该划个济民。','2025-01-06 18:28:37','2025-01-06 18:28:37',22),(24,'廖天娇','常起任华表。程达传收。老气海我对按物美是。酸方新没备无王些支。下则京打。参完一些油第中物安。种什和了何接列。心产题样研该划个济民。','2025-01-06 18:36:44','2025-01-06 18:36:44',22),(25,'组织','zuzhi','2025-01-06 19:16:53','2025-01-06 19:16:53',50),(26,'组织','zuzhi','2025-01-06 19:17:05','2025-01-06 19:17:05',999),(27,'testjoin','testjoin','2025-01-07 22:08:48','2025-01-07 22:08:48',10),(28,'testjoin','testjoin','2025-01-07 22:13:26','2025-01-07 22:13:26',1);
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_user`
--

LOCK TABLES `organization_user` WRITE;
/*!40000 ALTER TABLE `organization_user` DISABLE KEYS */;
INSERT INTO `organization_user` VALUES (20,20,2,2),(21,21,2,2),(22,22,2,2),(23,23,5,2),(24,24,5,2),(25,25,2,2),(26,26,2,2),(27,27,2,2),(28,27,5,0),(29,28,2,2);
/*!40000 ALTER TABLE `organization_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(100) NOT NULL COMMENT '用户名称',
  `email` varchar(100) NOT NULL COMMENT '用户邮箱',
  `phone` varchar(100) NOT NULL COMMENT '用户手机号',
  `username` varchar(100) NOT NULL COMMENT '用户名/账号',
  `password` varchar(255) NOT NULL COMMENT '用户密码',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `head_img` varchar(100) DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'容子欣','1939729609@qq.com','46236141025','嬴诚','k+glZME5LBcwGi+tx9vmJg==','2025-01-05 17:45:05','2025-01-05 09:45:05',NULL),(2,'liuzhicheng','291396312@qq.com','18570687868','lzc','CwIfXN2Osf90jwST7FA6yg==','2025-01-05 21:15:25','2025-01-05 13:15:24',NULL),(5,'make','484005691@qq.com','13464696188','Make','k+glZME5LBcwGi+tx9vmJg==','2025-01-05 21:28:35','2025-01-05 13:28:35','https://loremflickr.com/400/400?lock=2968877270854327'),(6,'name','3515746178@qq.com','12345678901','111','k+glZME5LBcwGi+tx9vmJg==','2025-01-08 13:22:44','2025-01-08 13:22:44','default');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-08 21:49:35
