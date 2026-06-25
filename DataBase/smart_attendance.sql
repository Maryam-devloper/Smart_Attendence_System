-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: smart_attendance
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `announcements`
--

DROP TABLE IF EXISTS `announcements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `announcements` (
  `announcement_id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(50) DEFAULT NULL,
  `teacher_name` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `body` text,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`announcement_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_post_date` (`post_date`),
  CONSTRAINT `announcements_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announcements`
--

LOCK TABLES `announcements` WRITE;
/*!40000 ALTER TABLE `announcements` DISABLE KEYS */;
INSERT INTO `announcements` VALUES (1,'103','Madiha','Math','Sesional marks updated','i will upload your marks in next semester','2026-05-14 04:58:04');
/*!40000 ALTER TABLE `announcements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assignments` (
  `assignment_id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(50) DEFAULT NULL,
  `teacher_name` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `description` text,
  `due_date` date DEFAULT NULL,
  `file_path` varchar(500) DEFAULT NULL,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`assignment_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_subject` (`subject`),
  KEY `idx_due_date` (`due_date`),
  CONSTRAINT `assignments_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assignments`
--

LOCK TABLES `assignments` WRITE;
/*!40000 ALTER TABLE `assignments` DISABLE KEYS */;
/*!40000 ALTER TABLE `assignments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attendance`
--

DROP TABLE IF EXISTS `attendance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attendance` (
  `attendance_id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(50) NOT NULL,
  `student_name` varchar(100) DEFAULT NULL,
  `department` varchar(100) DEFAULT NULL,
  `subject` varchar(100) NOT NULL,
  `status` enum('Present','Absent','Leave','/') NOT NULL DEFAULT '/',
  `date` date NOT NULL DEFAULT (curdate()),
  `marked_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`attendance_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_date` (`date`),
  KEY `idx_subject` (`subject`),
  KEY `idx_attendance_date_status` (`date`,`status`),
  KEY `idx_attendance_student_date` (`student_id`,`date`),
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attendance`
--

LOCK TABLES `attendance` WRITE;
/*!40000 ALTER TABLE `attendance` DISABLE KEYS */;
INSERT INTO `attendance` VALUES (1,'01','Hamza','Computer Science','Math','Absent','2026-05-13','Sadai'),(2,'01','Hamza','Computer Science','OOP','Leave','2026-05-13','Sadai'),(3,'02','Ayesha','Computer Science','Math','Present','2026-05-14','Sadai'),(4,'01','Hamza','Computer Science','Math','Present','2026-05-14','Sadai'),(5,'02','Ayesha','Computer Science','Math','Present','2026-05-14','Madiha'),(6,'01','Hamza','Computer Science','Math','Absent','2026-05-14','Madiha'),(7,'01','Hamza','Computer Science','Math','Leave','2026-05-15','Madiha');
/*!40000 ALTER TABLE `attendance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `attendance_summary`
--

DROP TABLE IF EXISTS `attendance_summary`;
/*!50001 DROP VIEW IF EXISTS `attendance_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `attendance_summary` AS SELECT 
 1 AS `student_id`,
 1 AS `student_name`,
 1 AS `subject`,
 1 AS `total_classes`,
 1 AS `present_count`,
 1 AS `absent_count`,
 1 AS `leave_count`,
 1 AS `attendance_percentage`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `course_outline`
--

DROP TABLE IF EXISTS `course_outline`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_outline` (
  `outline_id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(50) DEFAULT NULL,
  `teacher_name` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `week_number` int DEFAULT NULL,
  `topic` varchar(200) DEFAULT NULL,
  `description` text,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`outline_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_subject` (`subject`),
  KEY `idx_week_number` (`week_number`),
  CONSTRAINT `course_outline_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `course_outline`
--

LOCK TABLES `course_outline` WRITE;
/*!40000 ALTER TABLE `course_outline` DISABLE KEYS */;
/*!40000 ALTER TABLE `course_outline` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `guidelines`
--

DROP TABLE IF EXISTS `guidelines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `guidelines` (
  `guideline_id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(50) DEFAULT NULL,
  `teacher_name` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `body` text,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`guideline_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_subject` (`subject`),
  CONSTRAINT `guidelines_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `guidelines`
--

LOCK TABLES `guidelines` WRITE;
/*!40000 ALTER TABLE `guidelines` DISABLE KEYS */;
/*!40000 ALTER TABLE `guidelines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leave_requests`
--

DROP TABLE IF EXISTS `leave_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leave_requests` (
  `request_id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(50) NOT NULL,
  `student_name` varchar(100) DEFAULT NULL,
  `teacher_id` varchar(50) DEFAULT NULL,
  `teacher_name` varchar(100) DEFAULT NULL,
  `leave_type` varchar(50) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `body` text,
  `status` enum('Pending','Approved','Rejected') DEFAULT 'Pending',
  `apply_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`request_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_status` (`status`),
  KEY `idx_apply_date` (`apply_date`),
  KEY `idx_leaves_status_date` (`status`,`apply_date`),
  KEY `idx_leaves_teacher_status` (`teacher_id`,`status`),
  CONSTRAINT `leave_requests_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `leave_requests_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leave_requests`
--

LOCK TABLES `leave_requests` WRITE;
/*!40000 ALTER TABLE `leave_requests` DISABLE KEYS */;
INSERT INTO `leave_requests` VALUES (1,'01','Hamza','101','Sadai','Sick Leave','OOP','i am sick','Approved','2026-05-13 16:35:17'),(2,'02','Ayesha','102','Hamza','Sick Leave','Math','i am very sick','Pending','2026-05-14 02:05:38'),(3,'01','Hamza','103','Madiha','Other','Math','headache','Approved','2026-05-14 05:02:27');
/*!40000 ALTER TABLE `leave_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecture_notes`
--

DROP TABLE IF EXISTS `lecture_notes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecture_notes` (
  `note_id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(50) DEFAULT NULL,
  `teacher_name` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `description` text,
  `file_path` varchar(500) DEFAULT NULL,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`note_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_subject` (`subject`),
  KEY `idx_post_date` (`post_date`),
  CONSTRAINT `lecture_notes_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecture_notes`
--

LOCK TABLES `lecture_notes` WRITE;
/*!40000 ALTER TABLE `lecture_notes` DISABLE KEYS */;
/*!40000 ALTER TABLE `lecture_notes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `library_resources`
--

DROP TABLE IF EXISTS `library_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `library_resources` (
  `resource_id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(50) DEFAULT NULL,
  `teacher_name` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `description` text,
  `url` varchar(500) DEFAULT NULL,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`resource_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_subject` (`subject`),
  CONSTRAINT `library_resources_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `library_resources`
--

LOCK TABLES `library_resources` WRITE;
/*!40000 ALTER TABLE `library_resources` DISABLE KEYS */;
/*!40000 ALTER TABLE `library_resources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `miscellaneous`
--

DROP TABLE IF EXISTS `miscellaneous`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `miscellaneous` (
  `misc_id` int NOT NULL AUTO_INCREMENT,
  `teacher_id` varchar(50) DEFAULT NULL,
  `teacher_name` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `body` text,
  `post_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`misc_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_subject` (`subject`),
  CONSTRAINT `miscellaneous_ibfk_1` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miscellaneous`
--

LOCK TABLES `miscellaneous` WRITE;
/*!40000 ALTER TABLE `miscellaneous` DISABLE KEYS */;
/*!40000 ALTER TABLE `miscellaneous` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `pending_leaves`
--

DROP TABLE IF EXISTS `pending_leaves`;
/*!50001 DROP VIEW IF EXISTS `pending_leaves`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `pending_leaves` AS SELECT 
 1 AS `request_id`,
 1 AS `student_id`,
 1 AS `student_name`,
 1 AS `subject`,
 1 AS `leave_type`,
 1 AS `body`,
 1 AS `apply_date`,
 1 AS `teacher_name`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `student_leave_history`
--

DROP TABLE IF EXISTS `student_leave_history`;
/*!50001 DROP VIEW IF EXISTS `student_leave_history`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `student_leave_history` AS SELECT 
 1 AS `request_id`,
 1 AS `student_id`,
 1 AS `student_name`,
 1 AS `leave_type`,
 1 AS `subject`,
 1 AS `body`,
 1 AS `status`,
 1 AS `apply_date`,
 1 AS `teacher_name`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `student_pending_queries`
--

DROP TABLE IF EXISTS `student_pending_queries`;
/*!50001 DROP VIEW IF EXISTS `student_pending_queries`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `student_pending_queries` AS SELECT 
 1 AS `query_id`,
 1 AS `student_id`,
 1 AS `student_name`,
 1 AS `teacher_name`,
 1 AS `subject`,
 1 AS `question`,
 1 AS `ask_date`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `student_queries`
--

DROP TABLE IF EXISTS `student_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student_queries` (
  `query_id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(50) NOT NULL,
  `student_name` varchar(100) DEFAULT NULL,
  `teacher_id` varchar(50) NOT NULL,
  `teacher_name` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `question` text NOT NULL,
  `answer` text,
  `status` enum('Pending','Replied') DEFAULT 'Pending',
  `ask_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`query_id`),
  KEY `idx_student_id` (`student_id`),
  KEY `idx_teacher_id` (`teacher_id`),
  KEY `idx_status` (`status`),
  KEY `idx_ask_date` (`ask_date`),
  KEY `idx_queries_status_date` (`status`,`ask_date`),
  KEY `idx_queries_teacher_status` (`teacher_id`,`status`),
  CONSTRAINT `student_queries_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `student_queries_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_queries`
--

LOCK TABLES `student_queries` WRITE;
/*!40000 ALTER TABLE `student_queries` DISABLE KEYS */;
INSERT INTO `student_queries` VALUES (1,'01','Hamza','101','Sadai','OOP','what is oop?','oop means object oriented programming','Replied','2026-05-13 16:34:52'),(2,'01','Hamza','103','Madiha','Math','what is integration by parts?','u v','Replied','2026-05-14 05:03:18');
/*!40000 ALTER TABLE `student_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `teacher_dashboard`
--

DROP TABLE IF EXISTS `teacher_dashboard`;
/*!50001 DROP VIEW IF EXISTS `teacher_dashboard`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `teacher_dashboard` AS SELECT 
 1 AS `teacher_id`,
 1 AS `teacher_name`,
 1 AS `department`,
 1 AS `subjects`,
 1 AS `pending_leaves`,
 1 AS `pending_queries`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` varchar(50) NOT NULL,
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('Student','Teacher') NOT NULL,
  `department` varchar(100) DEFAULT NULL,
  `semester` varchar(20) DEFAULT NULL,
  `section` varchar(10) DEFAULT NULL,
  `subjects` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_users_role` (`role`),
  KEY `idx_users_name` (`name`),
  KEY `idx_users_department` (`department`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('01','Hamza','123','Student','Computer Science','3','A',NULL,'2026-05-13 16:29:51'),('02','Ayesha','456','Student','Computer Science','1','A',NULL,'2026-05-14 02:04:51'),('101','Sadai','1122','Teacher','Computer Science',NULL,NULL,'Math, OOP, DBMS','2026-05-13 16:31:10'),('102','Hamza','1234','Teacher','Computer Science',NULL,NULL,'Math, OOP, DBMS','2026-05-14 02:03:49'),('103','Madiha','789','Teacher','Computer Science',NULL,NULL,'Math, OOP, DSA, DBMS','2026-05-14 04:53:06');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `attendance_summary`
--

/*!50001 DROP VIEW IF EXISTS `attendance_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `attendance_summary` AS select `a`.`student_id` AS `student_id`,`a`.`student_name` AS `student_name`,`a`.`subject` AS `subject`,count(0) AS `total_classes`,sum((case when (`a`.`status` = 'Present') then 1 else 0 end)) AS `present_count`,sum((case when (`a`.`status` = 'Absent') then 1 else 0 end)) AS `absent_count`,sum((case when (`a`.`status` = 'Leave') then 1 else 0 end)) AS `leave_count`,round(((sum((case when (`a`.`status` = 'Present') then 1 else 0 end)) * 100.0) / count(0)),2) AS `attendance_percentage` from `attendance` `a` group by `a`.`student_id`,`a`.`student_name`,`a`.`subject` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `pending_leaves`
--

/*!50001 DROP VIEW IF EXISTS `pending_leaves`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `pending_leaves` AS select `lr`.`request_id` AS `request_id`,`lr`.`student_id` AS `student_id`,`lr`.`student_name` AS `student_name`,`lr`.`subject` AS `subject`,`lr`.`leave_type` AS `leave_type`,`lr`.`body` AS `body`,`lr`.`apply_date` AS `apply_date`,`lr`.`teacher_name` AS `teacher_name` from `leave_requests` `lr` where (`lr`.`status` = 'Pending') order by `lr`.`apply_date` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `student_leave_history`
--

/*!50001 DROP VIEW IF EXISTS `student_leave_history`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `student_leave_history` AS select `lr`.`request_id` AS `request_id`,`lr`.`student_id` AS `student_id`,`lr`.`student_name` AS `student_name`,`lr`.`leave_type` AS `leave_type`,`lr`.`subject` AS `subject`,`lr`.`body` AS `body`,`lr`.`status` AS `status`,`lr`.`apply_date` AS `apply_date`,`lr`.`teacher_name` AS `teacher_name` from `leave_requests` `lr` order by `lr`.`apply_date` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `student_pending_queries`
--

/*!50001 DROP VIEW IF EXISTS `student_pending_queries`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `student_pending_queries` AS select `sq`.`query_id` AS `query_id`,`sq`.`student_id` AS `student_id`,`sq`.`student_name` AS `student_name`,`sq`.`teacher_name` AS `teacher_name`,`sq`.`subject` AS `subject`,`sq`.`question` AS `question`,`sq`.`ask_date` AS `ask_date` from `student_queries` `sq` where (`sq`.`status` = 'Pending') order by `sq`.`ask_date` desc */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `teacher_dashboard`
--

/*!50001 DROP VIEW IF EXISTS `teacher_dashboard`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `teacher_dashboard` AS select `t`.`id` AS `teacher_id`,`t`.`name` AS `teacher_name`,`t`.`department` AS `department`,`t`.`subjects` AS `subjects`,count(distinct `lr`.`request_id`) AS `pending_leaves`,count(distinct `sq`.`query_id`) AS `pending_queries` from ((`users` `t` left join `leave_requests` `lr` on(((`t`.`id` = `lr`.`teacher_id`) and (`lr`.`status` = 'Pending')))) left join `student_queries` `sq` on(((`t`.`id` = `sq`.`teacher_id`) and (`sq`.`status` = 'Pending')))) where (`t`.`role` = 'Teacher') group by `t`.`id`,`t`.`name`,`t`.`department`,`t`.`subjects` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-25 13:21:01
