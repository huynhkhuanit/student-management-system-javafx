-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: student_system_database
-- ------------------------------------------------------
-- Server version	8.0.40

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
-- Current Database: `student_system_database`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `student_system_database` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `student_system_database`;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'admin','admin123');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `course_id` varchar(15) NOT NULL,
  `course_name` varchar(100) NOT NULL,
  `credits` int NOT NULL,
  `lecturer` varchar(50) DEFAULT NULL,
  `semester` enum('Học kỳ 1','Học kỳ 2','Học kỳ 3') NOT NULL,
  `status` enum('Đang mở','Đóng') NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`course_id`),
  KEY `fk_courses_lecturers` (`lecturer`),
  CONSTRAINT `fk_courses_lecturers` FOREIGN KEY (`lecturer`) REFERENCES `lecturers` (`lecturer_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES ('MH001','Lập trình C#',3,'GV001','Học kỳ 1','Đang mở','2025-02-16 21:15:40','2025-02-16 21:29:22'),('MH002','Lập trình Java',3,'GV002','Học kỳ 1','Đang mở','2025-02-16 21:15:55','2025-02-16 21:21:08'),('MH003','Lập trình game',4,'GV002','Học kỳ 3','Đang mở','2025-02-16 21:42:48','2025-02-16 22:00:42'),('MH004','Lập trình website',3,'GV003','Học kỳ 3','Đang mở','2025-02-16 21:53:59','2025-02-16 21:57:23');
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grades`
--

DROP TABLE IF EXISTS `grades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grades` (
  `id` int NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) NOT NULL,
  `school_year` varchar(20) NOT NULL,
  `course_id` varchar(20) NOT NULL,
  `midterm_grade` float DEFAULT NULL,
  `final_grade` float DEFAULT NULL,
  `total_grade` float GENERATED ALWAYS AS (((`midterm_grade` * 0.4) + (`final_grade` * 0.6))) STORED,
  PRIMARY KEY (`id`),
  KEY `fk_grades_students` (`student_id`),
  KEY `fk_grades_courses` (`course_id`),
  CONSTRAINT `fk_grades_courses` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_grades_students` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `grades_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`) ON DELETE CASCADE,
  CONSTRAINT `grades_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE,
  CONSTRAINT `grades_chk_1` CHECK ((`midterm_grade` between 0 and 10)),
  CONSTRAINT `grades_chk_2` CHECK ((`final_grade` between 0 and 10))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grades`
--

LOCK TABLES `grades` WRITE;
/*!40000 ALTER TABLE `grades` DISABLE KEYS */;
INSERT INTO `grades` (`id`, `student_id`, `school_year`, `course_id`, `midterm_grade`, `final_grade`) VALUES (6,'2205CT0035','Năm 3','MH002',10,10),(7,'2205CT0055','Năm 1','MH004',10,10),(8,'2205CT0091','Năm 3','MH002',10,10);
/*!40000 ALTER TABLE `grades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lecturers`
--

DROP TABLE IF EXISTS `lecturers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lecturers` (
  `lecturer_id` varchar(20) NOT NULL,
  `lecturer_name` varchar(100) NOT NULL,
  `gender` enum('Nam','Nữ') NOT NULL,
  `degree` varchar(50) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `status` enum('Đang giảng dạy','Nghỉ phép','Đã nghỉ hưu') NOT NULL,
  PRIMARY KEY (`lecturer_id`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lecturers`
--

LOCK TABLES `lecturers` WRITE;
/*!40000 ALTER TABLE `lecturers` DISABLE KEYS */;
INSERT INTO `lecturers` VALUES ('GV001','Nguyễn Thanh Tiến','Nam','Thạc sĩ','0123456789','Đang giảng dạy'),('GV002','Nguyễn Văn Dũng','Nam','Tiến sĩ','0943006018','Đang giảng dạy'),('GV003','Mai Đức Trung','Nam','Thạc sĩ','0123556452','Đang giảng dạy');
/*!40000 ALTER TABLE `lecturers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `student_id` varchar(15) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `birth_date` date NOT NULL,
  `gender` enum('Nam','Nữ') NOT NULL,
  `school_year` enum('Năm 1','Năm 2','Năm 3','Năm 4') NOT NULL,
  `major` varchar(100) NOT NULL,
  `subject` varchar(100) NOT NULL,
  `status` enum('Đang học','Bảo lưu','Nghỉ học','Tốt nghiệp') NOT NULL,
  `photo_path` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`student_id`),
  KEY `fk_students_courses` (`subject`),
  CONSTRAINT `fk_students_courses` FOREIGN KEY (`subject`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES ('2205CT0035','Huỳnh','Văn Khuân','2004-06-25','Nam','Năm 3','Công nghệ thông tin','MH002','Đang học','','2025-02-20 19:07:38','2025-02-20 19:07:38'),('2205CT0055','Mai','Trọng Quang','2004-03-25','Nam','Năm 1','Công nghệ thông tin','MH004','Đang học','','2025-02-16 21:43:15','2025-02-20 20:02:04'),('2205CT0091','Nguyễn','Ngọc Quỳnh','2004-09-25','Nữ','Năm 3','Kinh tế','MH002','Đang học','','2025-02-20 19:40:38','2025-02-20 19:51:12');
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_grades_school_year` AFTER UPDATE ON `students` FOR EACH ROW BEGIN
    UPDATE grades
    SET school_year = NEW.school_year
    WHERE student_id = NEW.student_id;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-21  3:36:06
