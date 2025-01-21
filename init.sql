-- MySQL dump 10.13  Distrib 8.3.0, for macos14 (arm64)
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
  `sound_name` varchar(255) NOT NULL DEFAULT '' COMMENT '声音名称',
  `type` int NOT NULL DEFAULT '0' COMMENT '类型：0 代表自定义训练，1 代表预设声音',
  `api_param` varchar(255) DEFAULT 'self-defined' COMMENT 'API 参数',
  `description` varchar(255) DEFAULT '' COMMENT '声音描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_sound_url` (`user_id`,`sound_url`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用于存储用户声音相关记录的表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_sound_rel`
--

LOCK TABLES `user_sound_rel` WRITE;
/*!40000 ALTER TABLE `user_sound_rel` DISABLE KEYS */;
INSERT INTO `user_sound_rel` VALUES (1,5,'/mp3/123/test.mp3',0,'2025-01-12 14:52:17','用户测试',0,'self-defined','用户自训练声音'),(3,5,'/mp3/123/test2.mp3',0,'2025-01-12 14:59:21','用户测试',0,'self-defined','用户自训练声音'),(10,5,'/mp3/123/test09.mp3',0,'2025-01-12 15:12:01','用户测试',0,'self-defined','用户自训练声音'),(12,2,'test',0,'2025-01-12 23:29:57','用户测试',0,'self-defined','用户自训练声音'),(21,2,'test1',0,'2025-01-13 11:52:51','用户测试',0,'self-defined','用户自训练声音'),(22,2,'test11',0,'2025-01-13 15:05:12','用户测试',0,'self-defined','用户自训练声音'),(23,2,'test112',0,'2025-01-13 15:09:51','用户测试',0,'self-defined','用户自训练声音'),(24,2,'test1123',0,'2025-01-13 15:15:35','111',0,'self-defined','用户自训练声音'),(25,2,'123',0,'2025-01-13 22:18:49','用户测试1',0,'self-defined','用户自训练声音'),(46,5,'/path/to/sound10',3,'2025-01-14 13:04:33','用户测试2',0,'self-defined','用户自训练声音'),(48,5,'/path/to/sound1',1,'2025-01-14 13:05:58','用户测试3',0,'self-defined','用户自训练声音'),(49,5,'/path/to/sound2',1,'2025-01-14 13:16:11','用户测试4',0,'self-defined','用户自训练声音'),(51,5,'/path/to/sound3',1,'2025-01-14 13:18:01','用户测试5',0,'self-defined','用户自训练声音'),(52,5,'/path/to/sound4',3,'2025-01-14 13:20:59','用户测试6',0,'self-defined','用户自训练声音'),(54,5,'/path/to/sound5',3,'2025-01-14 13:22:27','用户测试7',0,'self-defined','用户自训练声音'),(55,5,'/path/to/sound6',3,'2025-01-14 13:24:55','用户测试8',0,'self-defined','用户自训练声音'),(57,5,'/path/to/sound7',3,'2025-01-14 13:25:48','用户测试9',0,'self-defined','用户自训练声音'),(59,5,'/path/to/sound8',3,'2025-01-14 13:27:23','用户测试10',0,'self-defined','用户自训练声音'),(60,5,'/path/to/sound9',3,'2025-01-14 13:33:04','用户测试11',0,'self-defined','用户自训练声音'),(61,2,'test.mp3',2,'2025-01-14 21:35:52','用户测试12',0,'self-defined','用户自训练声音'),(62,5,'/path/to/sound10.mp3',2,'2025-01-15 09:49:22','用户测试13',0,'self-defined','用户自训练声音'),(63,5,'/path/to/sound11.mp3',2,'2025-01-19 11:42:52','用户测试666',0,'self-defined','用户自训练声音'),(64,6,'http://121.43.62.36:9000/mp3/1736845832691_test.mp3',0,'2025-01-19 07:32:17','456',0,'self-defined','用户自训练声音'),(65,2,'123.mp3',3,'2025-01-19 16:59:19','123',0,'self-defined','用户自训练声音'),(66,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaomengNeural.mp3',2,'2025-01-21 09:36:01','小萌',1,'zh-CN-XiaomengNeural','这是小萌的声音，音色柔和动听，适合用于各种场景，给人一种温馨亲切的感觉。'),(67,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaomoNeural.mp3',2,'2025-01-21 09:36:01','小墨',1,'zh-CN-XiaomoNeural','小墨的声音富有磁性，清晰有力，在广播、解说等场景中表现出色，能吸引听众的注意力。'),(68,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaoqiuNeural.mp3',2,'2025-01-21 09:36:01','小球',1,'zh-CN-XiaoqiuNeural','小球的声音充满活力，活泼可爱，适合用于儿童类节目、游戏等，为内容增添趣味。'),(69,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaoruiNeural.mp3',2,'2025-01-21 09:36:01','小蕊',1,'zh-CN-XiaoruiNeural','小蕊的声音轻柔悦耳，如同涓涓细流，适用于诗歌朗诵、温馨故事讲述等场景。'),(70,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaoshuangNeural.mp3',2,'2025-01-21 09:36:01','小爽',1,'zh-CN-XiaoshuangNeural','小爽的声音清脆响亮，具有较高的辨识度，适合广告宣传、语音提示等应用。'),(71,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaoxiaoNeural.mp3',2,'2025-01-21 09:36:01','小小',1,'zh-CN-XiaoxiaoNeural','小小的声音充满童趣，天真无邪，是儿童学习、娱乐应用的理想之选。'),(72,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaoxuanNeural.mp3',2,'2025-01-21 09:36:01','小玄',1,'zh-CN-XiaoxuanNeural','小玄的声音沉稳大气，富有感染力，适用于新闻播报、商务讲解等正式场合。'),(73,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaoyanNeural.mp3',2,'2025-01-21 09:36:01','小言',1,'zh-CN-XiaoyanNeural','小言的声音婉转悠扬，情感丰富，适合有声读物、情感类音频的录制。'),(74,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaoyiNeural.mp3',2,'2025-01-21 09:36:01','小艺',1,'zh-CN-XiaoyiNeural','小艺的声音温暖柔和，能给人带来一种舒适的听觉体验，适合作为客服语音。'),(75,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaoyouNeural.mp3',2,'2025-01-21 09:36:01','小右',1,'zh-CN-XiaoyouNeural','小右的声音充满青春活力，朝气蓬勃，适用于运动、时尚类内容的配音。'),(76,0,'http://121.43.62.36:9000/mp3/zh-CN-XiaozhenNeural.mp3',2,'2025-01-21 09:36:01','小真',1,'zh-CN-XiaozhenNeural','小真的声音质朴醇厚，带有一种自然的亲和力，可用于文化、历史类讲解。'),(77,0,'http://121.43.62.36:9000/mp3/zh-CN-YunhaoNeural.mp3',2,'2025-01-21 09:36:01','允浩',1,'zh-CN-YunhaoNeural','允浩的声音雄浑有力，具有权威性，适用于领导讲话、正式报告等场景。'),(78,0,'http://121.43.62.36:9000/mp3/zh-CN-YunxiNeural.mp3',2,'2025-01-21 09:36:01','云熙',1,'zh-CN-YunxiNeural','云熙的声音空灵梦幻，给人一种神秘的感觉，适合科幻、奇幻类内容的配音。'),(79,0,'http://121.43.62.36:9000/mp3/zh-CN-YunyangNeural.mp3',2,'2025-01-21 09:36:01','云杨',1,'zh-CN-YunyangNeural','云杨的声音热情奔放，充满激情，适合用于激励性的演讲、宣传等。'),(80,0,'http://121.43.62.36:9000/mp3/zh-CN-YunyeNeural.mp3',2,'2025-01-21 09:36:01','云野',1,'zh-CN-YunyeNeural','云野的声音舒缓平和，具有安抚人心的效果，可用于冥想、放松类音频的制作。');
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

-- Dump completed on 2025-01-21 19:30:53
