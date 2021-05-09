-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 27, 2021 at 12:51 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `revenue_monitoring`
--
CREATE DATABASE IF NOT EXISTS `revenue_monitoring` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `revenue_monitoring`;

-- --------------------------------------------------------

--
-- Table structure for table `center_items`
--
-- Creation: Apr 22, 2021 at 05:14 PM
-- Last update: Apr 25, 2021 at 12:04 PM
--

DROP TABLE IF EXISTS `center_items`;
CREATE TABLE IF NOT EXISTS `center_items` (
  `assign_center` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `assign_item` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `assign_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`assign_code`),
  KEY `assign_center` (`assign_center`),
  KEY `assign_item` (`assign_item`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `center_items`
--

INSERT INTO `center_items` (`assign_center`, `assign_item`, `assign_code`) VALUES
('K0301', '1423001', '02577f52242021224'),
('K0201', '1423001', '9960e712542021444'),
('K0206', '1423001', 'c7890112542021124'),
('K0101', '1423001', 'ff1e09522420212126');

-- --------------------------------------------------------

--
-- Table structure for table `cheque_details`
--
-- Creation: Apr 22, 2021 at 05:14 PM
--

DROP TABLE IF EXISTS `cheque_details`;
CREATE TABLE IF NOT EXISTS `cheque_details` (
  `cheque_ID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `payment_ID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cheque_date` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cheque_number` int(11) NOT NULL,
  `bank` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `amount` float NOT NULL,
  PRIMARY KEY (`cheque_ID`),
  KEY `payment_ID` (`payment_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `collection_payment_entries`
--
-- Creation: Apr 22, 2021 at 08:56 PM
--

DROP TABLE IF EXISTS `collection_payment_entries`;
CREATE TABLE IF NOT EXISTS `collection_payment_entries` (
  `pay_ID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `pay_revCenter` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `GCR` int(20) NOT NULL,
  `Amount` float NOT NULL,
  `Date` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Month` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Year` int(11) NOT NULL,
  `payment_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`pay_ID`),
  KEY `collection_payment_entries_ibfk_1` (`pay_revCenter`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `commission_details`
--
-- Creation: Apr 22, 2021 at 08:56 PM
--

DROP TABLE IF EXISTS `commission_details`;
CREATE TABLE IF NOT EXISTS `commission_details` (
  `commission_center` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `commission_ID` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `commission_amount` float NOT NULL,
  `commission_date` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `commission_week` int(11) NOT NULL,
  `commission_month` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `commission_quarter` int(11) NOT NULL,
  `commission_year` int(11) NOT NULL,
  PRIMARY KEY (`commission_ID`),
  KEY `commission_details_ibfk_1` (`commission_center`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `commission_details`
--

INSERT INTO `commission_details` (`commission_center`, `commission_ID`, `commission_amount`, `commission_date`, `commission_week`, `commission_month`, `commission_quarter`, `commission_year`) VALUES
('K0301', 'ea7242c352222438302', 5465, '09-04-2021', 2, 'April', 2, 2021);

-- --------------------------------------------------------

--
-- Table structure for table `daily_entries`
--
-- Creation: Apr 22, 2021 at 08:55 PM
-- Last update: Apr 25, 2021 at 03:08 PM
--

DROP TABLE IF EXISTS `daily_entries`;
CREATE TABLE IF NOT EXISTS `daily_entries` (
  `daily_revCenter` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `revenueItem` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `revenueAmount` float NOT NULL,
  `revenueDate` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `revenueWeek` int(10) NOT NULL,
  `revenueMonth` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `revenueQuarter` int(15) NOT NULL,
  `revenueYear` int(10) NOT NULL,
  `entries_ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`entries_ID`),
  KEY `daily_entries_ibfk_1` (`daily_revCenter`),
  KEY `daily_entries_ibfk_2` (`revenueItem`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `daily_entries`
--

INSERT INTO `daily_entries` (`daily_revCenter`, `revenueItem`, `revenueAmount`, `revenueDate`, `revenueWeek`, `revenueMonth`, `revenueQuarter`, `revenueYear`, `entries_ID`) VALUES
('K0101', '1423001', 5421, '22-04-2021', 4, 'April', 2, 2021, 31),
('K0206', '1423001', 23000, '15-04-2021', 3, 'April', 2, 2021, 37),
('K0101', '1423001', 57557, '20-04-2021', 4, 'April', 2, 2021, 38),
('K0101', '1423001', 4254, '21-04-2021', 4, 'April', 2, 2021, 39),
('K0101', '1423001', 4547, '25-04-2021', 5, 'April', 2, 2021, 40),
('K0301', '1423001', 56429, '20-04-2021', 4, 'April', 2, 2021, 41);

-- --------------------------------------------------------

--
-- Table structure for table `revenue_centers`
--
-- Creation: Apr 22, 2021 at 05:14 PM
-- Last update: Apr 25, 2021 at 12:03 PM
--

DROP TABLE IF EXISTS `revenue_centers`;
CREATE TABLE IF NOT EXISTS `revenue_centers` (
  `CenterID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `revenue_category` varchar(70) COLLATE utf8mb4_unicode_ci NOT NULL,
  `revenue_center` varchar(70) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`CenterID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `revenue_centers`
--

INSERT INTO `revenue_centers` (`CenterID`, `revenue_category`, `revenue_center`) VALUES
('K0101', 'KMA MAIN', 'ASAFO MARKET'),
('K0201', 'PROPERTY RATE SECTION', 'PROPERTY RATE BANTAMA'),
('K0206', 'PROPERTY RATE SECTION', 'PROPERTY RATE KMA'),
('K0301', 'SUB-METROS', 'BANTAMA SUB-METRO');

-- --------------------------------------------------------

--
-- Table structure for table `revenue_items`
--
-- Creation: Apr 22, 2021 at 05:14 PM
--

DROP TABLE IF EXISTS `revenue_items`;
CREATE TABLE IF NOT EXISTS `revenue_items` (
  `revenue_item_ID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `revenue_item` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `item_category` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`revenue_item_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `revenue_items`
--

INSERT INTO `revenue_items` (`revenue_item_ID`, `revenue_item`, `item_category`) VALUES
('1423001', 'Market Tolls', 'Fees');

-- --------------------------------------------------------

--
-- Table structure for table `target_entries`
--
-- Creation: Apr 22, 2021 at 05:14 PM
--

DROP TABLE IF EXISTS `target_entries`;
CREATE TABLE IF NOT EXISTS `target_entries` (
  `target_revCenter` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Amount` float NOT NULL,
  `Year` int(11) NOT NULL,
  `target_ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`target_ID`),
  KEY `target_revCenter` (`target_revCenter`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `value_books_details`
--
-- Creation: Apr 22, 2021 at 05:14 PM
--

DROP TABLE IF EXISTS `value_books_details`;
CREATE TABLE IF NOT EXISTS `value_books_details` (
  `value_book_ID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value_books` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`value_book_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `value_books_details`
--

INSERT INTO `value_books_details` (`value_book_ID`, `value_books`, `price`) VALUES
('VB01', 'GCR', 0),
('VB02', 'Car Park Ticket (1cedi)', 1),
('VB03', 'Car Park Ticket (2cedis)', 2),
('VB04', 'Market Toll (1cedi)', 1);

-- --------------------------------------------------------

--
-- Table structure for table `value_books_stock_record`
--
-- Creation: Apr 22, 2021 at 05:14 PM
--

DROP TABLE IF EXISTS `value_books_stock_record`;
CREATE TABLE IF NOT EXISTS `value_books_stock_record` (
  `value_stock_revCenter` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `year` int(11) NOT NULL,
  `month` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `week` int(11) NOT NULL,
  `date` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value_book` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_serial` int(11) NOT NULL,
  `last_serial` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `amount` float NOT NULL,
  `purchase_amount` float NOT NULL,
  `remarks` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value_record_ID` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`value_record_ID`),
  KEY `value_book` (`value_book`),
  KEY `value_stock_revCenter` (`value_stock_revCenter`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `center_items`
--
ALTER TABLE `center_items`
  ADD CONSTRAINT `center_items_ibfk_1` FOREIGN KEY (`assign_center`) REFERENCES `revenue_centers` (`CenterID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `center_items_ibfk_2` FOREIGN KEY (`assign_item`) REFERENCES `revenue_items` (`revenue_item_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `cheque_details`
--
ALTER TABLE `cheque_details`
  ADD CONSTRAINT `cheque_details_ibfk_1` FOREIGN KEY (`payment_ID`) REFERENCES `collection_payment_entries` (`pay_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `collection_payment_entries`
--
ALTER TABLE `collection_payment_entries`
  ADD CONSTRAINT `collection_payment_entries_ibfk_1` FOREIGN KEY (`pay_revCenter`) REFERENCES `revenue_centers` (`CenterID`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `commission_details`
--
ALTER TABLE `commission_details`
  ADD CONSTRAINT `commission_details_ibfk_1` FOREIGN KEY (`commission_center`) REFERENCES `revenue_centers` (`CenterID`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `daily_entries`
--
ALTER TABLE `daily_entries`
  ADD CONSTRAINT `daily_entries_ibfk_1` FOREIGN KEY (`daily_revCenter`) REFERENCES `revenue_centers` (`CenterID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `daily_entries_ibfk_2` FOREIGN KEY (`revenueItem`) REFERENCES `revenue_items` (`revenue_item_ID`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `target_entries`
--
ALTER TABLE `target_entries`
  ADD CONSTRAINT `target_entries_ibfk_1` FOREIGN KEY (`target_revCenter`) REFERENCES `revenue_centers` (`CenterID`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `value_books_stock_record`
--
ALTER TABLE `value_books_stock_record`
  ADD CONSTRAINT `value_books_stock_record_ibfk_1` FOREIGN KEY (`value_book`) REFERENCES `value_books_details` (`value_book_ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `value_books_stock_record_ibfk_2` FOREIGN KEY (`value_stock_revCenter`) REFERENCES `revenue_centers` (`CenterID`) ON DELETE NO ACTION ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
