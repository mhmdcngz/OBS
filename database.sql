-- --------------------------------------------------------
-- Sunucu:                       127.0.0.1
-- Sunucu sürümü:                12.1.2-MariaDB - MariaDB Server
-- Sunucu İşletim Sistemi:       Win64
-- HeidiSQL Sürüm:               12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- obs_db için veritabanı yapısı dökülüyor
DROP DATABASE IF EXISTS `obs_db`;
CREATE DATABASE IF NOT EXISTS `obs_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `obs_db`;

-- tablo yapısı dökülüyor obs_db.courses
DROP TABLE IF EXISTS `courses`;
CREATE TABLE IF NOT EXISTS `courses` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `akts` int(11) NOT NULL,
  `teacher_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `teacher_id` (`teacher_id`),
  CONSTRAINT `1` FOREIGN KEY (`teacher_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- obs_db.courses: ~8 rows (yaklaşık) tablosu için veriler indiriliyor
INSERT INTO `courses` (`id`, `name`, `akts`, `teacher_id`) VALUES
	(1, 'Algoritma ve Programlama', 6, 2),
	(2, 'Bilgisayar Bilimlerine Giriş', 5, 3),
	(3, 'Fizik 1', 6, 6),
	(4, 'Matematik 1', 6, 5),
	(5, 'Fizik Lab 1', 2, 8),
	(6, 'İngilizce 1', 2, 7),
	(7, 'Türk Dili 1', 2, 9),
	(8, 'Yazılım Müh. Oryantasyonu', 1, 10);

-- tablo yapısı dökülüyor obs_db.transcripts
DROP TABLE IF EXISTS `transcripts`;
CREATE TABLE IF NOT EXISTS `transcripts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  `midterm_score` int(11) DEFAULT NULL,
  `final_score` int(11) DEFAULT NULL,
  `average` double DEFAULT NULL,
  `is_passed` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_enrollment` (`student_id`,`course_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `1` FOREIGN KEY (`student_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `2` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- obs_db.transcripts: ~9 rows (yaklaşık) tablosu için veriler indiriliyor
INSERT INTO `transcripts` (`id`, `student_id`, `course_id`, `midterm_score`, `final_score`, `average`, `is_passed`) VALUES
	(2, 4, 1, 74, 75, 74.6, 1),
	(3, 4, 2, 55, 53, 53.8, 1),
	(4, 4, 3, 65, 85, 77, 1),
	(7, 4, 4, 70, 55, 61, 1),
	(8, 4, 5, 91, 80, 84.4, 1),
	(9, 4, 6, 50, 55, 53, 1),
	(10, 4, 7, 65, 60, 62, 1),
	(11, 4, 8, 90, 100, 96, 1);

-- tablo yapısı dökülüyor obs_db.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `role_enum` enum('DEKAN','AKADEMISYEN','OGRENCI') NOT NULL,
  `title` varchar(50) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT 1,
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- obs_db.users: ~10 rows (yaklaşık) tablosu için veriler indiriliyor
INSERT INTO `users` (`id`, `username`, `password_hash`, `full_name`, `role_enum`, `title`, `is_active`, `created_at`) VALUES
	(1, 'erenmente', '3434', 'Muhammet Eren Mente', 'DEKAN', 'Prof. Dr.', 1, '2026-02-04 17:42:42'),
	(2, 'omrdgn', '4646', 'Ömer Doğan', 'AKADEMISYEN', 'Prof.', 1, '2026-02-04 20:47:03'),
	(3, 'efeblgnr', '2525', 'Efe Bilginer', 'AKADEMISYEN', 'Prof.', 1, '2026-02-04 21:04:46'),
	(4, 'mhmdcngz', '2323', 'Muhammed Cengiz', 'OGRENCI', 'Öğrenci', 1, '2026-02-04 21:28:18'),
	(5, 'metehan', '5353', 'Metehan Salihoğlu', 'AKADEMISYEN', 'Doç.', 1, '2026-02-04 23:03:02'),
	(6, 'yakup', '2323', 'Yakup Kardeş', 'AKADEMISYEN', 'Dr.', 1, '2026-02-04 23:04:24'),
	(7, 'aleyna', '1212', 'Aleyna Arlı', 'AKADEMISYEN', 'Ar. Gör.', 1, '2026-02-04 23:15:41'),
	(8, 'yunusemre', '6161', 'Yunus Emre Nakkaş', 'AKADEMISYEN', 'Ar. Gör.', 1, '2026-02-04 23:17:20'),
	(9, 'mali', '4646', 'Mehmet Ali Kıraççakalı', 'AKADEMISYEN', 'Ar. Gör.', 1, '2026-02-04 23:21:55'),
	(10, 'furkan', '4646', 'Furkan Durkaç', 'AKADEMISYEN', 'Doç.', 1, '2026-02-04 23:22:17');

-- tetikleyici yapısı dökülüyor obs_db.calculate_grade_before_update
DROP TRIGGER IF EXISTS `calculate_grade_before_update`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER calculate_grade_before_update
BEFORE UPDATE ON transcripts
FOR EACH ROW
BEGIN
    -- Eğer hem vize hem final girilmişse hesapla
    IF NEW.midterm_score IS NOT NULL AND NEW.final_score IS NOT NULL THEN
        -- Formül: Vize %40 + Final %60
        SET NEW.average = (NEW.midterm_score * 0.40) + (NEW.final_score * 0.60);

        -- Geçme Durumu (Baraj: 50)
        IF NEW.average >= 50 THEN
            SET NEW.is_passed = 1; -- GEÇTİ
        ELSE
            SET NEW.is_passed = 0; -- KALDI
        END IF;
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
