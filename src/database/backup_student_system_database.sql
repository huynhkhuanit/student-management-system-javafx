-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: student_system_database
-- ------------------------------------------------------
-- Server version	8.0.41

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
INSERT INTO `courses` VALUES ('MH001','Lập trình Java',3,'GV002','Học kỳ 1','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH002','Cơ sở dữ liệu',4,'GV001','Học kỳ 2','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH003','Thiết kế hệ thống',3,'GV001','Học kỳ 2','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH004','Mạng máy tính',3,'GV001','Học kỳ 1','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH005','Kiến trúc phần mềm',3,'GV001','Học kỳ 2','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH006','Lập trình Web',3,'GV001','Học kỳ 1','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH007','Xử lý ảnh và video',3,'GV001','Học kỳ 1','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH008','Phát triển ứng dụng di động',3,'GV001','Học kỳ 2','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH009','Hệ điều hành',3,'GV001','Học kỳ 1','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH010','Quản lý dự án phần mềm',4,'GV001','Học kỳ 2','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH011','Công nghệ Blockchain',3,'GV001','Học kỳ 1','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH012','Khoa học dữ liệu',4,'GV001','Học kỳ 2','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH013','Học máy',3,'GV001','Học kỳ 1','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH014','Trí tuệ nhân tạo',4,'GV001','Học kỳ 2','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19'),('MH015','Thực tập tốt nghiệp',6,'GV001','Học kỳ 2','Đang mở','2025-02-26 05:57:19','2025-02-26 05:57:19');
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grades`
--

LOCK TABLES `grades` WRITE;
/*!40000 ALTER TABLE `grades` DISABLE KEYS */;
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
INSERT INTO `lecturers` VALUES ('GV001','Trần Thị B','Nữ','Tiến sĩ','0123456789','Đang giảng dạy'),('GV002','Nguyễn Văn A','Nam','Tiến sĩ','0943006018','Đang giảng dạy'),('GV003','Lê Thị C','Nữ','Thạc sĩ','0987654321','Đang giảng dạy'),('GV004','Phạm Văn D','Nam','Tiến sĩ','0934567890','Đang giảng dạy'),('GV005','Hoàng Thị E','Nữ','Cử nhân','0912345678','Đang giảng dạy'),('GV006','Vũ Văn F','Nam','Phó giáo sư','0923456789','Đang giảng dạy'),('GV007','Đỗ Thị G','Nữ','Giáo sư','0956789012','Đang giảng dạy'),('GV008','Trần Văn H','Nam','Thạc sĩ','0967890123','Nghỉ phép'),('GV009','Nguyễn Thị I','Nữ','Tiến sĩ','0978901234','Đang giảng dạy'),('GV010','Lê Văn J','Nam','Cử nhân','0989012345','Đang giảng dạy'),('GV011','Phạm Thị K','Nữ','Phó giáo sư','0990123456','Nghỉ phép'),('GV012','Hoàng Văn L','Nam','Giáo sư','0911234567','Đang giảng dạy'),('GV013','Vũ Thị M','Nữ','Thạc sĩ','0922345678','Đã nghỉ hưu'),('GV014','Đỗ Văn N','Nam','Tiến sĩ','0933456789','Đang giảng dạy'),('GV015','Trần Thị O','Nữ','Cử nhân','0944567890','Đang giảng dạy');
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
INSERT INTO `students` VALUES ('2205CT0013','Nguyen','Van A','2004-02-23','Nam','Năm 3','Công nghệ thông tin','MH014','Đang học',NULL,'2025-02-26 06:52:11','2025-02-26 06:52:11'),('2205CT0014','Tran','Thi B','2003-05-14','Nữ','Năm 4','Kinh tế','MH003','Đang học',NULL,'2025-02-26 06:52:11','2025-02-26 06:52:11'),('2205CT0015','Le','Van C','2005-11-29','Nam','Năm 2','Công nghệ thông tin','MH002','Đang học',NULL,'2025-02-26 06:52:11','2025-02-26 06:52:11'),('2205CT0016','Pham','Thi D','2002-07-09','Nữ','Năm 4','Y khoa','MH002','Tốt nghiệp',NULL,'2025-02-26 06:52:11','2025-02-26 06:52:11'),('2205CT0017','Hoang','Van E','2004-03-21','Nam','Năm 3','Kỹ thuật','MH002','Bảo lưu',NULL,'2025-02-26 06:52:11','2025-02-26 06:52:11'),('2205CT0018','Nguyen','Thi F','2005-09-04','Nữ','Năm 2','Công nghệ thông tin','MH001','Đang học',NULL,'2025-02-26 06:52:11','2025-02-26 06:52:11'),('2205CT0019','Tran','Van G','2003-12-11','Nam','Năm 3','Kinh tế','MH002','Đang học',NULL,'2025-02-26 06:52:11','2025-02-26 06:52:11'),('2205CT0020','Le','Thi H','2004-04-17','Nữ','Năm 3','Y khoa','MH002','Nghỉ học',NULL,'2025-02-26 06:52:11','2025-02-26 06:52:11'),('2205CT0021','Pham','Van I','2005-08-24','Nam','Năm 2','Kỹ thuật','MH002','Đang học',NULL,'2025-02-26 06:52:11','2025-02-26 06:52:11');
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

-- Dump completed on 2025-02-26 13:52:53
