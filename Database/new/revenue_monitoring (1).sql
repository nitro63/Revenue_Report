-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 07, 2021 at 07:12 PM
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
CREATE DATABASE IF NOT EXISTS `revenue_monitoring` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `revenue_monitoring`;

-- --------------------------------------------------------

--
-- Table structure for table `cheque_details`
--

DROP TABLE IF EXISTS `cheque_details`;
CREATE TABLE IF NOT EXISTS `cheque_details` (
  `cheque_ID` varchar(50) NOT NULL,
  `payment_ID` varchar(50) NOT NULL,
  `cheque_date` varchar(20) NOT NULL,
  `cheque_number` int(11) NOT NULL,
  `bank` varchar(80) NOT NULL,
  `amount` float NOT NULL,
  PRIMARY KEY (`cheque_ID`),
  KEY `payment_ID` (`payment_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `collection_payment_entries`
--

DROP TABLE IF EXISTS `collection_payment_entries`;
CREATE TABLE IF NOT EXISTS `collection_payment_entries` (
  `pay_ID` varchar(50) NOT NULL,
  `pay_revCenter` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `GCR` int(20) NOT NULL,
  `Amount` float NOT NULL,
  `Date` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `Month` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `Year` int(11) NOT NULL,
  `payment_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`pay_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `collection_payment_entries`
--

INSERT INTO `collection_payment_entries` (`pay_ID`, `pay_revCenter`, `GCR`, `Amount`, `Date`, `Month`, `Year`, `payment_type`) VALUES
('006ed2d6474202116217968', 'DAY & NIGHT', 5456878, 7845, '21-04-2021', 'March', 2021, 'Cheque'),
('085b736a4742021154324848', 'DAY & NIGHT', 5452456, 7895, '14-04-2021', 'March', 2021, 'Cheque'),
('136270464742021163827736', 'ASAFO MARKET', 7845648, 7845, '14-04-2021', 'March', 2021, 'Cheque'),
('210c25344742021135258850', 'ASAFO MARKET', 5445645, 7874, '22-04-2021', 'March', 2021, 'Cash'),
('23c0b8a94742021162543172', 'NOISE CONTROL', 5464654, 4524, '21-04-2021', 'March', 2021, 'Cheque'),
('247aabfe474202116309900', 'ASAFO MARKET', 7845565, 5445, '21-04-2021', 'March', 2021, 'Cheque'),
('3e0029284742021133245719', 'ASAFO MARKET', 2454468, 4542, '08-04-2021', 'February', 2021, 'Cash'),
('3eae4db24742021155230379', 'ASAFO MARKET', 2441578, 5455, '14-04-2021', 'January', 2021, 'Cheque'),
('4777b688474202116228597', 'NOISE CONTROL', 7545678, 5541, '14-04-2021', 'March', 2021, 'Cheque'),
('5d56e148474202115475617', 'KOBBY JEY', 6541578, 57466, '21-04-2021', 'March', 2021, 'Cheque'),
('a306c4404742021145442424', 'BANTAMA', 700101, 200, '07-04-2021', 'January', 2021, 'Cheque'),
('a8cf43bc474202113324675', 'ASAFO MARKET', 4541554, 5455, '23-04-2021', 'March', 2021, 'Cheque'),
('b2b4d92d474202115558632', 'ASAFO MARKET', 3521657, 5454, '13-04-2021', 'March', 2021, 'Cheque'),
('b763ac3c474202116438866', 'ASAFO MARKET', 5555555, 7897, '14-04-2021', 'March', 2021, 'Cheque'),
('d4930d544742021135259194', 'ASAFO MARKET', 7487457, 7858, '08-04-2021', 'March', 2021, 'Cheque'),
('f832538e4742021162351154', 'DAY & NIGHT', 6545455, 4155, '15-04-2021', 'March', 2021, 'Cheque'),
('ffcc7ad6474202116563301', 'BANTAMA', 1007061, 4120, '07-04-2021', 'March', 2021, 'Cheque');

-- --------------------------------------------------------

--
-- Table structure for table `daily_entries`
--

DROP TABLE IF EXISTS `daily_entries`;
CREATE TABLE IF NOT EXISTS `daily_entries` (
  `daily_revCenter` varchar(80) NOT NULL,
  `Code` varchar(20) NOT NULL,
  `revenueItem` varchar(80) NOT NULL,
  `revenueAmount` float NOT NULL,
  `commissionAmount` float DEFAULT NULL,
  `revenueDate` varchar(20) NOT NULL,
  `revenueWeek` int(10) NOT NULL,
  `revenueMonth` varchar(20) NOT NULL,
  `revenueQuarter` int(15) NOT NULL,
  `revenueYear` int(10) NOT NULL,
  `entries_ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`entries_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `daily_entries`
--

INSERT INTO `daily_entries` (`daily_revCenter`, `Code`, `revenueItem`, `revenueAmount`, `commissionAmount`, `revenueDate`, `revenueWeek`, `revenueMonth`, `revenueQuarter`, `revenueYear`, `entries_ID`) VALUES
('BANTAMA', '1423011', 'Reg. of Marriage/Divorce', 4234, 4212, '22-04-2021', 4, 'April', 2, 2021, 12),
('ASAFO MARKET', '1423001', 'Market Tolls', 7854, 0, '13-04-2021', 3, 'April', 2, 2021, 13),
('ASAFO MARKET', '13425', 'Burial Permits and Cemetry', 654, 0, '13-04-2021', 3, 'April', 2, 2021, 14),
('BANTAMA', '1423001', 'Market Tolls', 7854.56, 0, '07-04-2021', 2, 'April', 2, 2021, 15);

-- --------------------------------------------------------

--
-- Table structure for table `revenue_centers`
--

DROP TABLE IF EXISTS `revenue_centers`;
CREATE TABLE IF NOT EXISTS `revenue_centers` (
  `CenterID` varchar(50) NOT NULL,
  `revenue_category` varchar(70) NOT NULL,
  `revenue_center` varchar(70) NOT NULL,
  PRIMARY KEY (`CenterID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `revenue_centers`
--

INSERT INTO `revenue_centers` (`CenterID`, `revenue_category`, `revenue_center`) VALUES
('1', 'KMA MAIN', 'ASAFO MARKET'),
('10', 'KMA MAIN', 'NOISE CONTROL'),
('11', 'KMA MAIN', 'OTHER REVENUE'),
('12', 'KMA MAIN', 'PREMPEH ASSEMBLY HALL'),
('13', 'KMA MAIN', 'RACE COURSE'),
('14', 'KMA MAIN', 'SOKOBAN'),
('15', 'KMA MAIN', 'WASTE MANAGEMENT DEPARTMENT'),
('16', 'KMA MAIN', 'WORKS DEPARTMENT'),
('17', 'OUTSOURCED', 'DAY & NIGHT'),
('18', 'OUTSOURCED', 'KOBBY JEY'),
('19', 'OUTSOURCED', 'ACHEAMFOUR'),
('2', 'KMA MAIN', 'TOWING YARD'),
('20', 'OUTSOURCED', 'AMANSIA GHANA'),
('21', 'OUTSOURCED', 'DE GEONS'),
('22', 'OUTSOURCED', 'FIDELITY'),
('23', 'OUTSOURCED', 'GOLD PRINT'),
('24', 'OUTSOURCED', 'GOLD STREET'),
('25', 'OUTSOURCED', 'MY COMPANY 365'),
('26', 'OUTSOURCED', 'NANA AFIA SERWA KOBI MARKET'),
('27', 'OUTSOURCED', 'SKYMOUNT'),
('28', 'SUB-METROS', 'BANTAMA'),
('29', 'SUB-METROS', 'MANHYIA NORTH'),
('3', 'KMA MAIN', 'METRO GAURD'),
('30', 'SUB-METROS', 'MANHYIA SOUTH'),
('31', 'SUB-METROS', 'NHYIAESO'),
('32', 'SUB-METROS', 'SUBIN'),
('4', 'KMA MAIN', 'ENVIRONMENTAL HEALTH'),
('5', 'KMA MAIN', 'ESTATE'),
('6', 'KMA MAIN', 'LICENCE'),
('7', 'KMA MAIN', 'PROPERTY RATE KMA'),
('8', 'KMA MAIN', 'PROPERTY RATE SUB-METROS'),
('9', 'KMA MAIN', 'MARRIAGE UNIT');

-- --------------------------------------------------------

--
-- Table structure for table `revenue_items`
--

DROP TABLE IF EXISTS `revenue_items`;
CREATE TABLE IF NOT EXISTS `revenue_items` (
  `revenue_item_ID` varchar(50) NOT NULL,
  `revenue_item` varchar(80) NOT NULL,
  PRIMARY KEY (`revenue_item_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `revenue_items`
--

INSERT INTO `revenue_items` (`revenue_item_ID`, `revenue_item`) VALUES
('13425', 'Burial Permits and Cemetry'),
('1423001', 'Market Tolls'),
('1423011', 'Reg. of Marriage/Divorce'),
('685894', 'Sub Metro Managed Toilets / Surtax');

-- --------------------------------------------------------

--
-- Table structure for table `target_entries`
--

DROP TABLE IF EXISTS `target_entries`;
CREATE TABLE IF NOT EXISTS `target_entries` (
  `target_revCenter` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `Amount` float NOT NULL,
  `Year` int(11) NOT NULL,
  `target_ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`target_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `target_entries`
--

INSERT INTO `target_entries` (`target_revCenter`, `Amount`, `Year`, `target_ID`) VALUES
('SKYMOUNT', 568876000, 2020, 1),
('SKYMOUNT', 685498000, 2021, 2),
('Marriage Unit', 78784100, 2021, 3),
('DEGEON', 789541, 2022, 4),
('BANTAMA', 1000000, 2021, 5);

-- --------------------------------------------------------

--
-- Table structure for table `value_books_details`
--

DROP TABLE IF EXISTS `value_books_details`;
CREATE TABLE IF NOT EXISTS `value_books_details` (
  `value_book_ID` varchar(50) NOT NULL,
  `value_books` varchar(45) NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`value_book_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `value_books_details`
--

INSERT INTO `value_books_details` (`value_book_ID`, `value_books`, `price`) VALUES
('1', 'Car-Park(GH₵ 2.00)', 2),
('2', 'Car-Park(GH₵ 1.00)', 1),
('3', 'Market-tolls', 1),
('4', 'GCR', 0);

-- --------------------------------------------------------

--
-- Table structure for table `value_books_stock_record`
--

DROP TABLE IF EXISTS `value_books_stock_record`;
CREATE TABLE IF NOT EXISTS `value_books_stock_record` (
  `value_stock_revCenter` varchar(50) NOT NULL,
  `year` int(11) NOT NULL,
  `month` varchar(20) NOT NULL,
  `date` varchar(20) NOT NULL,
  `value_book` varchar(50) NOT NULL,
  `first_serial` int(11) NOT NULL,
  `last_serial` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `amount` float NOT NULL,
  `purchase_amount` float NOT NULL,
  `remarks` varchar(80) NOT NULL,
  `value_record_ID` varchar(50) NOT NULL,
  PRIMARY KEY (`value_record_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `value_books_stock_record`
--

INSERT INTO `value_books_stock_record` (`value_stock_revCenter`, `year`, `month`, `date`, `value_book`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks`, `value_record_ID`) VALUES
('SKYMOUNT', 2021, 'March', '11-03-2021', 'Car-Park(GH₵ 1.00)', 101, 200, 1, 100, 7.8, 'Initial Entry', '1');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cheque_details`
--
ALTER TABLE `cheque_details`
  ADD CONSTRAINT `cheque_details_ibfk_1` FOREIGN KEY (`payment_ID`) REFERENCES `collection_payment_entries` (`pay_ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
