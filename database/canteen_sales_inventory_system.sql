-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               8.0.45 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for canteen_sales_inventory_system
DROP DATABASE IF EXISTS `canteen_sales_inventory_system`;
CREATE DATABASE IF NOT EXISTS `canteen_sales_inventory_system` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `canteen_sales_inventory_system`;

-- Dumping structure for table canteen_sales_inventory_system.announcements
DROP TABLE IF EXISTS `announcements`;
CREATE TABLE IF NOT EXISTS `announcements` (
  `announcement_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `body` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_role` enum('ALL','ADMIN','CUSTOMER') COLLATE utf8mb4_unicode_ci DEFAULT 'ALL',
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  `created_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`announcement_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `announcements_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.announcements: ~3 rows (approximately)
INSERT INTO `announcements` (`announcement_id`, `title`, `body`, `target_role`, `status`, `created_by`, `created_at`) VALUES
	(1, 'Welcome to GR 6 Canteen', 'Online pickup ordering is now available for registered customers.', 'ALL', 'ACTIVE', 1, '2026-05-25 15:11:24'),
	(2, 'Welcome', 'Welcome to the Canteen System', 'ALL', 'ACTIVE', 1, '2026-06-11 00:14:31'),
	(3, 'Promo Alert', 'Enjoy student discounts today', 'CUSTOMER', 'ACTIVE', 1, '2026-06-11 00:14:31');

-- Dumping structure for table canteen_sales_inventory_system.audit_logs
DROP TABLE IF EXISTS `audit_logs`;
CREATE TABLE IF NOT EXISTS `audit_logs` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `action` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `details` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `audit_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=650 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.audit_logs: ~411 rows (approximately)
INSERT INTO `audit_logs` (`log_id`, `user_id`, `action`, `details`, `created_at`) VALUES
	(1, 1, 'LOGIN', 'Successful login', '2026-05-25 15:14:08'),
	(2, 3, 'LOGIN', 'Successful login', '2026-05-25 15:15:03'),
	(3, 1, 'LOGIN', 'Successful login', '2026-06-03 18:18:03'),
	(4, 1, 'LOGIN', 'Successful login', '2026-06-03 18:25:21'),
	(5, 3, 'LOGIN', 'Successful login', '2026-06-03 18:27:52'),
	(6, 1, 'LOGIN', 'Successful login', '2026-06-03 18:28:21'),
	(7, 1, 'LOGIN', 'Successful login', '2026-06-08 02:58:11'),
	(8, 1, 'LOGIN', 'Successful login', '2026-06-08 02:59:31'),
	(9, 1, 'LOGIN', 'Successful login', '2026-06-08 03:11:26'),
	(10, 3, 'LOGIN', 'Successful login', '2026-06-08 03:12:10'),
	(11, 3, 'LOGIN', 'Successful login', '2026-06-08 03:12:49'),
	(12, 3, 'LOGIN', 'Successful login', '2026-06-08 03:14:53'),
	(13, 1, 'LOGIN', 'Successful login', '2026-06-08 03:15:04'),
	(14, 1, 'LOGIN', 'Successful login', '2026-06-08 03:22:44'),
	(15, 3, 'LOGIN', 'Successful login', '2026-06-08 03:23:04'),
	(16, 3, 'LOGIN', 'Successful login', '2026-06-08 03:27:36'),
	(17, 3, 'LOGIN', 'Successful login', '2026-06-08 03:28:31'),
	(18, 3, 'LOGIN', 'Successful login', '2026-06-08 03:29:49'),
	(19, 3, 'LOGIN', 'Successful login', '2026-06-08 03:30:55'),
	(20, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 03:31:09'),
	(21, 1, 'LOGIN', 'Successful login', '2026-06-08 03:31:15'),
	(22, 3, 'LOGIN', 'Successful login', '2026-06-08 03:32:32'),
	(23, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 03:32:52'),
	(24, 1, 'LOGIN', 'Successful login', '2026-06-08 03:34:54'),
	(25, 3, 'LOGIN', 'Successful login', '2026-06-08 03:37:58'),
	(26, 3, 'LOGIN', 'Successful login', '2026-06-08 03:44:00'),
	(27, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 03:45:54'),
	(28, 1, 'LOGIN', 'Successful login', '2026-06-08 03:47:28'),
	(29, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 03:47:35'),
	(30, 3, 'LOGIN', 'Successful login', '2026-06-08 03:47:46'),
	(31, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 03:47:49'),
	(32, 1, 'LOGIN', 'Successful login', '2026-06-08 03:55:01'),
	(33, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 03:55:12'),
	(34, 1, 'LOGIN', 'Successful login', '2026-06-08 03:55:58'),
	(35, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 03:58:43'),
	(36, 1, 'LOGIN', 'Successful login', '2026-06-08 03:58:59'),
	(37, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:04:05'),
	(38, 1, 'LOGIN', 'Successful login', '2026-06-08 04:04:18'),
	(39, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:04:38'),
	(40, 1, 'LOGIN', 'Successful login', '2026-06-08 04:05:10'),
	(41, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:05:17'),
	(42, 1, 'LOGIN', 'Successful login', '2026-06-08 04:06:50'),
	(43, 1, 'LOGIN', 'Successful login', '2026-06-08 04:08:56'),
	(44, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:09:05'),
	(45, 3, 'LOGIN', 'Successful login', '2026-06-08 04:09:59'),
	(46, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:10:16'),
	(47, 1, 'LOGIN', 'Successful login', '2026-06-08 04:10:25'),
	(48, 3, 'LOGIN', 'Successful login', '2026-06-08 04:11:32'),
	(49, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:12:22'),
	(50, 1, 'LOGIN', 'Successful login', '2026-06-08 04:12:28'),
	(51, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:12:37'),
	(52, 3, 'LOGIN', 'Successful login', '2026-06-08 04:12:43'),
	(53, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:15:29'),
	(54, 3, 'LOGIN', 'Successful login', '2026-06-08 04:16:12'),
	(55, 1, 'LOGIN', 'Successful login', '2026-06-08 04:17:46'),
	(56, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:17:49'),
	(57, 3, 'LOGIN', 'Successful login', '2026-06-08 04:17:55'),
	(58, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:18:35'),
	(59, 1, 'LOGIN', 'Successful login', '2026-06-08 04:18:54'),
	(60, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:18:56'),
	(61, 3, 'LOGIN', 'Successful login', '2026-06-08 04:19:06'),
	(62, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:19:17'),
	(63, 1, 'LOGIN', 'Successful login', '2026-06-08 04:20:02'),
	(64, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:20:17'),
	(65, 1, 'LOGIN', 'Successful login', '2026-06-08 04:22:00'),
	(66, 1, 'UPDATE_products', 'product_id=6', '2026-06-08 04:22:24'),
	(67, 1, 'UPDATE_products', 'product_id=7', '2026-06-08 04:22:31'),
	(68, 1, 'UPDATE_products', 'product_id=8', '2026-06-08 04:22:37'),
	(69, 1, 'UPDATE_products', 'product_id=9', '2026-06-08 04:22:42'),
	(70, 1, 'UPDATE_products', 'product_id=10', '2026-06-08 04:22:49'),
	(71, 1, 'UPDATE_products', 'product_id=11', '2026-06-08 04:22:57'),
	(72, 1, 'UPDATE_products', 'product_id=12', '2026-06-08 04:23:03'),
	(73, 1, 'UPDATE_products', 'product_id=13', '2026-06-08 04:23:09'),
	(74, 1, 'UPDATE_products', 'product_id=14', '2026-06-08 04:23:13'),
	(75, 1, 'UPDATE_products', 'product_id=15', '2026-06-08 04:23:20'),
	(76, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:25:20'),
	(77, 3, 'LOGIN', 'Successful login', '2026-06-08 04:25:41'),
	(78, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:25:53'),
	(79, 1, 'LOGIN', 'Successful login', '2026-06-08 04:25:59'),
	(80, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:26:24'),
	(81, 1, 'LOGIN', 'Successful login', '2026-06-08 04:33:02'),
	(82, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:33:53'),
	(83, 3, 'LOGIN', 'Successful login', '2026-06-08 04:34:05'),
	(84, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:34:49'),
	(85, 3, 'LOGIN', 'Successful login', '2026-06-08 04:35:31'),
	(86, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:37:28'),
	(87, 3, 'LOGIN', 'Successful login', '2026-06-08 04:37:44'),
	(88, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:39:46'),
	(89, 3, 'LOGIN', 'Successful login', '2026-06-08 04:40:20'),
	(90, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:41:30'),
	(91, 3, 'LOGIN', 'Successful login', '2026-06-08 04:42:10'),
	(92, 1, 'LOGIN', 'Successful login', '2026-06-08 04:43:25'),
	(93, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:43:46'),
	(94, 1, 'LOGIN', 'Successful login', '2026-06-08 04:44:38'),
	(95, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:44:41'),
	(96, 1, 'LOGIN', 'Successful login', '2026-06-08 04:51:52'),
	(97, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:52:00'),
	(98, 3, 'LOGIN', 'Successful login', '2026-06-08 04:52:05'),
	(99, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:52:27'),
	(100, 3, 'LOGIN', 'Successful login', '2026-06-08 04:52:44'),
	(101, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:52:50'),
	(102, 3, 'LOGIN', 'Successful login', '2026-06-08 04:53:45'),
	(103, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:53:58'),
	(104, 3, 'LOGIN', 'Successful login', '2026-06-08 04:54:28'),
	(105, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-08 04:54:38'),
	(106, 1, 'LOGIN', 'Successful login', '2026-06-08 05:00:43'),
	(107, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 05:00:50'),
	(108, 1, 'LOGIN', 'Successful login', '2026-06-11 00:05:25'),
	(109, 1, 'LOGIN', 'Successful login', '2026-06-11 00:17:14'),
	(110, 1, 'UPDATE_suppliers', 'supplier_id=1', '2026-06-11 00:17:56'),
	(111, 1, 'UPDATE_suppliers', 'supplier_id=2', '2026-06-11 00:18:03'),
	(112, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-11 00:18:16'),
	(113, 1, 'LOGIN', 'Successful login', '2026-06-11 00:18:25'),
	(114, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-11 00:18:34'),
	(115, 1, 'LOGIN', 'Successful login', '2026-06-11 00:18:46'),
	(116, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-11 00:20:43'),
	(117, 3, 'LOGIN', 'Successful login', '2026-06-11 00:27:04'),
	(118, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-11 00:27:22'),
	(119, 1, 'LOGIN', 'Successful login', '2026-06-11 00:27:27'),
	(120, 1, 'LOGIN', 'Successful login', '2026-06-11 00:30:08'),
	(121, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-11 00:32:17'),
	(122, 1, 'LOGIN', 'Successful login', '2026-06-11 00:38:40'),
	(123, 1, 'LOGIN', 'Successful login', '2026-06-11 00:41:39'),
	(124, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-11 00:41:44'),
	(125, 1, 'LOGIN', 'Successful login', '2026-06-11 00:42:16'),
	(126, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-11 00:42:24'),
	(127, 1, 'LOGIN', 'Successful login', '2026-06-11 00:44:03'),
	(128, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-11 00:44:21'),
	(129, 1, 'LOGIN', 'Successful login', '2026-06-14 09:45:28'),
	(130, 1, 'KITCHEN_START_PREP', '702', '2026-06-14 09:46:00'),
	(131, 1, 'KITCHEN_START_PREP', '702', '2026-06-14 09:46:03'),
	(132, 1, 'KITCHEN_START_PREP', '901', '2026-06-14 09:46:07'),
	(133, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-14 09:46:16'),
	(134, 9, 'LOGIN', 'Successful login', '2026-06-14 09:58:30'),
	(135, 9, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-14 09:59:31'),
	(136, 1, 'LOGIN', 'Successful login', '2026-06-14 10:00:05'),
	(137, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-14 10:00:40'),
	(138, 9, 'LOGIN', 'Successful login', '2026-06-14 10:06:04'),
	(139, 9, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-14 10:06:28'),
	(140, 3, 'LOGIN', 'Successful login', '2026-06-14 10:06:56'),
	(141, 9, 'LOGIN', 'Successful login', '2026-06-14 10:09:36'),
	(142, 9, 'CHANGE_PASSWORD', 'Password changed', '2026-06-14 10:11:01'),
	(143, 9, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-14 10:11:07'),
	(144, 9, 'LOGIN', 'Successful login', '2026-06-14 10:11:16'),
	(145, 9, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-14 10:11:52'),
	(146, 3, 'LOGIN', 'Successful login', '2026-06-14 10:28:17'),
	(147, 3, 'LOGIN', 'Successful login', '2026-06-14 10:31:06'),
	(148, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-14 10:35:06'),
	(149, 3, 'LOGIN', 'Successful login', '2026-06-14 10:37:27'),
	(150, 3, 'LOGIN', 'Successful login', '2026-06-14 10:41:52'),
	(151, 3, 'WALLET_PAYMENT', 'Order ID 905', '2026-06-14 10:41:58'),
	(152, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-14 10:43:31'),
	(153, 3, 'LOGIN', 'Successful login', '2026-06-14 10:43:49'),
	(154, 3, 'CANCEL_ORDER', 'Order ID: 902', '2026-06-14 10:43:58'),
	(155, 3, 'CANCEL_ORDER', 'Order ID: 801', '2026-06-14 10:44:04'),
	(156, 3, 'CANCEL_ORDER', 'Order ID: 903', '2026-06-14 10:44:17'),
	(157, 3, 'CANCEL_ORDER', 'Order ID: 904', '2026-06-14 10:44:19'),
	(158, 9, 'LOGIN', 'Successful login', '2026-06-14 10:48:01'),
	(159, 9, 'SUBMIT_PAYMENT', 'Order ID: 906', '2026-06-14 10:48:48'),
	(160, 9, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-14 10:49:00'),
	(161, 1, 'LOGIN', 'Successful login', '2026-06-14 10:49:04'),
	(162, 1, 'VERIFY_PAYMENT', 'Processed Payment ID: 4', '2026-06-14 10:49:19'),
	(163, 1, 'KITCHEN_START_PREP', '906', '2026-06-14 10:49:49'),
	(164, 1, 'KITCHEN_START_PREP', '906', '2026-06-14 10:49:54'),
	(165, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-14 10:52:59'),
	(166, 1, 'LOGIN', 'Successful login', '2026-06-14 10:53:17'),
	(167, 1, 'KITCHEN_SERVE_COMPLETE', '906', '2026-06-14 10:55:21'),
	(168, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-14 10:57:58'),
	(169, 9, 'LOGIN', 'Successful login', '2026-06-14 10:58:09'),
	(170, 9, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-14 11:00:01'),
	(171, 3, 'LOGIN', 'Successful login', '2026-06-15 02:32:45'),
	(172, 3, 'SUBMIT_PAYMENT', 'Order ID: 907', '2026-06-15 02:34:55'),
	(173, 3, 'SUBMIT_PAYMENT', 'Order ID: 908', '2026-06-15 02:35:10'),
	(174, 3, 'SEND_MESSAGE', 'TEST', '2026-06-15 02:35:37'),
	(175, 1, 'LOGIN', 'Successful login', '2026-06-15 02:35:51'),
	(176, 3, 'SUBMIT_PAYMENT', 'Order ID: 909', '2026-06-15 02:36:28'),
	(177, 3, 'SEND_MESSAGE', 'TEST', '2026-06-15 02:38:11'),
	(178, 1, 'VERIFY_PAYMENT', 'Processed Payment ID: 7', '2026-06-15 02:39:56'),
	(179, 1, 'VERIFY_PAYMENT', 'Processed Payment ID: 6', '2026-06-15 02:39:59'),
	(180, 1, 'KITCHEN_START_PREP', '905', '2026-06-15 02:40:32'),
	(181, 1, 'KITCHEN_START_PREP', '908', '2026-06-15 02:40:39'),
	(182, 1, 'KITCHEN_START_PREP', '907', '2026-06-15 02:40:40'),
	(183, 1, 'KITCHEN_START_PREP', '909', '2026-06-15 02:40:42'),
	(184, 1, 'KITCHEN_START_PREP', '905', '2026-06-15 02:43:36'),
	(185, 1, 'KITCHEN_START_PREP', '907', '2026-06-15 02:43:38'),
	(186, 1, 'KITCHEN_SERVE_COMPLETE', '901', '2026-06-15 02:43:47'),
	(187, 1, 'KITCHEN_MARK_READY', '702', '2026-06-15 02:43:58'),
	(188, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-15 02:47:37'),
	(189, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-15 02:47:41'),
	(190, 3, 'LOGIN', 'Successful login', '2026-06-15 02:48:15'),
	(191, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-15 02:48:20'),
	(192, 1, 'LOGIN', 'Successful login', '2026-06-15 02:48:25'),
	(193, 3, 'LOGIN', 'Successful login', '2026-06-15 02:49:06'),
	(194, 1, 'SEND_MESSAGE', 'Reply to user 3: Re: TEST', '2026-06-15 02:49:15'),
	(195, 3, 'LOGIN', 'Successful login', '2026-06-15 02:58:53'),
	(196, 1, 'LOGIN', 'Successful login', '2026-06-15 02:59:03'),
	(197, 1, 'SEND_MESSAGE', 'Reply to user 3: Re: TEST', '2026-06-15 02:59:21'),
	(198, 1, 'KITCHEN_MARK_READY', '908', '2026-06-15 03:00:05'),
	(199, 1, 'KITCHEN_SERVE_COMPLETE', '907', '2026-06-15 03:00:22'),
	(200, 1, 'KITCHEN_SERVE_COMPLETE', '905', '2026-06-15 03:00:26'),
	(201, 1, 'KITCHEN_SERVE_COMPLETE', '909', '2026-06-15 03:00:33'),
	(202, 3, 'SUBMIT_FEEDBACK', 'Order ID 909', '2026-06-15 03:01:11'),
	(203, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-15 03:01:30'),
	(204, 3, 'LOGIN', 'Successful login', '2026-06-15 03:07:50'),
	(205, 1, 'LOGIN', 'Successful login', '2026-06-15 03:08:25'),
	(206, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-15 03:09:27'),
	(207, 1, 'LOGIN', 'Successful login', '2026-06-16 08:58:13'),
	(208, 3, 'LOGIN', 'Successful login', '2026-06-16 12:28:09'),
	(209, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences', '2026-06-16 12:30:04'),
	(210, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences', '2026-06-16 12:30:07'),
	(211, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-16 12:31:30'),
	(212, 3, 'LOGIN', 'Successful login', '2026-06-16 12:31:35'),
	(213, 3, 'LOGIN', 'Successful login', '2026-06-16 12:49:32'),
	(214, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences', '2026-06-16 12:56:24'),
	(215, 3, 'LOGIN', 'Successful login', '2026-06-16 13:03:20'),
	(216, 3, 'UPDATE_PROFILE', 'Updated profile and nutrition preferences', '2026-06-16 13:03:35'),
	(217, 3, 'UPDATE_PROFILE', 'Updated profile and nutrition preferences', '2026-06-16 13:03:56'),
	(218, 3, 'UPDATE_PROFILE', 'Updated profile and nutrition preferences', '2026-06-16 13:05:20'),
	(219, 3, 'UPDATE_PROFILE', 'Updated profile and nutrition preferences', '2026-06-16 13:05:29'),
	(220, 3, 'UPDATE_PROFILE', 'Updated profile and nutrition preferences', '2026-06-16 13:05:38'),
	(221, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-16 13:05:57'),
	(222, 1, 'LOGIN', 'Successful login', '2026-06-16 13:06:08'),
	(223, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-16 13:07:24'),
	(224, 3, 'LOGIN', 'Successful login', '2026-06-16 13:09:40'),
	(225, 1, 'LOGIN', 'Successful login', '2026-06-16 13:10:01'),
	(226, 1, 'LOGIN', 'Successful login', '2026-06-16 13:17:23'),
	(227, 1, 'UPDATE_ingredients', 'ingredient_id=6', '2026-06-16 13:17:37'),
	(228, 3, 'LOGIN', 'Successful login', '2026-06-16 13:18:21'),
	(229, 3, 'UPDATE_PROFILE', 'Updated profile and nutrition preferences', '2026-06-16 13:18:28'),
	(230, 1, 'LOGIN', 'Successful login', '2026-06-16 13:57:46'),
	(231, 1, 'LOGOUT', 'Admin/Staff logged out', '2026-06-16 13:57:58'),
	(232, 1, 'LOGIN', 'Successful login', '2026-06-16 14:06:47'),
	(233, 1, 'LOGOUT', 'Admin/Staff logged out', '2026-06-16 14:09:44'),
	(234, 1, 'LOGIN', 'Successful login', '2026-06-16 14:09:49'),
	(235, 1, 'LOGOUT', 'Admin/Staff logged out', '2026-06-16 14:10:00'),
	(236, 1, 'LOGIN', 'Successful login', '2026-06-16 14:11:24'),
	(237, 1, 'LOGOUT', 'Admin/Staff logged out', '2026-06-16 14:11:33'),
	(238, 1, 'LOGIN', 'Successful login', '2026-06-17 14:28:28'),
	(239, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-17 14:28:40'),
	(240, 3, 'LOGIN', 'Successful login', '2026-06-17 14:28:45'),
	(241, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-17 14:32:59'),
	(242, 1, 'LOGIN', 'Successful login', '2026-06-18 13:30:36'),
	(243, 1, 'LOGIN', 'Successful login', '2026-06-18 13:48:01'),
	(244, 1, 'LOGIN', 'Successful login', '2026-06-18 13:53:59'),
	(245, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-18 13:55:29'),
	(246, 3, 'LOGIN', 'Successful login', '2026-06-18 13:55:35'),
	(247, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-18 13:56:02'),
	(248, 3, 'LOGIN', 'Successful login', '2026-06-18 13:56:33'),
	(249, 1, 'LOGIN', 'Successful login', '2026-06-18 13:58:03'),
	(250, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-18 13:59:29'),
	(251, 9, 'LOGIN', 'Successful login', '2026-06-18 13:59:37'),
	(252, 9, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-18 13:59:43'),
	(253, 1, 'LOGIN', 'Successful login', '2026-06-18 14:19:57'),
	(254, 1, 'LOGOUT', 'System Manager logged out', '2026-06-18 14:20:43'),
	(255, 3, 'LOGIN', 'Successful login', '2026-06-18 14:20:51'),
	(256, 3, 'LOGIN', 'Successful login', '2026-06-18 14:23:01'),
	(257, 3, 'LOGIN', 'Successful login', '2026-06-18 14:27:16'),
	(258, 3, 'LOGIN', 'Successful login', '2026-06-18 14:30:07'),
	(259, 3, 'LOGIN', 'Successful login', '2026-06-18 14:43:38'),
	(260, 3, 'LOGIN', 'Successful login', '2026-06-18 14:49:57'),
	(261, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-18 14:51:20'),
	(262, 3, 'LOGIN', 'Successful login', '2026-06-18 14:57:49'),
	(263, 3, 'LOGIN', 'Successful login', '2026-06-18 15:00:20'),
	(264, 3, 'LOGIN', 'Successful login', '2026-06-18 15:02:40'),
	(265, 3, 'LOGIN', 'Successful login', '2026-06-18 15:04:13'),
	(266, 3, 'LOGIN', 'Successful login', '2026-06-18 15:04:55'),
	(267, 3, 'LOGIN', 'Successful login', '2026-06-18 15:05:52'),
	(268, 3, 'LOGIN', 'Successful login', '2026-06-18 15:06:34'),
	(269, 3, 'LOGIN', 'Successful login', '2026-06-18 15:09:09'),
	(270, 3, 'LOGIN', 'Successful login', '2026-06-18 15:11:07'),
	(271, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-18 15:14:10'),
	(272, 1, 'LOGIN', 'Successful login', '2026-06-18 15:14:22'),
	(273, 3, 'LOGIN', 'Successful login', '2026-06-18 15:15:32'),
	(274, 3, 'LOGIN', 'Successful login', '2026-06-18 15:23:45'),
	(275, 3, 'LOGIN', 'Successful login', '2026-06-18 15:26:39'),
	(276, 3, 'LOGIN', 'Successful login', '2026-06-18 15:29:42'),
	(277, 3, 'LOGIN', 'Successful login', '2026-06-18 15:31:54'),
	(278, 3, 'LOGIN', 'Successful login', '2026-06-18 15:32:33'),
	(279, 3, 'LOGIN', 'Successful login', '2026-06-18 15:33:06'),
	(280, 3, 'LOGIN', 'Successful login', '2026-06-18 15:33:41'),
	(281, 3, 'LOGIN', 'Successful login', '2026-06-18 15:38:17'),
	(282, 3, 'LOGIN', 'Successful login', '2026-06-18 15:40:31'),
	(283, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-18 15:40:38'),
	(284, 3, 'LOGIN', 'Successful login', '2026-06-18 15:42:37'),
	(285, 3, 'LOGIN', 'Successful login', '2026-06-18 15:44:11'),
	(286, 3, 'LOGIN', 'Successful login', '2026-06-18 15:45:29'),
	(287, 3, 'LOGIN', 'Successful login', '2026-06-18 15:46:03'),
	(288, 3, 'LOGIN', 'Successful login', '2026-06-18 15:50:43'),
	(289, 3, 'LOGIN', 'Successful login', '2026-06-18 15:52:47'),
	(290, 3, 'LOGIN', 'Successful login', '2026-06-18 15:54:00'),
	(291, 3, 'LOGIN', 'Successful login', '2026-06-18 15:55:29'),
	(292, 3, 'LOGIN', 'Successful login', '2026-06-18 16:00:22'),
	(293, 3, 'LOGIN', 'Successful login', '2026-06-18 16:03:44'),
	(294, 3, 'LOGIN', 'Successful login', '2026-06-18 17:08:44'),
	(295, 3, 'LOGIN', 'Successful login', '2026-06-18 17:11:24'),
	(296, 3, 'LOGIN', 'Successful login', '2026-06-18 17:14:48'),
	(297, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-18 17:15:46'),
	(298, 1, 'LOGIN', 'Successful login', '2026-06-18 17:15:53'),
	(299, 1, 'LOGIN', 'Successful login', '2026-06-19 14:20:40'),
	(300, 1, 'LOGIN', 'Successful login', '2026-06-19 14:23:47'),
	(301, 1, 'LOGIN', 'Successful login', '2026-06-19 14:27:50'),
	(302, 1, 'LOGOUT', 'System Manager logged out', '2026-06-19 14:28:52'),
	(303, 1, 'LOGIN', 'Successful login', '2026-06-19 14:29:56'),
	(304, 1, 'LOGIN', 'Successful login', '2026-06-19 14:30:45'),
	(305, 1, 'LOGIN', 'Successful login', '2026-06-19 14:33:10'),
	(306, 1, 'LOGOUT', 'System Manager logged out', '2026-06-19 14:33:48'),
	(307, 1, 'LOGIN', 'Successful login', '2026-06-19 14:34:15'),
	(308, 1, 'LOGIN', 'Successful login', '2026-06-19 14:36:30'),
	(309, 1, 'LOGIN', 'Successful login', '2026-06-19 14:39:22'),
	(310, 1, 'LOGOUT', 'System Manager logged out', '2026-06-19 14:39:29'),
	(311, 1, 'LOGIN', 'Successful login', '2026-06-19 14:39:37'),
	(312, 1, 'LOGIN', 'Successful login', '2026-06-20 03:53:16'),
	(313, 1, 'LOGOUT', 'System Manager logged out', '2026-06-20 04:00:50'),
	(314, 3, 'LOGIN', 'Successful login', '2026-06-20 04:00:58'),
	(315, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences', '2026-06-20 04:01:09'),
	(316, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences', '2026-06-20 04:01:44'),
	(317, 1, 'LOGIN', 'Successful login', '2026-06-20 04:02:21'),
	(318, 1, 'LOGOUT', 'System Manager logged out', '2026-06-20 04:03:33'),
	(319, 1, 'LOGIN', 'Successful login', '2026-06-20 04:03:52'),
	(320, 1, 'UPDATE_wallets', 'wallet_id=1', '2026-06-20 04:04:31'),
	(321, 1, 'LOGIN', 'Successful login', '2026-06-20 04:11:52'),
	(322, 1, 'RESET_PASSWORD', '7', '2026-06-20 04:12:13'),
	(323, 7, 'LOGIN', 'Successful login', '2026-06-20 04:12:38'),
	(324, 1, 'UPDATE_wallets', 'wallet_id=1', '2026-06-20 04:13:44'),
	(325, 7, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-20 04:17:38'),
	(326, 1, 'LOGIN', 'Successful login', '2026-06-20 04:17:43'),
	(327, 1, 'LOGIN', 'Successful login', '2026-06-20 04:33:17'),
	(328, 1, 'LOGOUT', 'System Manager logged out', '2026-06-20 04:36:23'),
	(329, 3, 'LOGIN', 'Successful login', '2026-06-20 04:36:30'),
	(330, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-20 04:36:42'),
	(331, 1, 'LOGIN', 'Successful login', '2026-06-20 04:37:22'),
	(332, 1, 'LOGIN', 'Successful login', '2026-06-20 04:40:50'),
	(333, 1, 'UPDATE_ingredients', 'ingredient_id=3', '2026-06-20 04:41:34'),
	(334, 1, 'UPDATE_ingredients', 'ingredient_id=7', '2026-06-20 04:41:44'),
	(335, 1, 'UPDATE_ingredients', 'ingredient_id=13', '2026-06-20 04:41:57'),
	(336, 1, 'LOGIN', 'Successful login', '2026-06-20 04:49:21'),
	(337, 1, 'UPDATE_employees', 'employee_id=2', '2026-06-20 04:50:14'),
	(338, 1, 'ADD_employees', 'New record', '2026-06-20 04:51:12'),
	(339, 1, 'ACCOUNT_INACTIVE', '10', '2026-06-20 04:52:15'),
	(340, 1, 'ACCOUNT_INACTIVE', '9', '2026-06-20 04:52:22'),
	(341, 1, 'UPDATE_customer_profiles', 'customer_id=4', '2026-06-20 04:54:24'),
	(342, 1, 'UPDATE_customer_profiles', 'customer_id=1', '2026-06-20 04:54:31'),
	(343, 1, 'UPDATE_ingredients', 'ingredient_id=4', '2026-06-20 04:56:40'),
	(344, 1, 'UPDATE_ingredients', 'ingredient_id=5', '2026-06-20 04:57:20'),
	(345, 1, 'UPDATE_ingredients', 'ingredient_id=9', '2026-06-20 04:57:41'),
	(346, 1, 'UPDATE_products', 'product_id=1', '2026-06-20 04:58:07'),
	(347, 1, 'UPDATE_products', 'product_id=2', '2026-06-20 04:58:14'),
	(348, 1, 'UPDATE_products', 'product_id=6', '2026-06-20 04:58:24'),
	(349, 1, 'UPDATE_ingredients', 'ingredient_id=8', '2026-06-20 04:58:47'),
	(350, 1, 'UPDATE_ingredients', 'ingredient_id=11', '2026-06-20 04:58:51'),
	(351, 1, 'LOGIN', 'Successful login', '2026-06-20 05:07:07'),
	(352, 1, 'LOGIN', 'Successful login', '2026-06-20 05:08:12'),
	(353, 1, 'LOGIN', 'Successful login', '2026-06-20 05:09:06'),
	(354, 1, 'LOGIN', 'Successful login', '2026-06-20 05:10:02'),
	(355, 1, 'LOGIN', 'Successful login', '2026-06-20 05:27:08'),
	(356, 1, 'ADD_RECIPE', 'Product ID: 12, Ing ID: 5', '2026-06-20 05:28:53'),
	(357, 1, 'ADD_RECIPE', 'Product ID: 12, Ing ID: 6', '2026-06-20 05:29:19'),
	(358, 1, 'ADD_RECIPE', 'Product ID: 22, Ing ID: 14', '2026-06-20 05:29:33'),
	(359, 1, 'ADD_RECIPE', 'Product ID: 18, Ing ID: 11', '2026-06-20 05:30:20'),
	(360, 1, 'LOGIN', 'Successful login', '2026-06-20 05:37:39'),
	(361, 1, 'ADD_ingredients', 'New record', '2026-06-20 05:38:46'),
	(362, 1, 'LOGOUT', 'System Manager logged out', '2026-06-20 05:39:48'),
	(363, 3, 'LOGIN', 'Successful login', '2026-06-20 05:39:54'),
	(364, 3, 'LOGIN', 'Successful login', '2026-06-20 05:43:24'),
	(365, 3, 'LOGIN', 'Successful login', '2026-06-20 05:44:14'),
	(366, 3, 'LOGIN', 'Successful login', '2026-06-20 05:48:38'),
	(367, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-20 05:49:10'),
	(368, 1, 'LOGIN', 'Successful login', '2026-06-20 05:49:16'),
	(369, 1, 'ACCOUNT_ACTIVE', '10', '2026-06-20 05:49:27'),
	(370, 1, 'LOGOUT', 'System Manager logged out', '2026-06-20 05:49:30'),
	(371, 1, 'LOGIN', 'Successful login', '2026-06-20 06:00:30'),
	(372, 10, 'LOGIN', 'Successful login', '2026-06-20 06:00:39'),
	(373, 10, 'UPDATE_PROFILE', 'Updated profile nutrition preferences', '2026-06-20 06:01:23'),
	(374, 1, 'ADD_wallets', 'New record', '2026-06-20 06:04:24'),
	(375, 10, 'CANCEL_ORDER', 'Order ID: 910', '2026-06-20 06:05:13'),
	(376, 1, 'KITCHEN_START_PREP', '911', '2026-06-20 06:08:17'),
	(377, 1, 'KITCHEN_MARK_READY', '911', '2026-06-20 06:08:21'),
	(378, 1, 'KITCHEN_SERVE_COMPLETE', '911', '2026-06-20 06:08:59'),
	(379, 1, 'KITCHEN_START_PREP', '925', '2026-06-20 06:12:09'),
	(380, 1, 'KITCHEN_START_PREP', '924', '2026-06-20 06:12:11'),
	(381, 1, 'KITCHEN_START_PREP', '921', '2026-06-20 06:12:12'),
	(382, 1, 'KITCHEN_START_PREP', '922', '2026-06-20 06:12:13'),
	(383, 1, 'KITCHEN_MARK_READY', '922', '2026-06-20 06:12:13'),
	(384, 1, 'KITCHEN_MARK_READY', '923', '2026-06-20 06:12:15'),
	(385, 1, 'KITCHEN_MARK_READY', '919', '2026-06-20 06:12:16'),
	(386, 1, 'KITCHEN_MARK_READY', '918', '2026-06-20 06:12:17'),
	(387, 1, 'KITCHEN_MARK_READY', '920', '2026-06-20 06:12:18'),
	(388, 1, 'KITCHEN_MARK_READY', '917', '2026-06-20 06:12:19'),
	(389, 1, 'KITCHEN_MARK_READY', '915', '2026-06-20 06:12:20'),
	(390, 1, 'KITCHEN_MARK_READY', '916', '2026-06-20 06:12:20'),
	(391, 1, 'KITCHEN_MARK_READY', '914', '2026-06-20 06:12:21'),
	(392, 1, 'KITCHEN_MARK_READY', '921', '2026-06-20 06:12:23'),
	(393, 1, 'KITCHEN_MARK_READY', '913', '2026-06-20 06:12:24'),
	(394, 1, 'KITCHEN_MARK_READY', '924', '2026-06-20 06:12:25'),
	(395, 1, 'KITCHEN_MARK_READY', '925', '2026-06-20 06:12:26'),
	(396, 1, 'KITCHEN_MARK_READY', '912', '2026-06-20 06:12:27'),
	(397, 1, 'KITCHEN_SERVE_COMPLETE', '915', '2026-06-20 06:12:29'),
	(398, 1, 'KITCHEN_SERVE_COMPLETE', '914', '2026-06-20 06:12:30'),
	(399, 1, 'KITCHEN_SERVE_COMPLETE', '916', '2026-06-20 06:12:30'),
	(400, 1, 'KITCHEN_SERVE_COMPLETE', '920', '2026-06-20 06:12:31'),
	(401, 1, 'KITCHEN_SERVE_COMPLETE', '917', '2026-06-20 06:12:32'),
	(402, 1, 'KITCHEN_SERVE_COMPLETE', '919', '2026-06-20 06:12:32'),
	(403, 1, 'KITCHEN_SERVE_COMPLETE', '923', '2026-06-20 06:12:33'),
	(404, 1, 'KITCHEN_SERVE_COMPLETE', '922', '2026-06-20 06:12:34'),
	(405, 1, 'KITCHEN_SERVE_COMPLETE', '918', '2026-06-20 06:12:35'),
	(406, 1, 'KITCHEN_SERVE_COMPLETE', '702', '2026-06-20 06:12:35'),
	(407, 1, 'KITCHEN_SERVE_COMPLETE', '908', '2026-06-20 06:12:37'),
	(408, 1, 'KITCHEN_SERVE_COMPLETE', '913', '2026-06-20 06:12:38'),
	(409, 1, 'KITCHEN_SERVE_COMPLETE', '925', '2026-06-20 06:12:38'),
	(410, 1, 'KITCHEN_SERVE_COMPLETE', '924', '2026-06-20 06:12:39'),
	(411, 1, 'KITCHEN_SERVE_COMPLETE', '921', '2026-06-20 06:12:40'),
	(412, 1, 'KITCHEN_SERVE_COMPLETE', '912', '2026-06-20 06:12:41'),
	(413, 1, 'LOGIN', 'Successful login', '2026-06-20 06:56:56'),
	(414, 10, 'LOGIN', 'Successful login', '2026-06-20 06:57:21'),
	(415, 10, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-20 06:58:10'),
	(416, 1, 'LOGOUT', 'System Manager logged out', '2026-06-20 06:59:13'),
	(417, 1, 'LOGIN', 'Successful login', '2026-06-21 06:42:51'),
	(418, 1, 'LOGOUT', 'System Manager logged out', '2026-06-21 06:43:38'),
	(419, 1, 'LOGIN', 'Successful login', '2026-06-22 09:53:48'),
	(420, 1, 'LOGOUT', 'System Manager logged out', '2026-06-22 09:54:08'),
	(421, 3, 'LOGIN', 'Successful login', '2026-06-22 09:54:14'),
	(422, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-22 09:54:22'),
	(423, 1, 'LOGIN', 'Successful login', '2026-06-23 20:57:50'),
	(424, 1, 'LOGOUT', 'System Manager logged out', '2026-06-23 20:58:00'),
	(425, 3, 'LOGIN', 'Successful login', '2026-06-23 20:58:05'),
	(426, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-23 21:02:50'),
	(427, 1, 'LOGIN', 'Successful login', '2026-06-23 21:06:26'),
	(428, 1, 'LOGIN', 'Successful login', '2026-06-23 21:24:15'),
	(429, 1, 'LOGOUT', 'System Manager logged out', '2026-06-23 21:24:37'),
	(430, 3, 'LOGIN', 'Successful login', '2026-06-23 21:24:42'),
	(431, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-23 21:25:13'),
	(432, 1, 'LOGIN', 'Successful login', '2026-06-23 21:25:17'),
	(433, 1, 'LOGOUT', 'System Manager logged out', '2026-06-23 21:25:28'),
	(434, 1, 'LOGIN', 'Successful login', '2026-06-23 21:27:48'),
	(435, 1, 'ALLOCATE_SUB', 'Customer ID: 3', '2026-06-23 21:28:04'),
	(436, 1, 'LOGOUT', 'System Manager logged out', '2026-06-23 21:28:08'),
	(437, 3, 'LOGIN', 'Successful login', '2026-06-23 21:28:14'),
	(438, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-23 21:28:34'),
	(439, 1, 'LOGIN', 'Successful login', '2026-06-23 21:28:39'),
	(440, 1, 'UPDATE_SUB', 'Customer ID: 3', '2026-06-23 21:28:53'),
	(441, 1, 'UPDATE_SUB', 'Customer ID: 1', '2026-06-23 21:29:05'),
	(442, 1, 'ALLOCATE_SUB', 'Customer ID: 3', '2026-06-23 21:29:26'),
	(443, 1, 'UPDATE_SUB', 'Customer ID: 3', '2026-06-23 21:29:30'),
	(444, 1, 'REVOKE_SUB', 'Sub ID: 3', '2026-06-23 21:29:35'),
	(445, 1, 'REVOKE_SUB', 'Sub ID: 4', '2026-06-23 21:29:38'),
	(446, 3, 'LOGIN', 'Successful login', '2026-06-23 21:29:52'),
	(447, 3, 'LOGIN', 'Successful login', '2026-06-23 21:33:39'),
	(448, 3, 'LOGIN', 'Successful login', '2026-06-23 21:33:59'),
	(449, 3, 'LOGIN', 'Successful login', '2026-06-23 21:34:35'),
	(450, 3, 'LOGIN', 'Successful login', '2026-06-23 21:35:21'),
	(451, 3, 'LOGIN', 'Successful login', '2026-06-23 21:41:29'),
	(452, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-23 21:42:00'),
	(453, 3, 'LOGIN', 'Successful login', '2026-06-23 21:42:06'),
	(454, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-23 21:42:09'),
	(455, 1, 'LOGIN', 'Successful login', '2026-06-23 21:42:14'),
	(456, 3, 'LOGIN', 'Successful login', '2026-06-23 21:46:18'),
	(457, 3, 'LOGIN', 'Successful login', '2026-06-23 21:55:17'),
	(458, 3, 'LOGIN', 'Successful login', '2026-06-23 22:23:18'),
	(459, 3, 'LOGIN', 'Successful login', '2026-06-23 22:28:58'),
	(460, 3, 'LOGIN', 'Successful login', '2026-06-23 22:31:19'),
	(461, 3, 'LOGIN', 'Successful login', '2026-06-23 22:34:24'),
	(462, 3, 'LOGIN', 'Successful login', '2026-06-23 22:57:55'),
	(463, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-23 22:58:29'),
	(464, 1, 'LOGIN', 'Successful login', '2026-06-24 05:24:21'),
	(465, 3, 'LOGIN', 'Successful login', '2026-06-24 05:24:32'),
	(466, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 05:25:17'),
	(467, 3, 'LOGIN', 'Successful login', '2026-06-24 05:48:23'),
	(468, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 05:48:48'),
	(469, 1, 'LOGIN', 'Successful login', '2026-06-24 05:48:53'),
	(470, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 05:49:55'),
	(471, 1, 'LOGIN', 'Successful login', '2026-06-24 05:50:00'),
	(472, 1, 'LOGIN', 'Successful login', '2026-06-24 05:55:59'),
	(473, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 05:56:40'),
	(474, 3, 'LOGIN', 'Successful login', '2026-06-24 05:56:47'),
	(475, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 05:56:51'),
	(476, 1, 'LOGIN', 'Successful login', '2026-06-24 05:56:57'),
	(477, 3, 'LOGIN', 'Successful login', '2026-06-24 05:57:05'),
	(478, 1, 'UPDATE_SUB', 'Customer ID: 3', '2026-06-24 05:57:18'),
	(479, 1, 'REVOKE_SUB', 'Sub ID: 5', '2026-06-24 05:57:23'),
	(480, 1, 'ALLOCATE_SUB', 'Customer ID: 3', '2026-06-24 05:57:29'),
	(481, 1, 'REVOKE_SUB', 'Sub ID: 1', '2026-06-24 05:57:41'),
	(482, 1, 'REVOKE_SUB', 'Sub ID: 6', '2026-06-24 05:57:49'),
	(483, 1, 'ALLOCATE_SUB', 'Customer ID: 1', '2026-06-24 05:58:02'),
	(484, 1, 'ALLOCATE_SUB', 'Customer ID: 1', '2026-06-24 05:58:08'),
	(485, 3, 'LOGIN', 'Successful login', '2026-06-24 06:02:56'),
	(486, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 06:05:04'),
	(487, 1, 'LOGIN', 'Successful login', '2026-06-24 06:05:09'),
	(488, 3, 'LOGIN', 'Successful login', '2026-06-24 06:08:06'),
	(489, 3, 'CANCEL_ORDER', 'Order ID: 932', '2026-06-24 06:08:10'),
	(490, 3, 'CANCEL_ORDER', 'Order ID: 931', '2026-06-24 06:08:12'),
	(491, 3, 'LOGIN', 'Successful login', '2026-06-24 06:17:05'),
	(492, 3, 'CANCEL_ORDER', 'Order ID: 934', '2026-06-24 06:18:12'),
	(493, 3, 'LOGIN', 'Successful login', '2026-06-24 06:23:29'),
	(494, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 06:23:43'),
	(495, 3, 'LOGIN', 'Successful login', '2026-06-24 06:24:20'),
	(496, 3, 'LOGIN', 'Successful login', '2026-06-24 06:31:06'),
	(497, 3, 'CANCEL_ORDER', 'Order ID: 933', '2026-06-24 06:31:29'),
	(498, 3, 'CANCEL_ORDER', 'Order ID: 927', '2026-06-24 06:31:53'),
	(499, 3, 'LOGIN', 'Successful login', '2026-06-24 06:37:46'),
	(500, 3, 'LOGIN', 'Successful login', '2026-06-24 06:38:08'),
	(501, 3, 'LOGIN', 'Successful login', '2026-06-24 06:41:25'),
	(502, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 06:43:11'),
	(503, 1, 'LOGIN', 'Successful login', '2026-06-24 06:43:16'),
	(504, 1, 'UPDATE_wallets', 'wallet_id=1', '2026-06-24 06:43:26'),
	(505, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 06:44:02'),
	(506, 3, 'LOGIN', 'Successful login', '2026-06-24 06:44:07'),
	(507, 3, 'CANCEL_ORDER', 'Order ID: 937', '2026-06-24 06:44:30'),
	(508, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 06:44:39'),
	(509, 3, 'LOGIN', 'Successful login', '2026-06-24 07:20:47'),
	(510, 3, 'LOGIN', 'Successful login', '2026-06-24 07:26:49'),
	(511, 1, 'LOGIN', 'Successful login', '2026-06-24 07:29:37'),
	(512, 3, 'LOGIN', 'Successful login', '2026-06-24 07:29:55'),
	(513, 1, 'UPDATE_products', 'product_id=1', '2026-06-24 07:30:51'),
	(514, 3, 'LOGIN', 'Successful login', '2026-06-24 07:42:46'),
	(515, 3, 'LOGIN', 'Successful login', '2026-06-24 07:46:16'),
	(516, 3, 'LOGIN', 'Successful login', '2026-06-24 07:51:38'),
	(517, 3, 'LOGIN', 'Successful login', '2026-06-24 07:57:53'),
	(518, 3, 'LOGIN', 'Successful login', '2026-06-24 08:00:11'),
	(519, 3, 'LOGIN', 'Successful login', '2026-06-24 08:01:28'),
	(520, 3, 'LOGIN', 'Successful login', '2026-06-24 08:02:15'),
	(521, 3, 'LOGIN', 'Successful login', '2026-06-24 08:02:55'),
	(522, 3, 'LOGIN', 'Successful login', '2026-06-24 08:03:29'),
	(523, 3, 'LOGIN', 'Successful login', '2026-06-24 08:04:24'),
	(524, 3, 'LOGIN', 'Successful login', '2026-06-24 08:05:35'),
	(525, 3, 'LOGIN', 'Successful login', '2026-06-24 08:07:36'),
	(526, 3, 'LOGIN', 'Successful login', '2026-06-24 08:08:15'),
	(527, 3, 'LOGIN', 'Successful login', '2026-06-24 08:33:09'),
	(528, 3, 'LOGIN', 'Successful login', '2026-06-24 08:36:10'),
	(529, 3, 'LOGIN', 'Successful login', '2026-06-24 08:36:45'),
	(530, 3, 'LOGIN', 'Successful login', '2026-06-24 08:41:19'),
	(531, 3, 'LOGIN', 'Successful login', '2026-06-24 08:42:20'),
	(532, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 08:43:18'),
	(533, 3, 'LOGIN', 'Successful login', '2026-06-24 08:44:52'),
	(534, 3, 'LOGIN', 'Successful login', '2026-06-24 08:46:44'),
	(535, 3, 'LOGIN', 'Successful login', '2026-06-24 08:48:22'),
	(536, 3, 'LOGIN', 'Successful login', '2026-06-24 08:48:47'),
	(537, 3, 'LOGIN', 'Successful login', '2026-06-24 08:49:13'),
	(538, 3, 'LOGIN', 'Successful login', '2026-06-24 08:51:04'),
	(539, 3, 'LOGIN', 'Successful login', '2026-06-24 08:54:28'),
	(540, 3, 'LOGIN', 'Successful login', '2026-06-24 09:53:51'),
	(541, 3, 'LOGIN', 'Successful login', '2026-06-24 09:57:19'),
	(542, 3, 'LOGIN', 'Successful login', '2026-06-24 10:02:05'),
	(543, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 10:02:15'),
	(544, 3, 'LOGIN', 'Successful login', '2026-06-24 10:02:21'),
	(545, 3, 'LOGIN', 'Successful login', '2026-06-24 10:10:27'),
	(546, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences and picture', '2026-06-24 10:10:36'),
	(547, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences and picture', '2026-06-24 10:10:52'),
	(548, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences and picture', '2026-06-24 10:11:18'),
	(549, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences and picture', '2026-06-24 10:11:30'),
	(550, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences and picture', '2026-06-24 10:12:46'),
	(551, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 10:12:49'),
	(552, 3, 'LOGIN', 'Successful login', '2026-06-24 10:12:54'),
	(553, 3, 'LOGIN', 'Successful login', '2026-06-24 10:15:29'),
	(554, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 10:16:34'),
	(555, 3, 'LOGIN', 'Successful login', '2026-06-24 10:16:49'),
	(556, 3, 'SUBMIT_FEEDBACK', 'Order ID 701', '2026-06-24 10:17:07'),
	(557, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 10:17:11'),
	(558, 1, 'LOGIN', 'Successful login', '2026-06-24 10:17:16'),
	(559, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 10:17:29'),
	(560, 3, 'LOGIN', 'Successful login', '2026-06-24 10:17:35'),
	(561, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 10:17:51'),
	(562, 1, 'LOGIN', 'Successful login', '2026-06-24 10:17:56'),
	(563, 1, 'KITCHEN_MARK_READY', '501', '2026-06-24 10:18:04'),
	(564, 1, 'KITCHEN_START_PREP', '926', '2026-06-24 10:18:05'),
	(565, 3, 'LOGIN', 'Successful login', '2026-06-24 10:31:25'),
	(566, 3, 'UPDATE_PROFILE', 'Updated profile nutrition preferences and picture', '2026-06-24 10:32:32'),
	(567, 3, 'CANCEL_ORDER', 'Order ID: 938', '2026-06-24 10:33:19'),
	(568, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 10:34:34'),
	(569, 1, 'LOGIN', 'Successful login', '2026-06-24 10:35:39'),
	(570, 1, 'KITCHEN_START_PREP', '939', '2026-06-24 10:36:02'),
	(571, 1, 'LOGIN', 'Successful login', '2026-06-24 10:48:14'),
	(572, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 10:48:23'),
	(573, 1, 'LOGIN', 'Successful login', '2026-06-24 10:52:26'),
	(574, 1, 'KITCHEN_MARK_READY', '926', '2026-06-24 10:52:36'),
	(575, 1, 'KITCHEN_MARK_READY', '939', '2026-06-24 10:52:37'),
	(576, 1, 'VERIFY_PAYMENT', 'Processed Payment ID: 5', '2026-06-24 10:53:03'),
	(577, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 10:53:20'),
	(578, 3, 'LOGIN', 'Successful login', '2026-06-24 10:53:29'),
	(579, 3, 'LOGIN', 'Successful login', '2026-06-24 11:19:31'),
	(580, 3, 'LOGIN', 'Successful login', '2026-06-24 11:26:28'),
	(581, 3, 'CANCEL_ORDER', 'Order ID: 945', '2026-06-24 11:27:07'),
	(582, 3, 'CANCEL_ORDER', 'Order ID: 944', '2026-06-24 11:29:04'),
	(583, 3, 'CANCEL_ORDER', 'Order ID: 943', '2026-06-24 11:29:07'),
	(584, 3, 'SUBMIT_PAYMENT', 'Order ID: 946', '2026-06-24 11:31:45'),
	(585, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 11:31:49'),
	(586, 1, 'LOGIN', 'Successful login', '2026-06-24 11:31:58'),
	(587, 1, 'VERIFY_PAYMENT', 'Processed Payment ID: 8', '2026-06-24 11:32:03'),
	(588, 1, 'KITCHEN_MARK_READY', '946', '2026-06-24 11:32:15'),
	(589, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 11:32:23'),
	(590, 3, 'LOGIN', 'Successful login', '2026-06-24 11:32:33'),
	(591, 1, 'LOGIN', 'Successful login', '2026-06-24 11:34:21'),
	(592, 3, 'SUBMIT_PAYMENT', 'Order ID: 947', '2026-06-24 11:34:38'),
	(593, 1, 'VERIFY_PAYMENT', 'Processed Payment ID: 9', '2026-06-24 11:34:49'),
	(594, 1, 'KITCHEN_START_PREP', '947', '2026-06-24 11:35:03'),
	(595, 1, 'KITCHEN_START_PREP', '503', '2026-06-24 11:35:06'),
	(596, 3, 'LOGIN', 'Successful login', '2026-06-24 13:10:42'),
	(597, 3, 'LOGIN', 'Successful login', '2026-06-24 13:19:01'),
	(598, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 13:19:24'),
	(603, 1, 'DELETE_audit_logs', 'log_id=601', '2026-06-24 13:37:40'),
	(605, 1, 'DELETE_audit_logs', 'log_id=604', '2026-06-24 13:37:49'),
	(606, 1, 'LOGIN', 'Successful login', '2026-06-24 14:22:07'),
	(607, 1, 'LOGIN', 'Successful login', '2026-06-24 14:29:04'),
	(608, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 14:30:05'),
	(609, 3, 'LOGIN', 'Successful login', '2026-06-24 14:49:21'),
	(610, 3, 'CANCEL_ORDER', 'Order ID: 959', '2026-06-24 14:50:30'),
	(611, 3, 'CANCEL_ORDER', 'Order ID: 958', '2026-06-24 14:50:40'),
	(612, 3, 'CANCEL_ORDER', 'Order ID: 957', '2026-06-24 14:50:41'),
	(613, 3, 'CANCEL_ORDER', 'Order ID: 956', '2026-06-24 14:50:42'),
	(614, 3, 'CANCEL_ORDER', 'Order ID: 955', '2026-06-24 14:50:43'),
	(615, 3, 'CANCEL_ORDER', 'Order ID: 953', '2026-06-24 14:50:45'),
	(616, 3, 'CANCEL_ORDER', 'Order ID: 954', '2026-06-24 14:50:48'),
	(617, 3, 'CANCEL_ORDER', 'Order ID: 952', '2026-06-24 14:50:49'),
	(618, 3, 'CANCEL_ORDER', 'Order ID: 950', '2026-06-24 14:50:50'),
	(619, 3, 'CANCEL_ORDER', 'Order ID: 951', '2026-06-24 14:50:51'),
	(620, 3, 'CANCEL_ORDER', 'Order ID: 949', '2026-06-24 14:50:52'),
	(621, 3, 'CANCEL_ORDER', 'Order ID: 948', '2026-06-24 14:50:53'),
	(622, 3, 'CANCEL_ORDER', 'Order ID: 947', '2026-06-24 14:50:54'),
	(623, 3, 'LOGIN', 'Successful login', '2026-06-24 15:04:08'),
	(624, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 15:04:20'),
	(625, 3, 'LOGIN', 'Successful login', '2026-06-24 15:09:02'),
	(626, 3, 'LOGIN', 'Successful login', '2026-06-24 15:11:36'),
	(627, 3, 'LOGIN', 'Successful login', '2026-06-24 15:16:39'),
	(628, 3, 'LOGIN', 'Successful login', '2026-06-24 15:19:23'),
	(629, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 15:20:06'),
	(630, 1, 'LOGIN', 'Successful login', '2026-06-24 15:20:13'),
	(631, 1, 'UPDATE_products', 'product_id=8', '2026-06-24 15:20:39'),
	(632, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 15:21:05'),
	(633, 1, 'LOGIN', 'Successful login', '2026-06-24 15:21:10'),
	(634, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 15:21:25'),
	(635, 1, 'LOGIN', 'Successful login', '2026-06-24 16:00:41'),
	(636, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 16:00:46'),
	(637, 3, 'LOGIN', 'Successful login', '2026-06-24 16:00:56'),
	(638, 1, 'LOGIN', 'Successful login', '2026-06-24 16:01:28'),
	(639, 3, 'CANCEL_ORDER', 'Order ID: 961', '2026-06-24 16:02:01'),
	(640, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 16:02:05'),
	(641, 1, 'LOGOUT', 'System Manager logged out', '2026-06-24 16:03:32'),
	(642, 3, 'LOGIN', 'Successful login', '2026-06-24 16:03:37'),
	(643, 3, 'LOGIN', 'Successful login', '2026-06-24 16:04:22'),
	(644, 3, 'LOGIN', 'Successful login', '2026-06-24 16:07:28'),
	(645, 3, 'LOGIN', 'Successful login', '2026-06-24 16:50:45'),
	(646, 3, 'LOGIN', 'Successful login', '2026-06-24 16:51:08'),
	(647, 3, 'LOGOUT', 'Customer clicked explicit workspace header logout button', '2026-06-24 16:52:53'),
	(648, 3, 'LOGIN', 'Successful login', '2026-06-24 16:53:13'),
	(649, 3, 'LOGIN', 'Successful login', '2026-06-24 16:53:53');

-- Dumping structure for event canteen_sales_inventory_system.auto_cancel_unpaid_orders
DROP EVENT IF EXISTS `auto_cancel_unpaid_orders`;
DELIMITER //
CREATE EVENT `auto_cancel_unpaid_orders` ON SCHEDULE EVERY 5 MINUTE STARTS '2026-06-24 23:28:13' ON COMPLETION NOT PRESERVE ENABLE DO UPDATE orders 
  SET order_status = 'CANCELLED', notes = 'Auto-cancelled: Payment timeout'
  WHERE order_status = 'PENDING' 
  -- Assuming your column is order_date based on our last fix!
  AND TIMESTAMPDIFF(MINUTE, order_date, NOW()) > 30//
DELIMITER ;

-- Dumping structure for table canteen_sales_inventory_system.categories
DROP TABLE IF EXISTS `categories`;
CREATE TABLE IF NOT EXISTS `categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `category_name` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.categories: ~9 rows (approximately)
INSERT INTO `categories` (`category_id`, `category_name`, `description`, `status`) VALUES
	(1, 'Rice Meals', 'Meals with rice', 'ACTIVE'),
	(2, 'Snacks', 'Quick snacks', 'ACTIVE'),
	(3, 'Beverages', 'Cold and hot beverages', 'ACTIVE'),
	(4, 'Desserts', 'Sweet products', 'ACTIVE'),
	(5, 'Milk Tea', 'Milk Tea Drinks', 'ACTIVE'),
	(6, 'Coffee', 'Coffee Beverages', 'ACTIVE'),
	(7, 'Pasta', 'Pasta Meals', 'ACTIVE'),
	(8, 'Sandwiches', 'Fresh Sandwiches', 'ACTIVE'),
	(9, 'Pastries', 'Baked Products', 'ACTIVE'),
	(10, 'Bakery', 'Fresh baked products', 'ACTIVE');

-- Dumping structure for table canteen_sales_inventory_system.customer_profiles
DROP TABLE IF EXISTS `customer_profiles`;
CREATE TABLE IF NOT EXISTS `customer_profiles` (
  `customer_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `student_employee_no` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `course_department` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dietary_notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `loyalty_points` int NOT NULL DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `dietary_profile` enum('NONE','DIABETIC_FRIENDLY','LOW_SODIUM','VEGETARIAN','HALAL') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
  `allergen_restrictions` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  `profile_picture` longblob,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `customer_profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.customer_profiles: ~1 rows (approximately)
INSERT INTO `customer_profiles` (`customer_id`, `user_id`, `student_employee_no`, `course_department`, `dietary_notes`, `loyalty_points`, `created_at`, `dietary_profile`, `allergen_restrictions`, `profile_picture`) VALUES
	(1, 3, '2026-0001', 'Computer Engineering', 'No allergies', 20, '2026-05-25 15:11:24', 'LOW_SODIUM', 'dairy', NULL),
	(2, 1, 'EMP-2026-001', 'Canteen Operations Root', NULL, 150, '2026-06-08 04:29:17', 'NONE', '', NULL),
	(3, 6, '2026-1003', 'BSBA', NULL, 35, '2026-06-11 00:29:45', 'NONE', '', NULL),
	(4, 9, '2021-2100-MN-0', 'Human Kinetics', NULL, 120, '2026-06-14 09:58:20', 'NONE', 'Nuts', NULL),
	(5, 10, '2023-02328-MN-0', 'Civil Engineering', '', 0, '2026-06-20 04:33:01', 'DIABETIC_FRIENDLY', 'dairy', NULL);

-- Dumping structure for table canteen_sales_inventory_system.customer_subscriptions
DROP TABLE IF EXISTS `customer_subscriptions`;
CREATE TABLE IF NOT EXISTS `customer_subscriptions` (
  `sub_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `plan_id` int NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `credits_total` int NOT NULL,
  `credits_used` int NOT NULL DEFAULT '0',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `payment_method` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'WALLET',
  `payment_reference` varchar(150) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`sub_id`),
  KEY `customer_id` (`customer_id`),
  KEY `plan_id` (`plan_id`),
  CONSTRAINT `customer_subscriptions_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer_profiles` (`customer_id`) ON DELETE CASCADE,
  CONSTRAINT `customer_subscriptions_ibfk_2` FOREIGN KEY (`plan_id`) REFERENCES `meal_plans` (`plan_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.customer_subscriptions: ~3 rows (approximately)
INSERT INTO `customer_subscriptions` (`sub_id`, `customer_id`, `plan_id`, `start_date`, `end_date`, `credits_total`, `credits_used`, `status`, `payment_method`, `payment_reference`) VALUES
	(2, 2, 2, '2026-06-01', '2026-07-01', 25, 5, 'ACTIVE', 'WALLET', NULL),
	(7, 1, 3, '2026-06-24', '2026-07-24', 60, 1, 'ACTIVE', 'WALLET', NULL),
	(8, 1, 2, '2026-06-24', '2026-07-24', 30, 0, 'ACTIVE', 'WALLET', NULL);

-- Dumping structure for table canteen_sales_inventory_system.employees
DROP TABLE IF EXISTS `employees`;
CREATE TABLE IF NOT EXISTS `employees` (
  `employee_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `position` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `shift_schedule` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hired_date` date DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  PRIMARY KEY (`employee_id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `employees_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.employees: ~3 rows (approximately)
INSERT INTO `employees` (`employee_id`, `user_id`, `position`, `shift_schedule`, `hired_date`, `status`) VALUES
	(1, 2, 'Cashier', '7:00 AM - 4:00 PM', '2026-01-05', 'ACTIVE'),
	(2, 5, 'Cashier', 'Afternoon', NULL, 'ACTIVE'),
	(4, 6, 'Cashier', 'Morning', '2026-06-20', 'ACTIVE');

-- Dumping structure for table canteen_sales_inventory_system.expenses
DROP TABLE IF EXISTS `expenses`;
CREATE TABLE IF NOT EXISTS `expenses` (
  `expense_id` int NOT NULL AUTO_INCREMENT,
  `expense_date` date NOT NULL,
  `category` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `amount` decimal(12,2) NOT NULL,
  `recorded_by` int DEFAULT NULL,
  PRIMARY KEY (`expense_id`),
  KEY `recorded_by` (`recorded_by`),
  CONSTRAINT `expenses_ibfk_1` FOREIGN KEY (`recorded_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.expenses: ~4 rows (approximately)
INSERT INTO `expenses` (`expense_id`, `expense_date`, `category`, `description`, `amount`, `recorded_by`) VALUES
	(1, '2026-05-20', 'Utilities', 'Cooking gas refill', 850.00, 1),
	(2, '2026-06-01', 'Utilities', 'Electric Bill', 3500.00, 1),
	(3, '2026-06-02', 'Supplies', 'Cleaning Materials', 1200.00, 1),
	(4, '2026-06-03', 'Maintenance', 'Equipment Repair', 2500.00, 1);

-- Dumping structure for table canteen_sales_inventory_system.feedback
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE IF NOT EXISTS `feedback` (
  `feedback_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `order_id` int DEFAULT NULL,
  `rating` int NOT NULL,
  `comments` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('VISIBLE','HIDDEN') COLLATE utf8mb4_unicode_ci DEFAULT 'VISIBLE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`feedback_id`),
  KEY `customer_id` (`customer_id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer_profiles` (`customer_id`),
  CONSTRAINT `feedback_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.feedback: ~2 rows (approximately)
INSERT INTO `feedback` (`feedback_id`, `customer_id`, `order_id`, `rating`, `comments`, `status`, `created_at`) VALUES
	(1, 1, 501, 5, 'The Carbonara is phenomenal! Creamy, fast, and completely packed out in Tahoma style.', '', '2026-06-11 00:29:45'),
	(2, 2, 502, 4, 'Very fast pickup preparation window. Chicken sandwich was perfectly toasted.', '', '2026-06-11 00:29:45'),
	(3, 1, 909, 4, 'Good', 'VISIBLE', '2026-06-15 03:01:11'),
	(4, 1, 701, 5, 'The Food is Good', 'VISIBLE', '2026-06-24 10:17:07');

-- Dumping structure for table canteen_sales_inventory_system.ingredients
DROP TABLE IF EXISTS `ingredients`;
CREATE TABLE IF NOT EXISTS `ingredients` (
  `ingredient_id` int NOT NULL AUTO_INCREMENT,
  `ingredient_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `unit` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `quantity_on_hand` decimal(12,3) NOT NULL DEFAULT '0.000',
  `reorder_level` decimal(12,3) NOT NULL DEFAULT '0.000',
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  `allergen_tags` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`ingredient_id`),
  UNIQUE KEY `ingredient_name` (`ingredient_name`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.ingredients: ~17 rows (approximately)
INSERT INTO `ingredients` (`ingredient_id`, `ingredient_name`, `unit`, `quantity_on_hand`, `reorder_level`, `status`, `allergen_tags`) VALUES
	(1, 'Rice', 'kg', 4.000, 10.000, 'ACTIVE', ''),
	(2, 'Chicken', 'kg', 25.000, 5.000, 'ACTIVE', ''),
	(3, 'Adobo Sauce', 'liter', 8.000, 2.000, 'ACTIVE', 'SOY'),
	(4, 'Siomai', 'pcs', 120.000, 40.000, 'ACTIVE', 'SOY, GLUTEN'),
	(5, 'Bread Slice', 'pcs', 100.000, 30.000, 'ACTIVE', 'EGG'),
	(6, 'Cheese', 'slice', 2.000, 15.000, 'ACTIVE', 'DAIRY'),
	(7, 'Iced Tea Mix', 'gram', 1000.000, 300.000, 'ACTIVE', 'DAIRY'),
	(8, 'Cup 16oz', 'pcs', 100.000, 30.000, 'ACTIVE', 'DAIRY'),
	(9, 'Banana Turon', 'pcs', 70.000, 20.000, 'ACTIVE', 'GLUTEN'),
	(10, 'Sugar', 'kg', 100.000, 20.000, 'ACTIVE', ''),
	(11, 'Coffee Beans', 'kg', 50.000, 10.000, 'ACTIVE', 'DAIRY'),
	(12, 'Milk', 'liter', 80.000, 15.000, 'ACTIVE', 'DAIRY'),
	(13, 'Tea Powder', 'kg', 40.000, 10.000, 'ACTIVE', 'DAIRY'),
	(14, 'Bread', 'pack', 30.000, 10.000, 'ACTIVE', 'GLUTEN'),
	(17, 'Egg', 'tray', 50.000, 10.000, 'ACTIVE', 'EGG'),
	(18, 'Butter', 'kg', 20.000, 5.000, 'ACTIVE', 'DAIRY'),
	(19, 'Lettuce', 'kg', 25.000, 5.000, 'ACTIVE', ''),
	(29, 'Beef', 'kg', 100.000, 5.000, 'ACTIVE', 'Soy');

-- Dumping structure for table canteen_sales_inventory_system.ingredient_batches
DROP TABLE IF EXISTS `ingredient_batches`;
CREATE TABLE IF NOT EXISTS `ingredient_batches` (
  `batch_id` int NOT NULL AUTO_INCREMENT,
  `ingredient_id` int NOT NULL,
  `supplier_id` int DEFAULT NULL,
  `batch_number` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `received_date` date NOT NULL,
  `expiry_date` date DEFAULT NULL,
  `quantity_received` decimal(12,3) NOT NULL,
  `quantity_available` decimal(12,3) NOT NULL,
  `unit_cost` decimal(12,2) NOT NULL DEFAULT '0.00',
  `status` enum('AVAILABLE','EMPTY','EXPIRED','DISPOSED') COLLATE utf8mb4_unicode_ci DEFAULT 'AVAILABLE',
  PRIMARY KEY (`batch_id`),
  UNIQUE KEY `batch_number` (`batch_number`),
  KEY `ingredient_id` (`ingredient_id`),
  KEY `supplier_id` (`supplier_id`),
  CONSTRAINT `ingredient_batches_ibfk_1` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`ingredient_id`),
  CONSTRAINT `ingredient_batches_ibfk_2` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=904 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.ingredient_batches: ~5 rows (approximately)
INSERT INTO `ingredient_batches` (`batch_id`, `ingredient_id`, `supplier_id`, `batch_number`, `received_date`, `expiry_date`, `quantity_received`, `quantity_available`, `unit_cost`, `status`) VALUES
	(1, 1, 1, 'RICE-20260501', '2026-05-01', '2026-09-01', 30.000, 30.000, 48.00, 'AVAILABLE'),
	(2, 2, 1, 'CHK-20260520', '2026-05-20', '2026-05-27', 15.000, 15.000, 180.00, 'AVAILABLE'),
	(3, 4, 2, 'SIO-20260519', '2026-05-19', '2026-06-19', 120.000, 120.000, 5.00, 'AVAILABLE'),
	(88, 6, 1, 'BATCH-EXP-WARN-88', '2026-06-11', '2026-06-15', 50.000, 35.000, 12.00, 'AVAILABLE'),
	(99, 6, 1, 'BATCH-EXP-WARNING', '2026-06-11', '2026-06-16', 50.000, 25.000, 10.00, 'AVAILABLE'),
	(902, 3, 1, 'BATCH-FORCE-EXP-01', '2026-06-11', '2026-06-14', 10.000, 10.000, 50.00, 'AVAILABLE'),
	(903, 2, 1, 'BATCH-CHICKEN-001', '2026-06-24', '2026-07-05', 10.000, 10.000, 30.00, 'AVAILABLE');

-- Dumping structure for table canteen_sales_inventory_system.inventory_adjustments
DROP TABLE IF EXISTS `inventory_adjustments`;
CREATE TABLE IF NOT EXISTS `inventory_adjustments` (
  `adjustment_id` int NOT NULL AUTO_INCREMENT,
  `ingredient_id` int NOT NULL,
  `batch_id` int DEFAULT NULL,
  `adjustment_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `adjustment_type` enum('ADD','DEDUCT','SPOILAGE','WASTAGE','CORRECTION') COLLATE utf8mb4_unicode_ci NOT NULL,
  `quantity` decimal(12,3) NOT NULL,
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `adjusted_by` int DEFAULT NULL,
  PRIMARY KEY (`adjustment_id`),
  KEY `ingredient_id` (`ingredient_id`),
  KEY `batch_id` (`batch_id`),
  KEY `adjusted_by` (`adjusted_by`),
  CONSTRAINT `inventory_adjustments_ibfk_1` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`ingredient_id`),
  CONSTRAINT `inventory_adjustments_ibfk_2` FOREIGN KEY (`batch_id`) REFERENCES `ingredient_batches` (`batch_id`),
  CONSTRAINT `inventory_adjustments_ibfk_3` FOREIGN KEY (`adjusted_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.inventory_adjustments: ~2 rows (approximately)
INSERT INTO `inventory_adjustments` (`adjustment_id`, `ingredient_id`, `batch_id`, `adjustment_date`, `adjustment_type`, `quantity`, `reason`, `adjusted_by`) VALUES
	(1, 1, 1, '2026-06-11 00:29:45', '', -2.000, 'Spoiled Stock - water damage exposure', 1),
	(2, 2, 2, '2026-06-11 00:29:45', 'CORRECTION', 1.500, 'Inventory Count Correction during weekend audit', 1),
	(3, 6, 1, '2026-06-11 00:38:11', 'CORRECTION', -1.000, 'Melted cheese packaging seal failure drop', 1);

-- Dumping structure for table canteen_sales_inventory_system.kitchen_queue
DROP TABLE IF EXISTS `kitchen_queue`;
CREATE TABLE IF NOT EXISTS `kitchen_queue` (
  `queue_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `slot_id` int NOT NULL,
  `queue_number` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `queue_priority` enum('STANDARD','FACULTY','PWD_SENIOR','EMERGENCY') COLLATE utf8mb4_unicode_ci DEFAULT 'STANDARD',
  `preparation_countdown_minutes` int DEFAULT '15',
  `actual_prep_start` timestamp NULL DEFAULT NULL,
  `actual_prep_ready` timestamp NULL DEFAULT NULL,
  `actual_serving_completed` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`queue_id`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `slot_id` (`slot_id`),
  CONSTRAINT `kitchen_queue_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  CONSTRAINT `kitchen_queue_ibfk_2` FOREIGN KEY (`slot_id`) REFERENCES `pickup_slots` (`slot_id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.kitchen_queue: ~8 rows (approximately)
INSERT INTO `kitchen_queue` (`queue_id`, `order_id`, `slot_id`, `queue_number`, `queue_priority`, `preparation_countdown_minutes`, `actual_prep_start`, `actual_prep_ready`, `actual_serving_completed`, `created_at`) VALUES
	(3, 101, 1, 'S-001', 'STANDARD', 15, '2026-06-08 04:31:28', NULL, NULL, '2026-06-08 04:31:28'),
	(4, 102, 2, 'F-001', 'FACULTY', 10, '2026-06-08 04:31:28', NULL, NULL, '2026-06-08 04:31:28'),
	(5, 501, 3, 'S-005', 'STANDARD', 15, '2026-06-11 00:26:48', NULL, NULL, '2026-06-11 00:26:48'),
	(6, 502, 3, 'S-006', 'STANDARD', 12, '2026-06-11 00:26:48', NULL, NULL, '2026-06-11 00:26:48'),
	(7, 503, 3, 'F-002', 'FACULTY', 8, '2026-06-11 00:26:48', NULL, NULL, '2026-06-11 00:26:48'),
	(8, 701, 3, 'S-010', 'STANDARD', 15, '2026-06-11 00:38:11', NULL, NULL, '2026-06-11 00:38:11'),
	(9, 702, 3, 'S-011', 'STANDARD', 12, '2026-06-11 00:38:11', NULL, NULL, '2026-06-11 00:38:11'),
	(10, 703, 3, 'F-005', 'FACULTY', 8, '2026-06-11 00:38:11', NULL, NULL, '2026-06-11 00:38:11');

-- Dumping structure for table canteen_sales_inventory_system.meal_plans
DROP TABLE IF EXISTS `meal_plans`;
CREATE TABLE IF NOT EXISTS `meal_plans` (
  `plan_id` int NOT NULL AUTO_INCREMENT,
  `plan_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `plan_type` enum('WEEKLY','MONTHLY','BUNDLE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `total_credits` int NOT NULL,
  PRIMARY KEY (`plan_id`),
  UNIQUE KEY `plan_name` (`plan_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.meal_plans: ~6 rows (approximately)
INSERT INTO `meal_plans` (`plan_id`, `plan_name`, `plan_type`, `price`, `total_credits`) VALUES
	(1, 'Breakfast Saver Weekly', 'WEEKLY', 200.00, 5),
	(2, 'Unli-Lunch Monthly Pass', 'MONTHLY', 1200.00, 22),
	(3, 'Discounted 10-Meal Bundle', 'BUNDLE', 650.00, 10),
	(4, 'Student Basic', 'MONTHLY', 500.00, 10),
	(5, 'Student Plus', 'MONTHLY', 1000.00, 25),
	(6, 'Faculty Plan', 'MONTHLY', 1500.00, 40);

-- Dumping structure for table canteen_sales_inventory_system.messages
DROP TABLE IF EXISTS `messages`;
CREATE TABLE IF NOT EXISTS `messages` (
  `message_id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int NOT NULL,
  `receiver_id` int DEFAULT NULL,
  `subject` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `body` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('SENT','READ','CLOSED') COLLATE utf8mb4_unicode_ci DEFAULT 'SENT',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`),
  KEY `sender_id` (`sender_id`),
  KEY `receiver_id` (`receiver_id`),
  CONSTRAINT `messages_ibfk_1` FOREIGN KEY (`sender_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `messages_ibfk_2` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.messages: ~6 rows (approximately)
INSERT INTO `messages` (`message_id`, `sender_id`, `receiver_id`, `subject`, `body`, `status`, `created_at`) VALUES
	(1, 1, 4, 'Welcome', 'Thank you for registering', 'READ', '2026-06-11 00:14:31'),
	(2, 1, 5, 'Promo', 'Check our latest promo', '', '2026-06-11 00:14:31'),
	(3, 3, 1, 'TEST', 'Hello Twin', 'READ', '2026-06-15 02:35:37'),
	(4, 3, 1, 'TEST', 'TEST', 'READ', '2026-06-15 02:38:11'),
	(5, 1, 3, 'Re: TEST', 'test reply', 'SENT', '2026-06-15 02:49:15'),
	(6, 1, 3, 'Re: TEST', 'BRUH', 'SENT', '2026-06-15 02:59:21');

-- Dumping structure for table canteen_sales_inventory_system.notifications
DROP TABLE IF EXISTS `notifications`;
CREATE TABLE IF NOT EXISTS `notifications` (
  `notification_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `title` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `body` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('UNREAD','READ') COLLATE utf8mb4_unicode_ci DEFAULT 'UNREAD',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`notification_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.notifications: ~39 rows (approximately)
INSERT INTO `notifications` (`notification_id`, `user_id`, `title`, `body`, `status`, `created_at`) VALUES
	(1, 4, 'Order Ready', 'Your order is ready for pickup', 'UNREAD', '2026-06-11 00:14:31'),
	(2, 5, 'Promo Available', 'New promo is now available', 'UNREAD', '2026-06-11 00:14:31'),
	(3, 1, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-15 02:43:58'),
	(4, 3, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'READ', '2026-06-15 03:00:05'),
	(5, 3, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-15 03:00:22'),
	(6, 3, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-15 03:00:26'),
	(7, 3, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-15 03:00:33'),
	(8, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:08:21'),
	(9, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:08:59'),
	(10, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:13'),
	(11, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:15'),
	(12, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:16'),
	(13, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:17'),
	(14, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:18'),
	(15, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:19'),
	(16, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:20'),
	(17, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:20'),
	(18, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:21'),
	(19, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:23'),
	(20, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:24'),
	(21, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:25'),
	(22, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:26'),
	(23, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-20 06:12:27'),
	(24, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:29'),
	(25, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:30'),
	(26, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:30'),
	(27, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:31'),
	(28, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:32'),
	(29, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:32'),
	(30, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:33'),
	(31, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:34'),
	(32, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:35'),
	(33, 1, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:35'),
	(34, 3, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'READ', '2026-06-20 06:12:37'),
	(35, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:38'),
	(36, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:38'),
	(37, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:39'),
	(38, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:40'),
	(39, 10, 'Order Ready Alert', 'Your Canteen Order has been marked as completed. Thank you for your order!', 'UNREAD', '2026-06-20 06:12:41'),
	(40, 3, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'READ', '2026-06-24 10:18:04'),
	(41, 10, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-24 10:52:36'),
	(42, 3, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'UNREAD', '2026-06-24 10:52:37'),
	(43, 3, 'Order Ready Alert', 'Your Canteen Order is Hot and Ready for Pickup! Please proceed to the counter.', 'READ', '2026-06-24 11:32:15');

-- Dumping structure for table canteen_sales_inventory_system.orders
DROP TABLE IF EXISTS `orders`;
CREATE TABLE IF NOT EXISTS `orders` (
  `order_id` int NOT NULL AUTO_INCREMENT,
  `order_no` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `customer_id` int DEFAULT NULL,
  `slot_id` int DEFAULT NULL,
  `order_type` enum('WALK_IN','PICKUP','DINE_IN') COLLATE utf8mb4_unicode_ci DEFAULT 'PICKUP',
  `table_number` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ordered_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `subtotal` decimal(12,2) NOT NULL DEFAULT '0.00',
  `discount_amount` decimal(12,2) NOT NULL DEFAULT '0.00',
  `total_amount` decimal(12,2) NOT NULL DEFAULT '0.00',
  `amount_paid` decimal(12,2) NOT NULL DEFAULT '0.00',
  `balance` decimal(12,2) NOT NULL DEFAULT '0.00',
  `payment_status` enum('UNPAID','PENDING','PAID','REFUNDED') COLLATE utf8mb4_unicode_ci DEFAULT 'UNPAID',
  `order_status` enum('PENDING','CONFIRMED','PREPARING','READY','COMPLETED','CANCELLED','REJECTED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `cashier_id` int DEFAULT NULL,
  `notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `inventory_deducted` tinyint(1) DEFAULT '0',
  `customer_type` enum('REGULAR','FACULTY','PWD','SENIOR','EMERGENCY') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'REGULAR',
  `queue_priority` int NOT NULL DEFAULT '3',
  `prep_started_at` timestamp NULL DEFAULT NULL,
  `prep_ready_at` timestamp NULL DEFAULT NULL,
  `customer_notified_at` timestamp NULL DEFAULT NULL,
  `served_completed_at` timestamp NULL DEFAULT NULL,
  `unclaimed_disposition` enum('NONE','REFUNDED','DISPOSED_WASTAGE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
  `payment_method` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'CASH',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `customer_id` (`customer_id`),
  KEY `cashier_id` (`cashier_id`),
  KEY `idx_kitchen_queue` (`order_type`,`order_status`),
  KEY `idx_ordered_at` (`ordered_at`),
  KEY `idx_pickup_slots` (`slot_id`,`order_status`),
  CONSTRAINT `fk_orders_slot` FOREIGN KEY (`slot_id`) REFERENCES `pickup_slots` (`slot_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer_profiles` (`customer_id`) ON DELETE SET NULL,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`cashier_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=962 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.orders: ~49 rows (approximately)
INSERT INTO `orders` (`order_id`, `order_no`, `customer_id`, `slot_id`, `order_type`, `table_number`, `ordered_at`, `subtotal`, `discount_amount`, `total_amount`, `amount_paid`, `balance`, `payment_status`, `order_status`, `cashier_id`, `notes`, `inventory_deducted`, `customer_type`, `queue_priority`, `prep_started_at`, `prep_ready_at`, `customer_notified_at`, `served_completed_at`, `unclaimed_disposition`, `payment_method`) VALUES
	(101, 'ORD-2026-0001', 1, NULL, 'PICKUP', NULL, '2026-06-08 04:31:28', 0.00, 0.00, 120.00, 0.00, 0.00, 'PAID', 'CONFIRMED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(102, 'ORD-2026-0002', 2, NULL, 'PICKUP', NULL, '2026-06-08 04:31:28', 0.00, 0.00, 115.00, 0.00, 0.00, 'PAID', 'CONFIRMED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(103, 'ORD-2026-0003', 1, NULL, 'PICKUP', NULL, '2026-06-08 04:31:28', 0.00, 0.00, 85.00, 0.00, 0.00, 'PAID', 'CANCELLED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(501, 'ORD-TEST-001', 1, NULL, 'PICKUP', NULL, '2026-06-11 00:26:48', 0.00, 0.00, 120.00, 0.00, 0.00, 'PAID', 'READY', NULL, NULL, 0, 'REGULAR', 3, NULL, '2026-06-24 10:18:04', NULL, NULL, 'NONE', 'CASH'),
	(502, 'ORD-TEST-002', 2, NULL, 'PICKUP', NULL, '2026-06-11 00:26:48', 0.00, 0.00, 95.00, 0.00, 0.00, 'PAID', 'CONFIRMED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(503, 'ORD-TEST-003', 2, NULL, 'PICKUP', NULL, '2026-06-11 00:26:48', 0.00, 0.00, 165.00, 0.00, 0.00, 'PAID', 'PREPARING', NULL, NULL, 0, 'REGULAR', 3, '2026-06-24 11:35:06', NULL, NULL, NULL, 'NONE', 'CASH'),
	(504, 'ORD-TEST-004', 1, NULL, 'PICKUP', NULL, '2026-06-11 00:26:48', 0.00, 0.00, 45.00, 0.00, 0.00, 'PAID', 'CANCELLED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(601, 'ORD-2026-A1', 1, NULL, 'PICKUP', NULL, '2026-06-11 00:34:02', 0.00, 0.00, 240.00, 0.00, 0.00, 'PAID', 'COMPLETED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(701, 'ORD-2026-LIVE1', 1, 3, 'PICKUP', NULL, '2026-06-11 00:38:11', 0.00, 0.00, 240.00, 240.00, 0.00, 'PAID', 'COMPLETED', NULL, NULL, 0, 'REGULAR', 3, '2026-06-11 03:32:00', '2026-06-11 03:44:00', NULL, NULL, 'NONE', 'CASH'),
	(702, 'ORD-2026-LIVE2', 2, NULL, 'PICKUP', NULL, '2026-06-11 00:38:11', 0.00, 0.00, 150.00, 0.00, 0.00, 'PENDING', 'COMPLETED', NULL, NULL, 0, 'REGULAR', 3, '2026-06-14 09:46:03', '2026-06-15 02:43:58', NULL, '2026-06-20 06:12:35', 'NONE', 'CASH'),
	(703, 'ORD-2026-LIVE3', 3, NULL, 'PICKUP', NULL, '2026-06-11 00:38:11', 0.00, 0.00, 90.00, 0.00, 0.00, 'UNPAID', 'CONFIRMED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(704, 'ORD-2026-LIVE4', 1, NULL, 'PICKUP', NULL, '2026-06-11 00:38:11', 0.00, 0.00, 45.00, 45.00, 0.00, 'REFUNDED', 'CANCELLED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(801, 'ORD-EXP-PMT1', 1, NULL, 'PICKUP', NULL, '2026-06-11 00:41:23', 0.00, 0.00, 170.00, 0.00, 0.00, 'UNPAID', 'CANCELLED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(901, 'ORD-DASH-PAY901', 1, NULL, 'PICKUP', NULL, '2026-06-11 00:43:43', 0.00, 0.00, 150.00, 0.00, 0.00, 'UNPAID', 'COMPLETED', NULL, NULL, 0, 'REGULAR', 3, '2026-06-14 09:46:07', NULL, NULL, '2026-06-15 02:43:47', 'NONE', 'CASH'),
	(902, 'ORD-20260614183213100', 1, NULL, 'PICKUP', NULL, '2026-06-14 10:32:13', 0.00, 0.00, 0.00, 0.00, 0.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup order', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(903, 'ORD-20260614183337769', 1, NULL, 'PICKUP', NULL, '2026-06-14 10:33:37', 0.00, 0.00, 0.00, 0.00, 0.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup order', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(904, 'ORD-20260614183743515', 1, NULL, 'PICKUP', NULL, '2026-06-14 10:37:43', 0.00, 0.00, 0.00, 0.00, 0.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup order', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(905, 'ORD-20260614184158608', 1, 13, 'PICKUP', NULL, '2026-06-14 10:41:58', 140.00, 0.00, 140.00, 140.00, 0.00, 'PAID', 'COMPLETED', NULL, 'Scheduled pickup order', 0, 'REGULAR', 3, '2026-06-15 02:43:36', NULL, NULL, '2026-06-15 03:00:26', 'NONE', 'CASH'),
	(906, 'ORD-20260614184812697', 4, 13, 'PICKUP', NULL, '2026-06-14 10:48:12', 45.00, 0.00, 45.00, 1000.00, 0.00, 'PAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, '2026-06-14 10:49:54', NULL, NULL, '2026-06-14 10:55:21', 'NONE', 'CASH'),
	(907, 'ORD-20260615103252309', 1, 13, 'PICKUP', NULL, '2026-06-15 02:32:52', 80.00, 0.00, 80.00, 80.00, 0.00, 'PAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, '2026-06-15 02:43:38', NULL, NULL, '2026-06-15 03:00:22', 'NONE', 'CASH'),
	(908, 'ORD-20260615103435387', 1, 13, 'PICKUP', NULL, '2026-06-15 02:34:35', 120.00, 0.00, 120.00, 120.00, 0.00, 'PAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'SENIOR', 3, '2026-06-15 02:40:39', '2026-06-15 03:00:05', NULL, '2026-06-20 06:12:37', 'NONE', 'CASH'),
	(909, 'ORD-20260615103616938', 1, 3, 'PICKUP', NULL, '2026-06-15 02:36:16', 75.00, 0.00, 75.00, 75.00, 0.00, 'PAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'FACULTY', 3, '2026-06-15 02:40:42', NULL, NULL, '2026-06-15 03:00:33', 'NONE', 'CASH'),
	(910, 'ORD-20260620140501329', 5, 13, 'PICKUP', NULL, '2026-06-20 06:05:01', 130.00, 0.00, 130.00, 0.00, 130.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(911, 'ORD-20260620140523798', 5, 13, 'PICKUP', NULL, '2026-06-20 06:05:23', 125.00, 0.00, 125.00, 0.00, 125.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, '2026-06-20 06:08:17', '2026-06-20 06:08:21', NULL, '2026-06-20 06:08:59', 'NONE', 'CASH'),
	(912, 'ORD-20260620141003013', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:03', 85.00, 0.00, 85.00, 0.00, 85.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-20 06:12:27', NULL, '2026-06-20 06:12:41', 'NONE', 'CASH'),
	(913, 'ORD-20260620141006549', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:06', 90.00, 0.00, 90.00, 0.00, 90.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-20 06:12:24', NULL, '2026-06-20 06:12:38', 'NONE', 'CASH'),
	(914, 'ORD-20260620141009900', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:09', 40.00, 0.00, 40.00, 0.00, 40.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-20 06:12:21', NULL, '2026-06-20 06:12:30', 'NONE', 'CASH'),
	(915, 'ORD-20260620141014190', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:14', 75.00, 0.00, 75.00, 0.00, 75.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-20 06:12:20', NULL, '2026-06-20 06:12:29', 'NONE', 'CASH'),
	(916, 'ORD-20260620141017070', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:17', 95.00, 0.00, 95.00, 0.00, 95.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-20 06:12:20', NULL, '2026-06-20 06:12:30', 'NONE', 'CASH'),
	(917, 'ORD-20260620141019533', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:19', 95.00, 0.00, 95.00, 0.00, 95.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-20 06:12:19', NULL, '2026-06-20 06:12:32', 'NONE', 'CASH'),
	(918, 'ORD-20260620141022868', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:22', 240.00, 0.00, 240.00, 0.00, 240.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-20 06:12:17', NULL, '2026-06-20 06:12:35', 'NONE', 'CASH'),
	(919, 'ORD-20260620141025005', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:25', 120.00, 0.00, 120.00, 0.00, 120.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-20 06:12:16', NULL, '2026-06-20 06:12:32', 'NONE', 'CASH'),
	(920, 'ORD-20260620141028141', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:28', 90.00, 0.00, 90.00, 0.00, 90.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-20 06:12:18', NULL, '2026-06-20 06:12:31', 'NONE', 'CASH'),
	(921, 'ORD-20260620141031885', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:31', 80.00, 0.00, 80.00, 0.00, 80.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, '2026-06-20 06:12:12', '2026-06-20 06:12:23', NULL, '2026-06-20 06:12:40', 'NONE', 'CASH'),
	(922, 'ORD-20260620141035221', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:35', 40.00, 0.00, 40.00, 0.00, 40.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, '2026-06-20 06:12:13', '2026-06-20 06:12:13', NULL, '2026-06-20 06:12:34', 'NONE', 'CASH'),
	(923, 'ORD-20260620141038190', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:38', 40.00, 0.00, 40.00, 0.00, 40.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-20 06:12:15', NULL, '2026-06-20 06:12:33', 'NONE', 'CASH'),
	(924, 'ORD-20260620141042349', 5, 13, 'PICKUP', NULL, '2026-06-20 06:10:42', 75.00, 0.00, 75.00, 0.00, 75.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, '2026-06-20 06:12:11', '2026-06-20 06:12:25', NULL, '2026-06-20 06:12:39', 'NONE', 'CASH'),
	(925, 'ORD-20260620141107221', 5, 13, 'PICKUP', NULL, '2026-06-20 06:11:07', 90.00, 0.00, 90.00, 0.00, 90.00, 'UNPAID', 'COMPLETED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, '2026-06-20 06:12:09', '2026-06-20 06:12:26', NULL, '2026-06-20 06:12:38', 'NONE', 'CASH'),
	(926, 'ORD-20260620145745163', 5, 13, 'PICKUP', NULL, '2026-06-20 06:57:45', 65.00, 0.00, 65.00, 65.00, 0.00, 'PAID', 'READY', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, '2026-06-24 10:18:05', '2026-06-24 10:52:36', NULL, NULL, 'NONE', 'CASH'),
	(927, 'ORD-20260624065759107', 1, 13, 'PICKUP', NULL, '2026-06-23 22:57:59', 65.00, 0.00, 65.00, 65.00, 0.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(928, 'ORD-20260624065810409', 1, 13, 'PICKUP', NULL, '2026-06-23 22:58:10', 65.00, 0.00, 65.00, 0.00, 65.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(929, 'ORD-20260624065818127', 1, 13, 'PICKUP', NULL, '2026-06-23 22:58:18', 65.00, 0.00, 65.00, 0.00, 65.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(930, 'ORD-20260624065821246', 1, 13, 'PICKUP', NULL, '2026-06-23 22:58:21', 45.00, 0.00, 45.00, 0.00, 45.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(931, 'ORD-20260624140303251', 1, 13, 'PICKUP', NULL, '2026-06-24 06:03:03', 65.00, 0.00, 65.00, 0.00, 65.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(932, 'ORD-20260624140435520', 1, 13, 'PICKUP', NULL, '2026-06-24 06:04:35', 45.00, 0.00, 45.00, 0.00, 45.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(933, 'ORD-20260624141738912', 1, 13, 'PICKUP', NULL, '2026-06-24 06:17:38', 65.00, 0.00, 65.00, 65.00, 0.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(934, 'ORD-20260624141806348', 1, 13, 'PICKUP', NULL, '2026-06-24 06:18:06', 65.00, 0.00, 65.00, 65.00, 0.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(935, 'ORD-20260624141830252', 1, 13, 'PICKUP', NULL, '2026-06-24 06:18:30', 75.00, 0.00, 75.00, 0.00, 75.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(936, 'ORD-20260624141839236', 1, 13, 'PICKUP', NULL, '2026-06-24 06:18:39', 25.00, 0.00, 25.00, 0.00, 25.00, 'UNPAID', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(937, 'ORD-20260624144420712', 1, 13, 'PICKUP', NULL, '2026-06-24 06:44:20', 75.00, 0.00, 75.00, 75.00, 0.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CANTEEN PREPAID WALLET'),
	(938, 'ORD-20260624183257041', 1, 13, 'PICKUP', NULL, '2026-06-24 10:32:57', 190.00, 0.00, 190.00, 190.00, 0.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CANTEEN PREPAID WALLET'),
	(939, 'ORD-20260624183341405', 1, 13, 'PICKUP', NULL, '2026-06-24 10:33:41', 90.00, 0.00, 90.00, 0.00, 90.00, 'PENDING', 'READY', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, '2026-06-24 10:36:02', '2026-06-24 10:52:37', NULL, NULL, 'NONE', 'CASH'),
	(940, 'ORD-20260624191942081', 1, 13, 'PICKUP', NULL, '2026-06-24 11:19:42', 45.00, 0.00, 45.00, 0.00, 45.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'BANK TRANSFER'),
	(941, 'ORD-20260624191945595', 1, 13, 'PICKUP', NULL, '2026-06-24 11:19:45', 45.00, 0.00, 45.00, 0.00, 45.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'BANK TRANSFER'),
	(942, 'ORD-20260624192034170', 1, 13, 'PICKUP', NULL, '2026-06-24 11:20:34', 45.00, 0.00, 45.00, 0.00, 45.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'GCASH / MAYA'),
	(943, 'ORD-20260624192636939', 1, 13, 'PICKUP', NULL, '2026-06-24 11:26:36', 20.00, 0.00, 20.00, 0.00, 20.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'BANK TRANSFER'),
	(944, 'ORD-20260624192649185', 1, 13, 'PICKUP', NULL, '2026-06-24 11:26:49', 25.00, 0.00, 25.00, 0.00, 25.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'GCASH / MAYA'),
	(945, 'ORD-20260624192657449', 1, 13, 'PICKUP', NULL, '2026-06-24 11:26:57', 85.00, 0.00, 85.00, 0.00, 85.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(946, 'ORD-20260624193118111', 1, 13, 'PICKUP', NULL, '2026-06-24 11:31:18', 45.00, 0.00, 45.00, 45.00, 0.00, 'PAID', 'READY', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, '2026-06-24 11:32:15', NULL, NULL, 'NONE', 'BANK TRANSFER'),
	(947, 'ORD-20260624193345141', 1, 13, 'PICKUP', NULL, '2026-06-24 11:33:45', 20.00, 0.00, 20.00, 20.00, 0.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, '2026-06-24 11:35:03', NULL, NULL, NULL, 'NONE', 'BANK TRANSFER'),
	(948, 'ORD-20260624224933884', 1, 13, 'PICKUP', NULL, '2026-06-24 14:49:33', 85.00, 0.00, 85.00, 0.00, 85.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(949, 'ORD-20260624224939708', 1, 13, 'PICKUP', NULL, '2026-06-24 14:49:39', 85.00, 0.00, 85.00, 0.00, 85.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(950, 'ORD-20260624224944186', 1, 13, 'PICKUP', NULL, '2026-06-24 14:49:44', 85.00, 0.00, 85.00, 0.00, 85.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(951, 'ORD-20260624224948411', 1, 13, 'PICKUP', NULL, '2026-06-24 14:49:48', 75.00, 0.00, 75.00, 0.00, 75.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(952, 'ORD-20260624224951666', 1, 13, 'PICKUP', NULL, '2026-06-24 14:49:51', 20.00, 0.00, 20.00, 0.00, 20.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(953, 'ORD-20260624224955713', 1, 13, 'PICKUP', NULL, '2026-06-24 14:49:55', 85.00, 0.00, 85.00, 0.00, 85.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(954, 'ORD-20260624224958626', 1, 13, 'PICKUP', NULL, '2026-06-24 14:49:58', 20.00, 0.00, 20.00, 0.00, 20.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(955, 'ORD-20260624225001322', 1, 13, 'PICKUP', NULL, '2026-06-24 14:50:01', 20.00, 0.00, 20.00, 0.00, 20.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(956, 'ORD-20260624225003706', 1, 13, 'PICKUP', NULL, '2026-06-24 14:50:03', 20.00, 0.00, 20.00, 0.00, 20.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(957, 'ORD-20260624225006353', 1, 13, 'PICKUP', NULL, '2026-06-24 14:50:06', 25.00, 0.00, 25.00, 0.00, 25.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(958, 'ORD-20260624225009875', 1, 13, 'PICKUP', NULL, '2026-06-24 14:50:09', 20.00, 0.00, 20.00, 0.00, 20.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(959, 'ORD-20260624225012490', 1, 13, 'PICKUP', NULL, '2026-06-24 14:50:12', 20.00, 0.00, 20.00, 0.00, 20.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CASH'),
	(960, 'ORD-20260625000140373', 1, 13, 'PICKUP', NULL, '2026-06-24 16:01:40', 65.00, 10.00, 55.00, 55.00, 0.00, 'PAID', 'PENDING', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CANTEEN PREPAID WALLET'),
	(961, 'ORD-20260625000156177', 1, 13, 'PICKUP', NULL, '2026-06-24 16:01:56', 65.00, 0.00, 65.00, 65.00, 0.00, 'REFUNDED', 'CANCELLED', NULL, 'Scheduled pickup', 0, 'REGULAR', 3, NULL, NULL, NULL, NULL, 'NONE', 'CANTEEN PREPAID WALLET');

-- Dumping structure for table canteen_sales_inventory_system.order_items
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE IF NOT EXISTS `order_items` (
  `order_item_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `product_id` int NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(12,2) NOT NULL,
  `line_total` decimal(12,2) NOT NULL,
  `special_instruction` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`order_item_id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `order_items_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`) ON DELETE CASCADE,
  CONSTRAINT `order_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.order_items: ~52 rows (approximately)
INSERT INTO `order_items` (`order_item_id`, `order_id`, `product_id`, `quantity`, `unit_price`, `line_total`, `special_instruction`) VALUES
	(1, 101, 8, 1, 95.00, 95.00, 'Extra garlic fried rice'),
	(2, 101, 4, 1, 25.00, 25.00, 'Less ice'),
	(3, 102, 7, 1, 75.00, 75.00, 'Make it breast part'),
	(4, 102, 15, 1, 40.00, 40.00, 'No extra sugar'),
	(5, 103, 6, 1, 85.00, 85.00, 'Crispy look'),
	(8, 501, 5, 1, 120.00, 120.00, 'Extra creamy, no fork needed'),
	(9, 502, 6, 1, 95.00, 95.00, 'Toasted bread extra crisp'),
	(10, 503, 2, 1, 90.00, 90.00, 'Less Ice'),
	(11, 503, 3, 1, 75.00, 75.00, 'No sugar added'),
	(12, 504, 7, 1, 45.00, 45.00, ''),
	(16, 601, 8, 2, 120.00, 240.00, NULL),
	(19, 701, 8, 2, 120.00, 240.00, NULL),
	(20, 702, 12, 1, 150.00, 150.00, NULL),
	(21, 703, 16, 1, 90.00, 90.00, NULL),
	(22, 704, 22, 1, 45.00, 45.00, NULL),
	(27, 801, 16, 2, 85.00, 170.00, NULL),
	(29, 901, 3, 2, 75.00, 150.00, NULL),
	(33, 905, 8, 1, 95.00, 95.00, ''),
	(34, 905, 3, 1, 45.00, 45.00, ''),
	(35, 906, 3, 1, 45.00, 45.00, ''),
	(36, 907, 19, 1, 80.00, 80.00, ''),
	(37, 908, 20, 1, 120.00, 120.00, ''),
	(38, 909, 7, 1, 75.00, 75.00, ''),
	(39, 910, 15, 1, 40.00, 40.00, ''),
	(40, 910, 17, 1, 90.00, 90.00, ''),
	(41, 911, 16, 1, 85.00, 85.00, ''),
	(42, 911, 15, 1, 40.00, 40.00, ''),
	(43, 912, 16, 1, 85.00, 85.00, ''),
	(44, 913, 17, 1, 90.00, 90.00, ''),
	(45, 914, 15, 1, 40.00, 40.00, ''),
	(46, 915, 18, 1, 75.00, 75.00, ''),
	(47, 916, 21, 1, 95.00, 95.00, ''),
	(48, 917, 21, 1, 95.00, 95.00, ''),
	(49, 918, 20, 2, 120.00, 240.00, ''),
	(50, 919, 20, 1, 120.00, 120.00, ''),
	(51, 920, 17, 1, 90.00, 90.00, ''),
	(52, 921, 19, 1, 80.00, 80.00, ''),
	(53, 922, 15, 1, 40.00, 40.00, ''),
	(54, 923, 15, 1, 40.00, 40.00, ''),
	(55, 924, 18, 1, 75.00, 75.00, ''),
	(56, 925, 17, 1, 90.00, 90.00, ''),
	(57, 926, 2, 1, 65.00, 65.00, ''),
	(58, 927, 2, 1, 65.00, 65.00, ''),
	(59, 928, 2, 1, 65.00, 65.00, ''),
	(60, 929, 2, 1, 65.00, 65.00, ''),
	(61, 930, 3, 1, 45.00, 45.00, ''),
	(62, 931, 2, 1, 65.00, 65.00, ''),
	(63, 932, 3, 1, 45.00, 45.00, ''),
	(64, 933, 2, 1, 65.00, 65.00, ''),
	(65, 934, 2, 1, 65.00, 65.00, ''),
	(66, 935, 1, 1, 75.00, 75.00, ''),
	(67, 936, 4, 1, 25.00, 25.00, ''),
	(68, 937, 1, 1, 75.00, 75.00, ''),
	(69, 938, 21, 2, 95.00, 190.00, ''),
	(70, 939, 17, 1, 90.00, 90.00, ''),
	(71, 940, 3, 1, 45.00, 45.00, ''),
	(72, 941, 3, 1, 45.00, 45.00, ''),
	(73, 942, 3, 1, 45.00, 45.00, ''),
	(74, 943, 5, 1, 20.00, 20.00, ''),
	(75, 944, 4, 1, 25.00, 25.00, ''),
	(76, 945, 6, 1, 85.00, 85.00, ''),
	(77, 946, 3, 1, 45.00, 45.00, ''),
	(78, 947, 5, 1, 20.00, 20.00, ''),
	(79, 948, 6, 1, 85.00, 85.00, ''),
	(80, 949, 6, 1, 85.00, 85.00, ''),
	(81, 950, 6, 1, 85.00, 85.00, ''),
	(82, 951, 7, 1, 75.00, 75.00, ''),
	(83, 952, 5, 1, 20.00, 20.00, ''),
	(84, 953, 6, 1, 85.00, 85.00, ''),
	(85, 954, 5, 1, 20.00, 20.00, ''),
	(86, 955, 5, 1, 20.00, 20.00, ''),
	(87, 956, 5, 1, 20.00, 20.00, ''),
	(88, 957, 4, 1, 25.00, 25.00, ''),
	(89, 958, 5, 1, 20.00, 20.00, ''),
	(90, 959, 5, 1, 20.00, 20.00, ''),
	(91, 960, 2, 1, 65.00, 65.00, ''),
	(92, 961, 2, 1, 65.00, 65.00, '');

-- Dumping structure for table canteen_sales_inventory_system.payments
DROP TABLE IF EXISTS `payments`;
CREATE TABLE IF NOT EXISTS `payments` (
  `payment_id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL,
  `customer_id` int DEFAULT NULL,
  `payment_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(12,2) NOT NULL,
  `method` enum('CASH','GCASH','MAYA','BANK_TRANSFER','E_WALLET') COLLATE utf8mb4_unicode_ci NOT NULL,
  `reference_no` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `proof_reference` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('PENDING','VERIFIED','REJECTED') COLLATE utf8mb4_unicode_ci DEFAULT 'PENDING',
  `verified_by` int DEFAULT NULL,
  `verified_at` timestamp NULL DEFAULT NULL,
  `remarks` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`payment_id`),
  KEY `order_id` (`order_id`),
  KEY `customer_id` (`customer_id`),
  KEY `verified_by` (`verified_by`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `payments_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customer_profiles` (`customer_id`),
  CONSTRAINT `payments_ibfk_3` FOREIGN KEY (`verified_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.payments: ~5 rows (approximately)
INSERT INTO `payments` (`payment_id`, `order_id`, `customer_id`, `payment_date`, `amount`, `method`, `reference_no`, `proof_reference`, `status`, `verified_by`, `verified_at`, `remarks`) VALUES
	(3, 701, 1, '2026-06-11 00:38:11', 240.00, 'CASH', NULL, NULL, 'VERIFIED', NULL, NULL, NULL),
	(4, 906, 4, '2026-06-14 10:48:48', 1000.00, 'GCASH', '123456', '', 'VERIFIED', 1, '2026-06-14 10:49:19', NULL),
	(5, 907, 1, '2026-06-15 02:34:55', 80.00, 'CASH', '', '', 'VERIFIED', 1, '2026-06-24 10:53:03', NULL),
	(6, 908, 1, '2026-06-15 02:35:10', 120.00, 'CASH', '', '', 'VERIFIED', 1, '2026-06-15 02:39:59', NULL),
	(7, 909, 1, '2026-06-15 02:36:28', 75.00, 'CASH', '', '', 'VERIFIED', 1, '2026-06-15 02:39:56', NULL),
	(8, 946, 1, '2026-06-24 11:31:45', 45.00, 'BANK_TRANSFER', '12345', '', 'VERIFIED', 1, '2026-06-24 11:32:03', NULL),
	(9, 947, 1, '2026-06-24 11:34:38', 20.00, 'BANK_TRANSFER', '12345', '', 'VERIFIED', 1, '2026-06-24 11:34:49', NULL);

-- Dumping structure for table canteen_sales_inventory_system.pickup_slots
DROP TABLE IF EXISTS `pickup_slots`;
CREATE TABLE IF NOT EXISTS `pickup_slots` (
  `slot_id` int NOT NULL AUTO_INCREMENT,
  `slot_time` time NOT NULL,
  `max_order_capacity` int NOT NULL DEFAULT '10',
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  PRIMARY KEY (`slot_id`),
  UNIQUE KEY `slot_time` (`slot_time`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.pickup_slots: ~15 rows (approximately)
INSERT INTO `pickup_slots` (`slot_id`, `slot_time`, `max_order_capacity`, `status`) VALUES
	(1, '11:00:00', 8, 'ACTIVE'),
	(2, '11:15:00', 8, 'ACTIVE'),
	(3, '11:30:00', 10, 'ACTIVE'),
	(4, '11:45:00', 10, 'ACTIVE'),
	(5, '12:00:00', 15, 'ACTIVE'),
	(6, '12:15:00', 15, 'ACTIVE'),
	(7, '12:30:00', 15, 'ACTIVE'),
	(8, '12:45:00', 15, 'ACTIVE'),
	(9, '13:00:00', 10, 'ACTIVE'),
	(10, '13:15:00', 10, 'ACTIVE'),
	(11, '13:30:00', 8, 'ACTIVE'),
	(12, '13:45:00', 8, 'ACTIVE'),
	(13, '08:00:00', 30, 'ACTIVE'),
	(14, '10:00:00', 30, 'ACTIVE'),
	(15, '15:00:00', 25, 'ACTIVE');

-- Dumping structure for table canteen_sales_inventory_system.products
DROP TABLE IF EXISTS `products`;
CREATE TABLE IF NOT EXISTS `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `product_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `barcode` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `preparation_time_minutes` int DEFAULT '5',
  `status` enum('AVAILABLE','UNAVAILABLE','INACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT 'AVAILABLE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `calories` int NOT NULL DEFAULT '0',
  `carbohydrates` decimal(10,2) NOT NULL DEFAULT '0.00',
  `protein` decimal(10,2) NOT NULL DEFAULT '0.00',
  `fat` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sugar` decimal(10,2) NOT NULL DEFAULT '0.00',
  `sodium` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `barcode` (`barcode`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.products: ~22 rows (approximately)
INSERT INTO `products` (`product_id`, `category_id`, `product_name`, `description`, `barcode`, `unit_price`, `preparation_time_minutes`, `status`, `created_at`, `calories`, `carbohydrates`, `protein`, `fat`, `sugar`, `sodium`) VALUES
	(1, 1, 'Chicken Adobo Meal', 'Rice with chicken adobo', 'CSIS-001', 75.00, 10, 'UNAVAILABLE', '2026-05-25 15:11:24', 520, 48.00, 32.00, 14.00, 2.00, 300.00),
	(2, 1, 'Pork Siomai Rice', 'Rice with steamed siomai', 'CSIS-002', 65.00, 8, 'AVAILABLE', '2026-05-25 15:11:24', 460, 52.00, 18.00, 16.00, 1.50, 250.00),
	(3, 2, 'Cheese Sandwich', 'Toasted sandwich', 'CSIS-003', 45.00, 5, 'AVAILABLE', '2026-05-25 15:11:24', 280, 26.00, 11.00, 12.00, 3.50, 430.00),
	(4, 3, 'Iced Tea', '16oz house iced tea', 'CSIS-004', 25.00, 2, 'AVAILABLE', '2026-05-25 15:11:24', 90, 22.00, 0.00, 0.00, 20.00, 10.00),
	(5, 4, 'Banana Turon', 'Sweet fried banana', 'CSIS-005', 20.00, 3, 'AVAILABLE', '2026-05-25 15:11:24', 160, 34.00, 1.50, 4.00, 18.00, 45.00),
	(6, 1, 'Crispy Pork Sisig Bowl', 'Traditional minced pork sizzled with white onions, chili, and egg over steamed rice.', 'CSIS-006', 85.00, 12, 'AVAILABLE', '2026-06-08 04:20:53', 580, 45.00, 22.00, 32.00, 2.50, 570.00),
	(7, 1, 'Chicken Adobo Rice Pack', 'Slow-cooked chicken thigh simmered in soy sauce, vinegar, garlic, and bay leaves.', 'CSIS-007', 75.00, 8, 'AVAILABLE', '2026-06-08 04:20:53', 490, 50.00, 28.00, 15.00, 3.00, 720.00),
	(8, 1, 'Beef Pares Combo', 'Braised beef brisket in sweet anise-infused gravy served with garlic fried rice and clear broth.', 'CSIS-008', 95.00, 10, 'AVAILABLE', '2026-06-08 04:20:53', 620, 55.00, 30.00, 18.00, 6.00, 810.00),
	(9, 1, 'Ginataang Sitaw at Kalabasa', 'Healthy string beans and squash simmered in rich coconut cream, served over brown rice.', 'CSIS-009', 65.00, 15, 'AVAILABLE', '2026-06-08 04:20:53', 340, 42.00, 8.00, 12.00, 4.50, 390.00),
	(10, 2, 'Pancit Guisado Plate', 'Stir-fried bihon and canton noodles packed with shredded vegetables and chicken strips.', 'CSIS-0010', 45.00, 7, 'AVAILABLE', '2026-06-08 04:20:53', 290, 38.00, 10.00, 6.00, 1.50, 450.00),
	(11, 2, 'Cheesy Baked Macaroni', 'Elbow macaroni tossed in meat sauce, topped with a thick, creamy melted cheese layer.', 'CSIS-011', 55.00, 5, 'AVAILABLE', '2026-06-08 04:20:53', 410, 48.00, 14.00, 16.00, 5.00, 540.00),
	(12, 2, 'Toasted Ham & Cheese Sandwich', 'Double-decker white bread with sliced sweet ham, cheddar cheese, and light mayo spread.', 'CSIS-012', 35.00, 4, 'AVAILABLE', '2026-06-08 04:20:53', 260, 28.00, 12.00, 9.00, 2.00, 410.00),
	(13, 2, 'Crispy Vegetable Lumpia (2pcs)', 'Deep-fried spring rolls stuffed with bean sprouts, carrots, and sweet potato, paired with vinegar.', 'CSIS-013', 30.00, 6, 'AVAILABLE', '2026-06-08 04:20:53', 180, 22.00, 4.00, 8.00, 2.50, 280.00),
	(14, 3, 'House Blend Iced Tea (L)', 'Freshly brewed black tea infused with sweet calamansi extract, served cold over ice.', 'CSIS-014', 25.00, 2, 'AVAILABLE', '2026-06-08 04:20:53', 120, 30.00, 0.00, 0.00, 28.00, 15.00),
	(15, 3, 'Creamy Buko Pandan Shake', 'Blended beverage made with real coconut strands, green gelatin cubes, and sweet condensed milk.', 'CSIS-015', 40.00, 3, 'AVAILABLE', '2026-06-08 04:20:53', 240, 35.00, 3.00, 7.00, 31.00, 45.00),
	(16, 1, 'Wintermelon Milk Tea', 'Large Size', 'MT001', 85.00, 5, 'AVAILABLE', '2026-06-11 00:14:31', 320, 42.00, 3.50, 8.00, 38.00, 35.00),
	(17, 1, 'Okinawa Milk Tea', 'Large Size', 'MT002', 90.00, 5, 'AVAILABLE', '2026-06-11 00:14:31', 340, 45.00, 3.80, 9.00, 40.00, 40.00),
	(18, 2, 'Iced Coffee', 'Cold Coffee', 'CF001', 75.00, 3, 'AVAILABLE', '2026-06-11 00:14:31', 150, 28.00, 1.00, 2.00, 22.00, 15.00),
	(19, 2, 'Cappuccino', 'Hot Coffee', 'CF002', 80.00, 5, 'AVAILABLE', '2026-06-11 00:14:31', 180, 24.00, 4.50, 6.00, 18.00, 55.00),
	(20, 3, 'Carbonara', 'Creamy Pasta', 'PS001', 120.00, 10, 'AVAILABLE', '2026-06-11 00:14:31', 540, 58.00, 18.00, 24.00, 4.00, 620.00),
	(21, 4, 'Chicken Sandwich', 'Club Sandwich', 'SW001', 95.00, 8, 'AVAILABLE', '2026-06-11 00:14:31', 380, 32.00, 22.00, 12.00, 3.50, 510.00),
	(22, 5, 'Chocolate Muffin', 'Fresh Muffin', 'PF001', 45.00, 2, 'AVAILABLE', '2026-06-11 00:14:31', 290, 38.00, 4.00, 10.00, 24.00, 180.00);

-- Dumping structure for table canteen_sales_inventory_system.product_promotions
DROP TABLE IF EXISTS `product_promotions`;
CREATE TABLE IF NOT EXISTS `product_promotions` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `promotion_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_id` (`product_id`,`promotion_id`),
  KEY `promotion_id` (`promotion_id`),
  CONSTRAINT `product_promotions_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`),
  CONSTRAINT `product_promotions_ibfk_2` FOREIGN KEY (`promotion_id`) REFERENCES `promotions` (`promotion_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.product_promotions: ~2 rows (approximately)
INSERT INTO `product_promotions` (`id`, `product_id`, `promotion_id`) VALUES
	(1, 1, 1),
	(2, 2, 1);

-- Dumping structure for table canteen_sales_inventory_system.promotions
DROP TABLE IF EXISTS `promotions`;
CREATE TABLE IF NOT EXISTS `promotions` (
  `promotion_id` int NOT NULL AUTO_INCREMENT,
  `promo_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `promo_code` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `discount_type` enum('PERCENT','FIXED') COLLATE utf8mb4_unicode_ci DEFAULT 'PERCENT',
  `discount_value` decimal(10,2) NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  PRIMARY KEY (`promotion_id`),
  UNIQUE KEY `promo_code` (`promo_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.promotions: ~3 rows (approximately)
INSERT INTO `promotions` (`promotion_id`, `promo_name`, `promo_code`, `discount_type`, `discount_value`, `start_date`, `end_date`, `status`) VALUES
	(1, 'Student Meal Discount', 'STUDENT10', 'PERCENT', 10.00, '2026-05-01', '2026-12-31', 'ACTIVE'),
	(2, 'Opening Promo', 'OPEN10', 'PERCENT', 10.00, '2026-01-01', '2026-12-31', 'ACTIVE'),
	(3, 'Student Discount', 'STUD20', 'PERCENT', 20.00, '2026-01-01', '2026-12-31', 'ACTIVE');

-- Dumping structure for table canteen_sales_inventory_system.purchase_items
DROP TABLE IF EXISTS `purchase_items`;
CREATE TABLE IF NOT EXISTS `purchase_items` (
  `purchase_item_id` int NOT NULL AUTO_INCREMENT,
  `purchase_id` int NOT NULL,
  `ingredient_id` int NOT NULL,
  `quantity` decimal(12,3) NOT NULL,
  `unit_cost` decimal(12,2) NOT NULL,
  `line_total` decimal(12,2) NOT NULL,
  `batch_number` varchar(80) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  PRIMARY KEY (`purchase_item_id`),
  KEY `purchase_id` (`purchase_id`),
  KEY `ingredient_id` (`ingredient_id`),
  CONSTRAINT `purchase_items_ibfk_1` FOREIGN KEY (`purchase_id`) REFERENCES `purchase_orders` (`purchase_id`) ON DELETE CASCADE,
  CONSTRAINT `purchase_items_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`ingredient_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.purchase_items: ~2 rows (approximately)
INSERT INTO `purchase_items` (`purchase_item_id`, `purchase_id`, `ingredient_id`, `quantity`, `unit_cost`, `line_total`, `batch_number`, `expiry_date`) VALUES
	(1, 1, 1, 100.000, 45.00, 4500.00, 'BATCH-SUG-01', NULL),
	(2, 2, 6, 20.000, 160.00, 3200.00, 'BATCH-CHK-02', NULL);

-- Dumping structure for table canteen_sales_inventory_system.purchase_orders
DROP TABLE IF EXISTS `purchase_orders`;
CREATE TABLE IF NOT EXISTS `purchase_orders` (
  `purchase_id` int NOT NULL AUTO_INCREMENT,
  `supplier_id` int NOT NULL,
  `order_date` date NOT NULL,
  `expected_date` date DEFAULT NULL,
  `received_date` date DEFAULT NULL,
  `total_amount` decimal(12,2) DEFAULT '0.00',
  `status` enum('DRAFT','ORDERED','PARTIALLY_RECEIVED','RECEIVED','CANCELLED') COLLATE utf8mb4_unicode_ci DEFAULT 'DRAFT',
  `created_by` int DEFAULT NULL,
  `notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`purchase_id`),
  KEY `supplier_id` (`supplier_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `purchase_orders_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`),
  CONSTRAINT `purchase_orders_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.purchase_orders: ~3 rows (approximately)
INSERT INTO `purchase_orders` (`purchase_id`, `supplier_id`, `order_date`, `expected_date`, `received_date`, `total_amount`, `status`, `created_by`, `notes`) VALUES
	(1, 1, '2026-06-01', '2026-06-03', '2026-06-03', 4500.00, 'RECEIVED', 1, 'Bulk monthly dry goods'),
	(2, 2, '2026-06-02', '2026-06-04', '2026-06-04', 3200.00, 'RECEIVED', 1, 'Fresh meat and poultry restock'),
	(3, 4, '2026-06-05', '2026-06-07', NULL, 1500.00, 'ORDERED', 1, 'Awaiting morning delivery dropoff');

-- Dumping structure for table canteen_sales_inventory_system.recipes
DROP TABLE IF EXISTS `recipes`;
CREATE TABLE IF NOT EXISTS `recipes` (
  `recipe_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `ingredient_id` int NOT NULL,
  `quantity_required` decimal(12,3) NOT NULL,
  PRIMARY KEY (`recipe_id`),
  UNIQUE KEY `product_id` (`product_id`,`ingredient_id`),
  KEY `ingredient_id` (`ingredient_id`),
  KEY `idx_product_lookup` (`product_id`,`ingredient_id`),
  CONSTRAINT `recipes_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE,
  CONSTRAINT `recipes_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`ingredient_id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.recipes: ~21 rows (approximately)
INSERT INTO `recipes` (`recipe_id`, `product_id`, `ingredient_id`, `quantity_required`) VALUES
	(1, 1, 1, 0.200),
	(2, 1, 2, 0.150),
	(3, 1, 3, 0.050),
	(4, 2, 1, 0.200),
	(5, 2, 4, 4.000),
	(6, 3, 5, 2.000),
	(7, 3, 6, 1.000),
	(8, 4, 7, 20.000),
	(9, 4, 8, 1.000),
	(10, 5, 9, 1.000),
	(11, 1, 6, 0.030),
	(12, 3, 2, 0.040),
	(13, 3, 1, 0.020),
	(14, 5, 8, 0.200),
	(15, 5, 7, 0.050),
	(16, 6, 5, 1.000),
	(17, 6, 6, 0.150),
	(18, 12, 5, 1.000),
	(19, 12, 6, 1.000),
	(20, 22, 14, 1.000),
	(21, 18, 11, 0.290);

-- Dumping structure for table canteen_sales_inventory_system.stock_movements
DROP TABLE IF EXISTS `stock_movements`;
CREATE TABLE IF NOT EXISTS `stock_movements` (
  `movement_id` int NOT NULL AUTO_INCREMENT,
  `ingredient_id` int NOT NULL,
  `movement_type` enum('PURCHASE','SALE_USAGE','ADJUSTMENT','WASTE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `reference_table` varchar(40) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reference_id` int DEFAULT NULL,
  `quantity_change` decimal(12,3) NOT NULL,
  `movement_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `remarks` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_by` int DEFAULT NULL,
  PRIMARY KEY (`movement_id`),
  KEY `ingredient_id` (`ingredient_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `stock_movements_ibfk_1` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`ingredient_id`),
  CONSTRAINT `stock_movements_ibfk_2` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.stock_movements: ~3 rows (approximately)
INSERT INTO `stock_movements` (`movement_id`, `ingredient_id`, `movement_type`, `reference_table`, `reference_id`, `quantity_change`, `movement_date`, `remarks`, `created_by`) VALUES
	(1, 1, '', 'ingredient_batches', 1, 100.000, '2026-06-11 00:29:45', 'Initial supplier intake delivery', 1),
	(2, 2, '', 'ingredient_batches', 2, 50.000, '2026-06-11 00:29:45', 'Fresh batch beans logging', 1),
	(3, 2, '', 'orders', 503, -5.000, '2026-06-11 00:29:45', 'Deducted via Faculty Coffee Purchase', 1),
	(4, 1, 'SALE_USAGE', 'orders', 701, -0.500, '2026-06-11 00:38:11', 'Deducted automatically via Beef Pares sales pass', 1),
	(5, 2, 'PURCHASE', NULL, NULL, 10.000, '2026-06-24 13:35:37', 'Received stock', 1);

-- Dumping structure for table canteen_sales_inventory_system.suppliers
DROP TABLE IF EXISTS `suppliers`;
CREATE TABLE IF NOT EXISTS `suppliers` (
  `supplier_id` int NOT NULL AUTO_INCREMENT,
  `supplier_name` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contact_person` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(120) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`supplier_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.suppliers: ~7 rows (approximately)
INSERT INTO `suppliers` (`supplier_id`, `supplier_name`, `contact_person`, `phone`, `email`, `address`, `status`, `created_at`) VALUES
	(1, 'Fresh Farm Supplies', 'Ana Cruz', '09181110000', 'freshfarm@example.com', 'San Mateo', 'ACTIVE', '2026-05-25 15:11:24'),
	(2, 'Campus Wholesale Depot', 'Ben Lim', '09182220000', 'depot@example.com', 'Marikina', 'ACTIVE', '2026-05-25 15:11:24'),
	(3, 'ABC Food Supply', 'Pedro Santos', '09171111111', 'abc@gmail.com', 'Manila', 'ACTIVE', '2026-06-11 00:14:31'),
	(4, 'Fresh Farm Foods', 'Maria Cruz', '09172222222', 'farm@gmail.com', 'Bulacan', 'ACTIVE', '2026-06-11 00:14:31'),
	(5, 'Golden Harvest', 'John Reyes', '09173333333', 'golden@gmail.com', 'Laguna', 'ACTIVE', '2026-06-11 00:14:31'),
	(6, 'Prime Ingredients', 'Mark Reyes', '09183333333', 'prime@gmail.com', 'Pasig', 'ACTIVE', '2026-06-11 00:15:32'),
	(7, 'Best Harvest', 'Liza Cruz', '09184444444', 'harvest@gmail.com', 'Laguna', 'ACTIVE', '2026-06-11 00:15:32');

-- Dumping structure for table canteen_sales_inventory_system.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` char(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `full_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` enum('ADMIN','STAFF','CUSTOMER') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'CUSTOMER',
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.users: ~8 rows (approximately)
INSERT INTO `users` (`user_id`, `username`, `password_hash`, `full_name`, `email`, `phone`, `address`, `role`, `status`, `created_at`, `updated_at`) VALUES
	(1, 'admin', 'e86f78a8a3caf0b60d8e74e5942aa6d86dc150cd3c03338aef25b7d2d7e3acc7', 'System Administrator', 'admin@canteen.local', '09170000001', 'School Campus', 'ADMIN', 'ACTIVE', '2026-05-25 15:11:24', '2026-05-25 15:11:24'),
	(2, 'staff1', 'e86f78a8a3caf0b60d8e74e5942aa6d86dc150cd3c03338aef25b7d2d7e3acc7', 'Canteen Cashier', 'staff@canteen.local', '09170000002', 'School Campus', 'STAFF', 'ACTIVE', '2026-05-25 15:11:24', '2026-05-25 15:11:24'),
	(3, 'customer1', '3e7c19576488862816f13b512cacf3e4ba97dd97243ea0bd6a2ad1642d86ba72', 'Juan Student', 'customer@canteen.local', '09170000003', 'Manila', 'CUSTOMER', 'ACTIVE', '2026-05-25 15:11:24', '2026-05-25 15:11:24'),
	(4, 'admin2', 'admin123', 'System Admin', 'admin2@gmail.com', '09170000001', 'Quezon City', 'ADMIN', 'ACTIVE', '2026-06-11 00:14:31', '2026-06-11 00:14:31'),
	(5, 'cashier1', 'cash123', 'Maria Santos', 'cashier1@gmail.com', '09170000002', 'Quezon City', 'STAFF', 'ACTIVE', '2026-06-11 00:14:31', '2026-06-11 00:36:29'),
	(6, 'cashier2', 'cash123', 'Juan Dela Cruz', 'cashier2@gmail.com', '09170000003', 'Quezon City', 'STAFF', 'ACTIVE', '2026-06-11 00:14:31', '2026-06-11 00:36:29'),
	(7, 'student1', '19b9dd3e24fad97f47400340f81e118ca3f88be2ee3503b34b9bde0ad5ad7ebd', 'Carlo Reyes', 'student1@gmail.com', '09170000004', 'Quezon City', 'CUSTOMER', 'ACTIVE', '2026-06-11 00:14:31', '2026-06-20 04:12:13'),
	(8, 'student2', 'stud123', 'Angela Cruz', 'student2@gmail.com', '09170000005', 'Quezon City', 'CUSTOMER', 'ACTIVE', '2026-06-11 00:14:31', '2026-06-11 00:14:31'),
	(9, 'Jeffrey', '8754cb9a658872aca7c3f1b8202224c270881c4721e68c3a48495b557646e3f9', 'Jeffrey Epstein', 'epstein@gmail.com', '03424425582', 'Little St. James Island', 'CUSTOMER', 'INACTIVE', '2026-06-14 09:58:20', '2026-06-20 04:52:22'),
	(10, 'test', 'ecd71870d1963316a97e3ac3408c9835ad8cf0f3c1bc703527c30265534f75ae', 'TEST', 'test@gmail.com', '0915344224345', 'Metro Manila', 'CUSTOMER', 'ACTIVE', '2026-06-20 04:33:01', '2026-06-20 05:49:27');

-- Dumping structure for view canteen_sales_inventory_system.v_current_inventory
DROP VIEW IF EXISTS `v_current_inventory`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_current_inventory` (
	`ingredient_id` INT NOT NULL,
	`ingredient_name` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`unit` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`quantity_on_hand` DECIMAL(12,3) NOT NULL,
	`reorder_level` DECIMAL(12,3) NOT NULL,
	`stock_status` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_general_ci'
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_daily_sales
DROP VIEW IF EXISTS `v_daily_sales`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_daily_sales` (
	`sale_date` DATE NULL,
	`order_count` BIGINT NOT NULL,
	`gross_sales` DECIMAL(34,2) NULL,
	`collected` DECIMAL(34,2) NULL
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_expiring_batches
DROP VIEW IF EXISTS `v_expiring_batches`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_expiring_batches` (
	`batch_id` INT NOT NULL,
	`ingredient_name` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`batch_number` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`expiry_date` DATE NULL,
	`quantity_available` DECIMAL(12,3) NOT NULL,
	`days_left` INT NULL
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_kitchen_display_queue
DROP VIEW IF EXISTS `v_kitchen_display_queue`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_kitchen_display_queue` (
	`order_id` INT NULL,
	`order_no` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`scheduled_pickup` TIME NULL,
	`customer_type` ENUM('REGULAR','FACULTY','PWD','SENIOR','EMERGENCY') NULL COLLATE 'utf8mb4_unicode_ci',
	`queue_priority` INT NULL,
	`order_status` ENUM('PENDING','CONFIRMED','PREPARING','READY','COMPLETED','CANCELLED','REJECTED') NULL COLLATE 'utf8mb4_unicode_ci',
	`prep_started_at` TIMESTAMP NULL,
	`total_estimated_prep_minutes` DECIMAL(32,0) NULL
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_low_stock
DROP VIEW IF EXISTS `v_low_stock`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_low_stock` (
	`ingredient_id` INT NOT NULL,
	`ingredient_name` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`unit` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`quantity_on_hand` DECIMAL(12,3) NOT NULL,
	`reorder_level` DECIMAL(12,3) NOT NULL,
	`stock_status` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_general_ci'
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_nutrition_analytics
DROP VIEW IF EXISTS `v_nutrition_analytics`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_nutrition_analytics` (
	`product_id` INT NOT NULL,
	`product_name` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`calories` INT NOT NULL,
	`sugar` DECIMAL(10,2) NOT NULL,
	`sodium` DECIMAL(10,2) NOT NULL,
	`compiled_allergens` TEXT NULL COLLATE 'utf8mb4_unicode_ci',
	`total_orders` DECIMAL(32,0) NOT NULL
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_order_summary
DROP VIEW IF EXISTS `v_order_summary`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_order_summary` (
	`order_id` INT NOT NULL,
	`order_no` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`customer` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`order_type` ENUM('WALK_IN','PICKUP','DINE_IN') NULL COLLATE 'utf8mb4_unicode_ci',
	`ordered_at` TIMESTAMP NULL,
	`total_amount` DECIMAL(12,2) NOT NULL,
	`amount_paid` DECIMAL(12,2) NOT NULL,
	`balance` DECIMAL(12,2) NOT NULL,
	`payment_status` ENUM('UNPAID','PENDING','PAID','REFUNDED') NULL COLLATE 'utf8mb4_unicode_ci',
	`order_status` ENUM('PENDING','CONFIRMED','PREPARING','READY','COMPLETED','CANCELLED','REJECTED') NULL COLLATE 'utf8mb4_unicode_ci'
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_peak_hour_analytics
DROP VIEW IF EXISTS `v_peak_hour_analytics`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_peak_hour_analytics` (
	`log_date` DATE NULL,
	`pickup_hour` INT NULL,
	`time_window` VARCHAR(1) NULL COLLATE 'utf8mb4_general_ci',
	`total_orders_processed` BIGINT NOT NULL,
	`avg_lead_time_minutes` DECIMAL(22,1) NULL,
	`avg_actual_kitchen_cooking_minutes` DECIMAL(22,1) NULL
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_popular_products
DROP VIEW IF EXISTS `v_popular_products`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_popular_products` (
	`product_name` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`quantity_sold` DECIMAL(32,0) NULL,
	`sales_amount` DECIMAL(34,2) NULL
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_recipe_management
DROP VIEW IF EXISTS `v_recipe_management`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_recipe_management` (
	`Recipe ID` INT NOT NULL,
	`Menu Product` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`Ingredient` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`Qty` DECIMAL(12,3) NOT NULL,
	`Unit` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`Allergens` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci'
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_subscription_credits
DROP VIEW IF EXISTS `v_subscription_credits`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_subscription_credits` (
	`sub_id` INT NOT NULL,
	`customer_id` INT NOT NULL,
	`full_name` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`plan_name` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`plan_type` ENUM('WEEKLY','MONTHLY','BUNDLE') NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`start_date` DATE NOT NULL,
	`end_date` DATE NOT NULL,
	`credits_total` INT NOT NULL,
	`credits_used` INT NOT NULL,
	`credits_remaining` BIGINT NOT NULL,
	`status` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`actual_status` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci'
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_wallet_utilization
DROP VIEW IF EXISTS `v_wallet_utilization`;
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_wallet_utilization` (
	`customer_id` INT NOT NULL,
	`full_name` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`email` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`current_balance` DECIMAL(10,2) NOT NULL,
	`subsidy_balance` DECIMAL(10,2) NOT NULL,
	`daily_limit` DECIMAL(10,2) NOT NULL,
	`parent_sponsor_name` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`parent_contact` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`total_loaded` DECIMAL(32,2) NOT NULL,
	`total_spent` DECIMAL(32,2) NOT NULL,
	`total_refunded` DECIMAL(32,2) NOT NULL
) ENGINE=MyISAM;

-- Dumping structure for table canteen_sales_inventory_system.wallets
DROP TABLE IF EXISTS `wallets`;
CREATE TABLE IF NOT EXISTS `wallets` (
  `wallet_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00',
  `subsidy_balance` decimal(10,2) NOT NULL DEFAULT '0.00',
  `daily_limit` decimal(10,2) NOT NULL DEFAULT '200.00',
  `parent_sponsor_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `parent_contact` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`wallet_id`),
  UNIQUE KEY `customer_id` (`customer_id`),
  CONSTRAINT `wallets_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer_profiles` (`customer_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.wallets: ~1 rows (approximately)
INSERT INTO `wallets` (`wallet_id`, `customer_id`, `balance`, `subsidy_balance`, `daily_limit`, `parent_sponsor_name`, `parent_contact`) VALUES
	(1, 1, 1375.00, 0.00, 1000.00, 'Engr. Roberto Reyes', '0917-123-4567'),
	(2, 2, 1835.00, 150.00, 99999.00, NULL, NULL),
	(3, 3, 500.00, 0.00, 200.00, NULL, NULL),
	(4, 5, 300.00, 135.00, 400.00, 'Engr. Test', '0912-123-4567');

-- Dumping structure for table canteen_sales_inventory_system.wallet_transactions
DROP TABLE IF EXISTS `wallet_transactions`;
CREATE TABLE IF NOT EXISTS `wallet_transactions` (
  `txn_id` int NOT NULL AUTO_INCREMENT,
  `wallet_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `txn_type` enum('LOAD','SUBSIDY_CREDIT','DEDUCT','REFUND') COLLATE utf8mb4_unicode_ci NOT NULL,
  `reference_order_id` int DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `txn_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`txn_id`),
  KEY `fk_wallet_txn_order` (`reference_order_id`),
  KEY `idx_wallet_sums` (`wallet_id`,`txn_type`,`amount`),
  KEY `idx_txn_date` (`txn_date`),
  CONSTRAINT `fk_wallet_txn_order` FOREIGN KEY (`reference_order_id`) REFERENCES `orders` (`order_id`) ON DELETE SET NULL,
  CONSTRAINT `wallet_transactions_ibfk_1` FOREIGN KEY (`wallet_id`) REFERENCES `wallets` (`wallet_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.wallet_transactions: ~18 rows (approximately)
INSERT INTO `wallet_transactions` (`txn_id`, `wallet_id`, `amount`, `txn_type`, `reference_order_id`, `description`, `txn_date`) VALUES
	(1, 1, 500.00, 'LOAD', NULL, 'Parent Over-the-counter deposit allowance load', '2026-06-08 02:52:19'),
	(2, 1, 150.00, 'SUBSIDY_CREDIT', NULL, 'School scholar daily free-meal subsidy assistance grant', '2026-06-08 02:52:19'),
	(3, 1, 75.00, 'DEDUCT', 101, 'Purchased Pork Sisig Rice Meal order', '2026-06-08 02:52:19'),
	(4, 1, 25.00, 'REFUND', 103, 'Automatic refund: Cancelled Egg Sandwich order', '2026-06-08 02:52:19'),
	(5, 1, 85.00, 'DEDUCT', 102, NULL, '2026-06-08 04:31:28'),
	(6, 1, 120.00, 'DEDUCT', NULL, NULL, '2026-06-11 00:25:45'),
	(7, 1, 120.00, 'DEDUCT', 501, NULL, '2026-06-11 00:26:48'),
	(8, 2, 95.00, 'DEDUCT', 502, NULL, '2026-06-11 00:26:48'),
	(9, 2, 165.00, 'DEDUCT', 503, NULL, '2026-06-11 00:26:48'),
	(10, 1, 45.00, 'DEDUCT', 504, NULL, '2026-06-11 00:26:48'),
	(11, 1, 140.00, 'DEDUCT', 905, NULL, '2026-06-14 10:41:58'),
	(12, 4, 65.00, 'DEDUCT', 926, NULL, '2026-06-20 06:57:45'),
	(13, 1, 500.00, 'LOAD', NULL, 'Self-Service E-Wallet Load (Ref: 09153442288)', '2026-06-23 22:23:29'),
	(14, 1, 100.00, 'LOAD', NULL, 'Self-Service E-Wallet Load (Ref: 09153442288)', '2026-06-23 22:24:01'),
	(15, 1, 65.00, 'DEDUCT', 927, NULL, '2026-06-23 22:57:59'),
	(16, 1, 65.00, 'DEDUCT', 933, NULL, '2026-06-24 06:17:38'),
	(17, 1, 65.00, 'DEDUCT', 934, NULL, '2026-06-24 06:18:06'),
	(18, 1, 65.00, 'REFUND', 934, 'Automated Refund for Cancelled Order #934', '2026-06-24 06:18:12'),
	(19, 1, 65.00, 'REFUND', 933, 'Automated Refund for Cancelled Order #933', '2026-06-24 06:31:29'),
	(20, 1, 65.00, 'REFUND', 927, 'Automated Refund for Cancelled Order #927', '2026-06-24 06:31:52'),
	(21, 1, 75.00, 'DEDUCT', 937, NULL, '2026-06-24 06:44:20'),
	(22, 1, 75.00, 'REFUND', 937, 'Automated Refund for Cancelled Order #937', '2026-06-24 06:44:30'),
	(23, 1, 190.00, 'DEDUCT', 938, NULL, '2026-06-24 10:32:57'),
	(24, 1, 190.00, 'REFUND', 938, 'Automated Refund for Cancelled Order #938', '2026-06-24 10:33:19'),
	(25, 1, 55.00, 'DEDUCT', 960, NULL, '2026-06-24 16:01:40'),
	(26, 1, 65.00, 'DEDUCT', 961, NULL, '2026-06-24 16:01:56'),
	(27, 1, 65.00, 'REFUND', 961, 'Automated Refund for Cancelled Order #961', '2026-06-24 16:02:01');

-- Dumping structure for trigger canteen_sales_inventory_system.prevent_negative_batch_stock
DROP TRIGGER IF EXISTS `prevent_negative_batch_stock`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `prevent_negative_batch_stock` BEFORE UPDATE ON `ingredient_batches` FOR EACH ROW BEGIN
    IF NEW.quantity_available < 0 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'CRITICAL ERROR: Adjustment would cause inventory to drop below zero. Transaction aborted.';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumping structure for trigger canteen_sales_inventory_system.trg_order_item_total
DROP TRIGGER IF EXISTS `trg_order_item_total`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `trg_order_item_total` BEFORE INSERT ON `order_items` FOR EACH ROW BEGIN SET NEW.line_total = NEW.quantity * NEW.unit_price; END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumping structure for trigger canteen_sales_inventory_system.trg_purchase_item_total
DROP TRIGGER IF EXISTS `trg_purchase_item_total`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `trg_purchase_item_total` BEFORE INSERT ON `purchase_items` FOR EACH ROW BEGIN SET NEW.line_total = NEW.quantity * NEW.unit_cost; END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumping structure for trigger canteen_sales_inventory_system.trg_verified_payment
DROP TRIGGER IF EXISTS `trg_verified_payment`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `trg_verified_payment` AFTER UPDATE ON `payments` FOR EACH ROW BEGIN
 IF NEW.status='VERIFIED' AND OLD.status <> 'VERIFIED' THEN
   UPDATE orders SET amount_paid = amount_paid + NEW.amount,
     balance = GREATEST(total_amount - amount_paid,0),
     payment_status = IF(amount_paid >= total_amount,'PAID','PENDING')
     WHERE order_id=NEW.order_id;
 END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_current_inventory`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_current_inventory` AS select `i`.`ingredient_id` AS `ingredient_id`,`i`.`ingredient_name` AS `ingredient_name`,`i`.`unit` AS `unit`,`i`.`quantity_on_hand` AS `quantity_on_hand`,`i`.`reorder_level` AS `reorder_level`,(case when (`i`.`quantity_on_hand` <= `i`.`reorder_level`) then 'LOW STOCK' else 'OK' end) AS `stock_status` from `ingredients` `i`;

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_daily_sales`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_daily_sales` AS select cast(`orders`.`ordered_at` as date) AS `sale_date`,count(0) AS `order_count`,sum(`orders`.`total_amount`) AS `gross_sales`,sum(`orders`.`amount_paid`) AS `collected` from `orders` where (`orders`.`order_status` in ('READY','COMPLETED')) group by cast(`orders`.`ordered_at` as date);

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_expiring_batches`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_expiring_batches` AS select `b`.`batch_id` AS `batch_id`,`i`.`ingredient_name` AS `ingredient_name`,`b`.`batch_number` AS `batch_number`,`b`.`expiry_date` AS `expiry_date`,`b`.`quantity_available` AS `quantity_available`,(to_days(`b`.`expiry_date`) - to_days(curdate())) AS `days_left` from (`ingredient_batches` `b` join `ingredients` `i` on((`i`.`ingredient_id` = `b`.`ingredient_id`))) where ((`b`.`quantity_available` > 0) and (`b`.`expiry_date` is not null) and (`b`.`expiry_date` <= (curdate() + interval 30 day)));

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_kitchen_display_queue`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_kitchen_display_queue` AS select `o`.`order_id` AS `order_id`,`o`.`order_no` AS `order_no`,`ps`.`slot_time` AS `scheduled_pickup`,`o`.`customer_type` AS `customer_type`,`o`.`queue_priority` AS `queue_priority`,`o`.`order_status` AS `order_status`,`o`.`prep_started_at` AS `prep_started_at`,(select coalesce(sum(`p`.`preparation_time_minutes`),5) from (`order_items` `oi` join `products` `p` on((`oi`.`product_id` = `p`.`product_id`))) where (`oi`.`order_id` = `o`.`order_id`)) AS `total_estimated_prep_minutes` from (`orders` `o` left join `pickup_slots` `ps` on((`o`.`slot_id` = `ps`.`slot_id`))) where ((`o`.`order_status` in ('PENDING','CONFIRMED','PREPARING')) and (`o`.`order_type` = 'PICKUP'));

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_low_stock`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_low_stock` AS select `v_current_inventory`.`ingredient_id` AS `ingredient_id`,`v_current_inventory`.`ingredient_name` AS `ingredient_name`,`v_current_inventory`.`unit` AS `unit`,`v_current_inventory`.`quantity_on_hand` AS `quantity_on_hand`,`v_current_inventory`.`reorder_level` AS `reorder_level`,`v_current_inventory`.`stock_status` AS `stock_status` from `v_current_inventory` where (`v_current_inventory`.`stock_status` = 'LOW STOCK');

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_nutrition_analytics`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_nutrition_analytics` AS select `p`.`product_id` AS `product_id`,`p`.`product_name` AS `product_name`,`p`.`calories` AS `calories`,`p`.`sugar` AS `sugar`,`p`.`sodium` AS `sodium`,(select coalesce(group_concat(distinct `i`.`allergen_tags` separator ','),'') from (`recipes` `r` join `ingredients` `i` on((`r`.`ingredient_id` = `i`.`ingredient_id`))) where ((`r`.`product_id` = `p`.`product_id`) and (`i`.`allergen_tags` <> ''))) AS `compiled_allergens`,coalesce(sum(`oi`.`quantity`),0) AS `total_orders` from (`products` `p` left join `order_items` `oi` on((`p`.`product_id` = `oi`.`product_id`))) group by `p`.`product_id`,`p`.`product_name`,`p`.`calories`,`p`.`sugar`,`p`.`sodium`;

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_order_summary`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_order_summary` AS select `o`.`order_id` AS `order_id`,`o`.`order_no` AS `order_no`,coalesce(`u`.`full_name`,'Walk-in') AS `customer`,`o`.`order_type` AS `order_type`,`o`.`ordered_at` AS `ordered_at`,`o`.`total_amount` AS `total_amount`,`o`.`amount_paid` AS `amount_paid`,`o`.`balance` AS `balance`,`o`.`payment_status` AS `payment_status`,`o`.`order_status` AS `order_status` from ((`orders` `o` left join `customer_profiles` `cp` on((`cp`.`customer_id` = `o`.`customer_id`))) left join `users` `u` on((`u`.`user_id` = `cp`.`user_id`)));

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_peak_hour_analytics`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_peak_hour_analytics` AS select cast(`o`.`ordered_at` as date) AS `log_date`,hour(`ps`.`slot_time`) AS `pickup_hour`,concat(hour(`ps`.`slot_time`),':00 - ',hour(`ps`.`slot_time`),':59') AS `time_window`,count(`o`.`order_id`) AS `total_orders_processed`,round(avg(timestampdiff(MINUTE,`o`.`ordered_at`,`o`.`prep_ready_at`)),1) AS `avg_lead_time_minutes`,round(avg(timestampdiff(MINUTE,`o`.`prep_started_at`,`o`.`prep_ready_at`)),1) AS `avg_actual_kitchen_cooking_minutes` from (`orders` `o` join `pickup_slots` `ps` on((`o`.`slot_id` = `ps`.`slot_id`))) where (`o`.`prep_ready_at` is not null) group by cast(`o`.`ordered_at` as date),hour(`ps`.`slot_time`),`ps`.`slot_time`;

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_popular_products`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_popular_products` AS select `p`.`product_name` AS `product_name`,sum(`oi`.`quantity`) AS `quantity_sold`,sum(`oi`.`line_total`) AS `sales_amount` from ((`order_items` `oi` join `products` `p` on((`p`.`product_id` = `oi`.`product_id`))) join `orders` `o` on((`o`.`order_id` = `oi`.`order_id`))) where (`o`.`order_status` not in ('CANCELLED','REJECTED')) group by `p`.`product_id`,`p`.`product_name`;

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_recipe_management`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_recipe_management` AS select `r`.`recipe_id` AS `Recipe ID`,`p`.`product_name` AS `Menu Product`,`i`.`ingredient_name` AS `Ingredient`,`r`.`quantity_required` AS `Qty`,`i`.`unit` AS `Unit`,`i`.`allergen_tags` AS `Allergens` from ((`recipes` `r` join `products` `p` on((`r`.`product_id` = `p`.`product_id`))) join `ingredients` `i` on((`r`.`ingredient_id` = `i`.`ingredient_id`))) order by `p`.`product_name`,`i`.`ingredient_name`;

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_subscription_credits`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_subscription_credits` AS select `cs`.`sub_id` AS `sub_id`,`cp`.`customer_id` AS `customer_id`,`u`.`full_name` AS `full_name`,`mp`.`plan_name` AS `plan_name`,`mp`.`plan_type` AS `plan_type`,`cs`.`start_date` AS `start_date`,`cs`.`end_date` AS `end_date`,`cs`.`credits_total` AS `credits_total`,`cs`.`credits_used` AS `credits_used`,(`cs`.`credits_total` - `cs`.`credits_used`) AS `credits_remaining`,`cs`.`status` AS `status`,if(((`cs`.`end_date` < curdate()) and (`cs`.`status` = 'ACTIVE')),'EXPIRED',`cs`.`status`) AS `actual_status` from (((`customer_subscriptions` `cs` join `customer_profiles` `cp` on((`cs`.`customer_id` = `cp`.`customer_id`))) join `users` `u` on((`cp`.`user_id` = `u`.`user_id`))) join `meal_plans` `mp` on((`cs`.`plan_id` = `mp`.`plan_id`)));

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_wallet_utilization`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_wallet_utilization` AS select `cp`.`customer_id` AS `customer_id`,`u`.`full_name` AS `full_name`,`u`.`email` AS `email`,`w`.`balance` AS `current_balance`,`w`.`subsidy_balance` AS `subsidy_balance`,`w`.`daily_limit` AS `daily_limit`,`w`.`parent_sponsor_name` AS `parent_sponsor_name`,`w`.`parent_contact` AS `parent_contact`,coalesce(sum((case when (`wt`.`txn_type` in ('LOAD','SUBSIDY_CREDIT')) then `wt`.`amount` else 0 end)),0) AS `total_loaded`,coalesce(sum((case when (`wt`.`txn_type` = 'DEDUCT') then `wt`.`amount` else 0 end)),0) AS `total_spent`,coalesce(sum((case when (`wt`.`txn_type` = 'REFUND') then `wt`.`amount` else 0 end)),0) AS `total_refunded` from (((`wallets` `w` join `customer_profiles` `cp` on((`w`.`customer_id` = `cp`.`customer_id`))) join `users` `u` on((`cp`.`user_id` = `u`.`user_id`))) left join `wallet_transactions` `wt` on((`w`.`wallet_id` = `wt`.`wallet_id`))) group by `cp`.`customer_id`,`u`.`full_name`,`u`.`email`,`w`.`balance`,`w`.`subsidy_balance`,`w`.`daily_limit`,`w`.`parent_sponsor_name`,`w`.`parent_contact`;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
