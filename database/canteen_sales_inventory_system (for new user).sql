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
CREATE DATABASE IF NOT EXISTS `canteen_sales_inventory_system` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `canteen_sales_inventory_system`;

-- Dumping structure for table canteen_sales_inventory_system.announcements
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.announcements: ~0 rows (approximately)
INSERT INTO `announcements` (`announcement_id`, `title`, `body`, `target_role`, `status`, `created_by`, `created_at`) VALUES
	(1, 'Welcome to GR 6 Canteen', 'Online pickup ordering is now available for registered customers.', 'ALL', 'ACTIVE', 1, '2026-05-25 15:11:24');

-- Dumping structure for table canteen_sales_inventory_system.audit_logs
CREATE TABLE IF NOT EXISTS `audit_logs` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `action` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `details` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `audit_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.audit_logs: ~6 rows (approximately)
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
	(95, 1, 'LOGOUT', 'System Manager logged out cleanly from administrative root terminal', '2026-06-08 04:44:41');

-- Dumping structure for table canteen_sales_inventory_system.categories
CREATE TABLE IF NOT EXISTS `categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `category_name` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.categories: ~4 rows (approximately)
INSERT INTO `categories` (`category_id`, `category_name`, `description`, `status`) VALUES
	(1, 'Rice Meals', 'Meals with rice', 'ACTIVE'),
	(2, 'Snacks', 'Quick snacks', 'ACTIVE'),
	(3, 'Beverages', 'Cold and hot beverages', 'ACTIVE'),
	(4, 'Desserts', 'Sweet products', 'ACTIVE');

-- Dumping structure for table canteen_sales_inventory_system.customer_profiles
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
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `customer_profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.customer_profiles: ~1 rows (approximately)
INSERT INTO `customer_profiles` (`customer_id`, `user_id`, `student_employee_no`, `course_department`, `dietary_notes`, `loyalty_points`, `created_at`, `dietary_profile`, `allergen_restrictions`) VALUES
	(1, 3, '2026-0001', 'Computer Engineering', 'No allergies', 20, '2026-05-25 15:11:24', 'NONE', ''),
	(2, 1, 'EMP-2026-001', 'Canteen Operations Root', NULL, 150, '2026-06-08 04:29:17', 'NONE', '');

-- Dumping structure for table canteen_sales_inventory_system.customer_subscriptions
CREATE TABLE IF NOT EXISTS `customer_subscriptions` (
  `sub_id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `plan_id` int NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `credits_total` int NOT NULL,
  `credits_used` int NOT NULL DEFAULT '0',
  `status` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`sub_id`),
  KEY `customer_id` (`customer_id`),
  KEY `plan_id` (`plan_id`),
  CONSTRAINT `customer_subscriptions_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer_profiles` (`customer_id`) ON DELETE CASCADE,
  CONSTRAINT `customer_subscriptions_ibfk_2` FOREIGN KEY (`plan_id`) REFERENCES `meal_plans` (`plan_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.customer_subscriptions: ~1 rows (approximately)
INSERT INTO `customer_subscriptions` (`sub_id`, `customer_id`, `plan_id`, `start_date`, `end_date`, `credits_total`, `credits_used`, `status`) VALUES
	(1, 1, 3, '2026-06-08', '2026-07-08', 10, 3, 'ACTIVE');

-- Dumping structure for table canteen_sales_inventory_system.employees
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.employees: ~0 rows (approximately)
INSERT INTO `employees` (`employee_id`, `user_id`, `position`, `shift_schedule`, `hired_date`, `status`) VALUES
	(1, 2, 'Cashier', '7:00 AM - 4:00 PM', '2026-01-05', 'ACTIVE');

-- Dumping structure for table canteen_sales_inventory_system.expenses
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.expenses: ~0 rows (approximately)
INSERT INTO `expenses` (`expense_id`, `expense_date`, `category`, `description`, `amount`, `recorded_by`) VALUES
	(1, '2026-05-20', 'Utilities', 'Cooking gas refill', 850.00, 1);

-- Dumping structure for table canteen_sales_inventory_system.feedback
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.feedback: ~0 rows (approximately)

-- Dumping structure for table canteen_sales_inventory_system.ingredients
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.ingredients: ~9 rows (approximately)
INSERT INTO `ingredients` (`ingredient_id`, `ingredient_name`, `unit`, `quantity_on_hand`, `reorder_level`, `status`, `allergen_tags`) VALUES
	(1, 'Rice', 'kg', 30.000, 10.000, 'ACTIVE', ''),
	(2, 'Chicken', 'kg', 15.000, 5.000, 'ACTIVE', ''),
	(3, 'Adobo Sauce', 'liter', 8.000, 2.000, 'ACTIVE', ''),
	(4, 'Siomai', 'pcs', 120.000, 40.000, 'ACTIVE', ''),
	(5, 'Bread Slice', 'pcs', 100.000, 30.000, 'ACTIVE', ''),
	(6, 'Cheese', 'slice', 80.000, 20.000, 'ACTIVE', ''),
	(7, 'Iced Tea Mix', 'gram', 1000.000, 300.000, 'ACTIVE', ''),
	(8, 'Cup 16oz', 'pcs', 100.000, 30.000, 'ACTIVE', ''),
	(9, 'Banana Turon', 'pcs', 70.000, 20.000, 'ACTIVE', '');

-- Dumping structure for table canteen_sales_inventory_system.ingredient_batches
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.ingredient_batches: ~3 rows (approximately)
INSERT INTO `ingredient_batches` (`batch_id`, `ingredient_id`, `supplier_id`, `batch_number`, `received_date`, `expiry_date`, `quantity_received`, `quantity_available`, `unit_cost`, `status`) VALUES
	(1, 1, 1, 'RICE-20260501', '2026-05-01', '2026-09-01', 30.000, 30.000, 48.00, 'AVAILABLE'),
	(2, 2, 1, 'CHK-20260520', '2026-05-20', '2026-05-27', 15.000, 15.000, 180.00, 'AVAILABLE'),
	(3, 4, 2, 'SIO-20260519', '2026-05-19', '2026-06-19', 120.000, 120.000, 5.00, 'AVAILABLE');

-- Dumping structure for table canteen_sales_inventory_system.inventory_adjustments
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.inventory_adjustments: ~0 rows (approximately)

-- Dumping structure for table canteen_sales_inventory_system.meal_plans
CREATE TABLE IF NOT EXISTS `meal_plans` (
  `plan_id` int NOT NULL AUTO_INCREMENT,
  `plan_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `plan_type` enum('WEEKLY','MONTHLY','BUNDLE') COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `total_credits` int NOT NULL,
  PRIMARY KEY (`plan_id`),
  UNIQUE KEY `plan_name` (`plan_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.meal_plans: ~3 rows (approximately)
INSERT INTO `meal_plans` (`plan_id`, `plan_name`, `plan_type`, `price`, `total_credits`) VALUES
	(1, 'Breakfast Saver Weekly', 'WEEKLY', 200.00, 5),
	(2, 'Unli-Lunch Monthly Pass', 'MONTHLY', 1200.00, 22),
	(3, 'Discounted 10-Meal Bundle', 'BUNDLE', 650.00, 10);

-- Dumping structure for table canteen_sales_inventory_system.messages
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.messages: ~0 rows (approximately)

-- Dumping structure for table canteen_sales_inventory_system.notifications
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.notifications: ~0 rows (approximately)

-- Dumping structure for table canteen_sales_inventory_system.orders
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
  `served_completed_at` timestamp NULL DEFAULT NULL,
  `unclaimed_disposition` enum('NONE','REFUNDED','DISPOSED_WASTAGE') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'NONE',
  PRIMARY KEY (`order_id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `customer_id` (`customer_id`),
  KEY `cashier_id` (`cashier_id`),
  KEY `fk_orders_slot` (`slot_id`),
  CONSTRAINT `fk_orders_slot` FOREIGN KEY (`slot_id`) REFERENCES `pickup_slots` (`slot_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer_profiles` (`customer_id`) ON DELETE SET NULL,
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`cashier_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.orders: ~0 rows (approximately)
INSERT INTO `orders` (`order_id`, `order_no`, `customer_id`, `slot_id`, `order_type`, `table_number`, `ordered_at`, `subtotal`, `discount_amount`, `total_amount`, `amount_paid`, `balance`, `payment_status`, `order_status`, `cashier_id`, `notes`, `inventory_deducted`, `customer_type`, `queue_priority`, `prep_started_at`, `prep_ready_at`, `served_completed_at`, `unclaimed_disposition`) VALUES
	(101, 'ORD-2026-0001', 1, NULL, 'PICKUP', NULL, '2026-06-08 04:31:28', 0.00, 0.00, 120.00, 0.00, 0.00, 'PAID', 'CONFIRMED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, 'NONE'),
	(102, 'ORD-2026-0002', 2, NULL, 'PICKUP', NULL, '2026-06-08 04:31:28', 0.00, 0.00, 115.00, 0.00, 0.00, 'PAID', 'CONFIRMED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, 'NONE'),
	(103, 'ORD-2026-0003', 1, NULL, 'PICKUP', NULL, '2026-06-08 04:31:28', 0.00, 0.00, 85.00, 0.00, 0.00, 'PAID', 'CANCELLED', NULL, NULL, 0, 'REGULAR', 3, NULL, NULL, NULL, 'NONE');

-- Dumping structure for table canteen_sales_inventory_system.order_items
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.order_items: ~0 rows (approximately)
INSERT INTO `order_items` (`order_item_id`, `order_id`, `product_id`, `quantity`, `unit_price`, `line_total`, `special_instruction`) VALUES
	(1, 101, 8, 1, 95.00, 95.00, 'Extra garlic fried rice'),
	(2, 101, 4, 1, 25.00, 25.00, 'Less ice'),
	(3, 102, 7, 1, 75.00, 75.00, 'Make it breast part'),
	(4, 102, 15, 1, 40.00, 40.00, 'No extra sugar'),
	(5, 103, 6, 1, 85.00, 85.00, 'Crispy look');

-- Dumping structure for table canteen_sales_inventory_system.payments
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.payments: ~0 rows (approximately)

-- Dumping structure for table canteen_sales_inventory_system.pickup_slots
CREATE TABLE IF NOT EXISTS `pickup_slots` (
  `slot_id` int NOT NULL AUTO_INCREMENT,
  `slot_time` time NOT NULL,
  `max_order_capacity` int NOT NULL DEFAULT '10',
  `status` enum('ACTIVE','INACTIVE') COLLATE utf8mb4_unicode_ci DEFAULT 'ACTIVE',
  PRIMARY KEY (`slot_id`),
  UNIQUE KEY `slot_time` (`slot_time`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.pickup_slots: ~12 rows (approximately)
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
	(12, '13:45:00', 8, 'ACTIVE');

-- Dumping structure for table canteen_sales_inventory_system.products
CREATE TABLE IF NOT EXISTS `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL,
  `product_name` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `barcode` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `preparation_time_minutes` int DEFAULT '5',
  `image_reference` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.products: ~5 rows (approximately)
INSERT INTO `products` (`product_id`, `category_id`, `product_name`, `description`, `barcode`, `unit_price`, `preparation_time_minutes`, `image_reference`, `status`, `created_at`, `calories`, `carbohydrates`, `protein`, `fat`, `sugar`, `sodium`) VALUES
	(1, 1, 'Chicken Adobo Meal', 'Rice with chicken adobo', 'CSIS-001', 75.00, 10, NULL, 'AVAILABLE', '2026-05-25 15:11:24', 520, 48.00, 32.00, 14.00, 2.00, 740.00),
	(2, 1, 'Pork Siomai Rice', 'Rice with steamed siomai', 'CSIS-002', 65.00, 8, NULL, 'AVAILABLE', '2026-05-25 15:11:24', 460, 52.00, 18.00, 16.00, 1.50, 590.00),
	(3, 2, 'Cheese Sandwich', 'Toasted sandwich', 'CSIS-003', 45.00, 5, NULL, 'AVAILABLE', '2026-05-25 15:11:24', 280, 26.00, 11.00, 12.00, 3.50, 430.00),
	(4, 3, 'Iced Tea', '16oz house iced tea', 'CSIS-004', 25.00, 2, NULL, 'AVAILABLE', '2026-05-25 15:11:24', 90, 22.00, 0.00, 0.00, 20.00, 10.00),
	(5, 4, 'Banana Turon', 'Sweet fried banana', 'CSIS-005', 20.00, 3, NULL, 'AVAILABLE', '2026-05-25 15:11:24', 160, 34.00, 1.50, 4.00, 18.00, 45.00),
	(6, 1, 'Crispy Pork Sisig Bowl', 'Traditional minced pork sizzled with white onions, chili, and egg over steamed rice.', 'CSIS-006', 85.00, 12, NULL, 'AVAILABLE', '2026-06-08 04:20:53', 580, 45.00, 22.00, 32.00, 2.50, 680.00),
	(7, 1, 'Chicken Adobo Rice Pack', 'Slow-cooked chicken thigh simmered in soy sauce, vinegar, garlic, and bay leaves.', 'CSIS-007', 75.00, 8, NULL, 'AVAILABLE', '2026-06-08 04:20:53', 490, 50.00, 28.00, 15.00, 3.00, 720.00),
	(8, 1, 'Beef Pares Combo', 'Braised beef brisket in sweet anise-infused gravy served with garlic fried rice and clear broth.', 'CSIS-008', 95.00, 10, NULL, 'AVAILABLE', '2026-06-08 04:20:53', 620, 55.00, 30.00, 18.00, 6.00, 810.00),
	(9, 1, 'Ginataang Sitaw at Kalabasa', 'Healthy string beans and squash simmered in rich coconut cream, served over brown rice.', 'CSIS-009', 65.00, 15, NULL, 'AVAILABLE', '2026-06-08 04:20:53', 340, 42.00, 8.00, 12.00, 4.50, 390.00),
	(10, 2, 'Pancit Guisado Plate', 'Stir-fried bihon and canton noodles packed with shredded vegetables and chicken strips.', 'CSIS-0010', 45.00, 7, NULL, 'AVAILABLE', '2026-06-08 04:20:53', 290, 38.00, 10.00, 6.00, 1.50, 450.00),
	(11, 2, 'Cheesy Baked Macaroni', 'Elbow macaroni tossed in meat sauce, topped with a thick, creamy melted cheese layer.', 'CSIS-011', 55.00, 5, NULL, 'AVAILABLE', '2026-06-08 04:20:53', 410, 48.00, 14.00, 16.00, 5.00, 540.00),
	(12, 2, 'Toasted Ham & Cheese Sandwich', 'Double-decker white bread with sliced sweet ham, cheddar cheese, and light mayo spread.', 'CSIS-012', 35.00, 4, NULL, 'AVAILABLE', '2026-06-08 04:20:53', 260, 28.00, 12.00, 9.00, 2.00, 410.00),
	(13, 2, 'Crispy Vegetable Lumpia (2pcs)', 'Deep-fried spring rolls stuffed with bean sprouts, carrots, and sweet potato, paired with vinegar.', 'CSIS-013', 30.00, 6, NULL, 'AVAILABLE', '2026-06-08 04:20:53', 180, 22.00, 4.00, 8.00, 2.50, 280.00),
	(14, 3, 'House Blend Iced Tea (L)', 'Freshly brewed black tea infused with sweet calamansi extract, served cold over ice.', 'CSIS-014', 25.00, 2, NULL, 'AVAILABLE', '2026-06-08 04:20:53', 120, 30.00, 0.00, 0.00, 28.00, 15.00),
	(15, 3, 'Creamy Buko Pandan Shake', 'Blended beverage made with real coconut strands, green gelatin cubes, and sweet condensed milk.', 'CSIS-015', 40.00, 3, NULL, 'AVAILABLE', '2026-06-08 04:20:53', 240, 35.00, 3.00, 7.00, 31.00, 45.00);

-- Dumping structure for table canteen_sales_inventory_system.product_promotions
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.promotions: ~0 rows (approximately)
INSERT INTO `promotions` (`promotion_id`, `promo_name`, `promo_code`, `discount_type`, `discount_value`, `start_date`, `end_date`, `status`) VALUES
	(1, 'Student Meal Discount', 'STUDENT10', 'PERCENT', 10.00, '2026-05-01', '2026-12-31', 'ACTIVE');

-- Dumping structure for table canteen_sales_inventory_system.purchase_items
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.purchase_items: ~0 rows (approximately)

-- Dumping structure for table canteen_sales_inventory_system.purchase_orders
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.purchase_orders: ~0 rows (approximately)

-- Dumping structure for table canteen_sales_inventory_system.recipes
CREATE TABLE IF NOT EXISTS `recipes` (
  `recipe_id` int NOT NULL AUTO_INCREMENT,
  `product_id` int NOT NULL,
  `ingredient_id` int NOT NULL,
  `quantity_required` decimal(12,3) NOT NULL,
  PRIMARY KEY (`recipe_id`),
  UNIQUE KEY `product_id` (`product_id`,`ingredient_id`),
  KEY `ingredient_id` (`ingredient_id`),
  CONSTRAINT `recipes_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`) ON DELETE CASCADE,
  CONSTRAINT `recipes_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredients` (`ingredient_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.recipes: ~10 rows (approximately)
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
	(10, 5, 9, 1.000);

-- Dumping structure for table canteen_sales_inventory_system.stock_movements
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.stock_movements: ~0 rows (approximately)

-- Dumping structure for table canteen_sales_inventory_system.suppliers
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.suppliers: ~2 rows (approximately)
INSERT INTO `suppliers` (`supplier_id`, `supplier_name`, `contact_person`, `phone`, `email`, `address`, `status`, `created_at`) VALUES
	(1, 'Fresh Farm Supplies', 'Ana Cruz', '09181110000', 'freshfarm@example.com', NULL, 'ACTIVE', '2026-05-25 15:11:24'),
	(2, 'Campus Wholesale Depot', 'Ben Lim', '09182220000', 'depot@example.com', NULL, 'ACTIVE', '2026-05-25 15:11:24');

-- Dumping structure for table canteen_sales_inventory_system.users
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.users: ~3 rows (approximately)
INSERT INTO `users` (`user_id`, `username`, `password_hash`, `full_name`, `email`, `phone`, `address`, `role`, `status`, `created_at`, `updated_at`) VALUES
	(1, 'admin', 'e86f78a8a3caf0b60d8e74e5942aa6d86dc150cd3c03338aef25b7d2d7e3acc7', 'System Administrator', 'admin@canteen.local', '09170000001', 'School Campus', 'ADMIN', 'ACTIVE', '2026-05-25 15:11:24', '2026-05-25 15:11:24'),
	(2, 'staff1', 'e86f78a8a3caf0b60d8e74e5942aa6d86dc150cd3c03338aef25b7d2d7e3acc7', 'Canteen Cashier', 'staff@canteen.local', '09170000002', 'School Campus', 'STAFF', 'ACTIVE', '2026-05-25 15:11:24', '2026-05-25 15:11:24'),
	(3, 'customer1', '3e7c19576488862816f13b512cacf3e4ba97dd97243ea0bd6a2ad1642d86ba72', 'Juan Student', 'customer@canteen.local', '09170000003', 'Manila', 'CUSTOMER', 'ACTIVE', '2026-05-25 15:11:24', '2026-05-25 15:11:24');

-- Dumping structure for view canteen_sales_inventory_system.v_current_inventory
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
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_daily_sales` (
	`sale_date` DATE NULL,
	`order_count` BIGINT NOT NULL,
	`gross_sales` DECIMAL(34,2) NULL,
	`collected` DECIMAL(34,2) NULL
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_expiring_batches
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
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_popular_products` (
	`product_name` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`quantity_sold` DECIMAL(32,0) NULL,
	`sales_amount` DECIMAL(34,2) NULL
) ENGINE=MyISAM;

-- Dumping structure for view canteen_sales_inventory_system.v_subscription_credits
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
-- Creating temporary table to overcome VIEW dependency errors
CREATE TABLE `v_wallet_utilization` (
	`customer_id` INT NOT NULL,
	`full_name` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`email` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`current_balance` DECIMAL(10,2) NULL,
	`subsidy_balance` DECIMAL(10,2) NULL,
	`daily_limit` DECIMAL(10,2) NULL,
	`parent_sponsor_name` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`parent_contact` VARCHAR(1) NULL COLLATE 'utf8mb4_unicode_ci',
	`total_loaded` DECIMAL(32,2) NOT NULL,
	`total_spent` DECIMAL(32,2) NOT NULL,
	`total_refunded` DECIMAL(32,2) NOT NULL
) ENGINE=MyISAM;

-- Dumping structure for table canteen_sales_inventory_system.wallets
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.wallets: ~1 rows (approximately)
INSERT INTO `wallets` (`wallet_id`, `customer_id`, `balance`, `subsidy_balance`, `daily_limit`, `parent_sponsor_name`, `parent_contact`) VALUES
	(1, 1, 465.00, 150.00, 150.00, 'Mr. Spenser Carl', '0917-123-4567'),
	(2, 2, 1200.00, 0.00, 0.00, NULL, NULL);

-- Dumping structure for table canteen_sales_inventory_system.wallet_transactions
CREATE TABLE IF NOT EXISTS `wallet_transactions` (
  `txn_id` int NOT NULL AUTO_INCREMENT,
  `wallet_id` int NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `txn_type` enum('LOAD','SUBSIDY_CREDIT','DEDUCT','REFUND') COLLATE utf8mb4_unicode_ci NOT NULL,
  `reference_order_id` int DEFAULT NULL,
  `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `txn_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`txn_id`),
  KEY `wallet_id` (`wallet_id`),
  KEY `fk_wallet_txn_order` (`reference_order_id`),
  CONSTRAINT `fk_wallet_txn_order` FOREIGN KEY (`reference_order_id`) REFERENCES `orders` (`order_id`) ON DELETE SET NULL,
  CONSTRAINT `wallet_transactions_ibfk_1` FOREIGN KEY (`wallet_id`) REFERENCES `wallets` (`wallet_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table canteen_sales_inventory_system.wallet_transactions: ~4 rows (approximately)
INSERT INTO `wallet_transactions` (`txn_id`, `wallet_id`, `amount`, `txn_type`, `reference_order_id`, `description`, `txn_date`) VALUES
	(1, 1, 500.00, 'LOAD', NULL, 'Parent Over-the-counter deposit allowance load', '2026-06-08 02:52:19'),
	(2, 1, 150.00, 'SUBSIDY_CREDIT', NULL, 'School scholar daily free-meal subsidy assistance grant', '2026-06-08 02:52:19'),
	(3, 1, 75.00, 'DEDUCT', 101, 'Purchased Pork Sisig Rice Meal order', '2026-06-08 02:52:19'),
	(4, 1, 25.00, 'REFUND', 103, 'Automatic refund: Cancelled Egg Sandwich order', '2026-06-08 02:52:19'),
	(5, 1, 85.00, 'DEDUCT', 102, NULL, '2026-06-08 04:31:28');

-- Dumping structure for trigger canteen_sales_inventory_system.trg_order_item_total
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `trg_order_item_total` BEFORE INSERT ON `order_items` FOR EACH ROW BEGIN SET NEW.line_total = NEW.quantity * NEW.unit_price; END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumping structure for trigger canteen_sales_inventory_system.trg_purchase_item_total
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `trg_purchase_item_total` BEFORE INSERT ON `purchase_items` FOR EACH ROW BEGIN SET NEW.line_total = NEW.quantity * NEW.unit_cost; END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Dumping structure for trigger canteen_sales_inventory_system.trg_verified_payment
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
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_popular_products` AS select `p`.`product_name` AS `product_name`,sum(`oi`.`quantity`) AS `quantity_sold`,sum(`oi`.`line_total`) AS `sales_amount` from ((`order_items` `oi` join `products` `p` on((`p`.`product_id` = `oi`.`product_id`))) join `orders` `o` on((`o`.`order_id` = `oi`.`order_id`))) where (`o`.`order_status` <> 'CANCELLED') group by `p`.`product_id`,`p`.`product_name`;

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_subscription_credits`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_subscription_credits` AS select `cs`.`sub_id` AS `sub_id`,`cp`.`customer_id` AS `customer_id`,`u`.`full_name` AS `full_name`,`mp`.`plan_name` AS `plan_name`,`mp`.`plan_type` AS `plan_type`,`cs`.`start_date` AS `start_date`,`cs`.`end_date` AS `end_date`,`cs`.`credits_total` AS `credits_total`,`cs`.`credits_used` AS `credits_used`,(`cs`.`credits_total` - `cs`.`credits_used`) AS `credits_remaining`,`cs`.`status` AS `status`,if(((`cs`.`end_date` < curdate()) and (`cs`.`status` = 'ACTIVE')),'EXPIRED',`cs`.`status`) AS `actual_status` from (((`customer_subscriptions` `cs` join `customer_profiles` `cp` on((`cs`.`customer_id` = `cp`.`customer_id`))) join `users` `u` on((`cp`.`user_id` = `u`.`user_id`))) join `meal_plans` `mp` on((`cs`.`plan_id` = `mp`.`plan_id`)));

-- Removing temporary table and create final VIEW structure
DROP TABLE IF EXISTS `v_wallet_utilization`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `v_wallet_utilization` AS select `cp`.`customer_id` AS `customer_id`,`u`.`full_name` AS `full_name`,`u`.`email` AS `email`,`w`.`balance` AS `current_balance`,`w`.`subsidy_balance` AS `subsidy_balance`,`w`.`daily_limit` AS `daily_limit`,`w`.`parent_sponsor_name` AS `parent_sponsor_name`,`w`.`parent_contact` AS `parent_contact`,coalesce((select sum(`wallet_transactions`.`amount`) from `wallet_transactions` where ((`wallet_transactions`.`wallet_id` = `w`.`wallet_id`) and (`wallet_transactions`.`txn_type` in ('LOAD','SUBSIDY_CREDIT')))),0) AS `total_loaded`,coalesce((select sum(`wallet_transactions`.`amount`) from `wallet_transactions` where ((`wallet_transactions`.`wallet_id` = `w`.`wallet_id`) and (`wallet_transactions`.`txn_type` = 'DEDUCT'))),0) AS `total_spent`,coalesce((select sum(`wallet_transactions`.`amount`) from `wallet_transactions` where ((`wallet_transactions`.`wallet_id` = `w`.`wallet_id`) and (`wallet_transactions`.`txn_type` = 'REFUND'))),0) AS `total_refunded` from ((`wallets` `w` join `customer_profiles` `cp` on((`w`.`customer_id` = `cp`.`customer_id`))) join `users` `u` on((`cp`.`user_id` = `u`.`user_id`)));

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
