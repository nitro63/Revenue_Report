-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 22, 2021 at 11:54 PM
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
-- Table structure for table `access_levels`
--

DROP TABLE IF EXISTS `access_levels`;
CREATE TABLE IF NOT EXISTS `access_levels` (
  `access_ID` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `level` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`access_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `access_levels`
--

INSERT INTO `access_levels` (`access_ID`, `level`) VALUES
('Lvl_1', 'Overall Administrator'),
('Lvl_2', 'Administrator'),
('Lvl_3', 'Accountant'),
('Lvl_4', 'Revenue Supervisor'),
('Lvl_5', 'Data Clerk');

-- --------------------------------------------------------

--
-- Table structure for table `center_items`
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
('K0101', '1422013', '30c2fa78520211436'),
('K0201', '1423001', '9960e712542021444'),
('K0206', '1423001', 'c7890112542021124'),
('K0101', '1423001', 'ff1e09522420212126');

-- --------------------------------------------------------

--
-- Table structure for table `cheque_details`
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

--
-- Dumping data for table `collection_payment_entries`
--

INSERT INTO `collection_payment_entries` (`pay_ID`, `pay_revCenter`, `GCR`, `Amount`, `Date`, `Month`, `Year`, `payment_type`) VALUES
('35cfd180412520213504132', 'K0206', 5421545, 2412, '12-05-2021', 'April', 2021, 'Cash'),
('80696e354125202134938873', 'K0201', 4125455, 4125, '12-05-2021', 'April', 2021, 'Cash'),
('977896d921052021112435225', 'K0101', 1234623, 2312, '11-05-2021', 'April', 2021, 'Cash');

-- --------------------------------------------------------

--
-- Table structure for table `commission_details`
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
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `daily_entries`
--

INSERT INTO `daily_entries` (`daily_revCenter`, `revenueItem`, `revenueAmount`, `revenueDate`, `revenueWeek`, `revenueMonth`, `revenueQuarter`, `revenueYear`, `entries_ID`) VALUES
('K0101', '1423001', 5421, '22-04-2021', 4, 'April', 2, 2021, 31),
('K0206', '1423001', 23000, '15-04-2021', 3, 'April', 2, 2021, 37),
('K0101', '1423001', 57557, '20-04-2021', 4, 'April', 2, 2021, 38),
('K0101', '1423001', 4254, '21-04-2021', 4, 'April', 2, 2021, 39),
('K0101', '1423001', 4547, '25-04-2021', 5, 'April', 2, 2021, 40),
('K0301', '1423001', 56429, '20-04-2021', 4, 'April', 2, 2021, 41),
('K0101', '1422013', 6425, '12-05-2021', 3, 'May', 2, 2021, 42),
('K0101', '1422013', 547, '09-06-2021', 2, 'June', 2, 2021, 43),
('K0201', '1423001', 5256, '11-05-2021', 3, 'May', 2, 2021, 44);

-- --------------------------------------------------------

--
-- Table structure for table `revenue_centers`
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
('004001', 'Mobile Phones & Accessories', 'Licences'),
('004002', 'Mobile Money Vendors', 'Licences'),
('004003', 'Warehouse', 'Licences'),
('004004', 'Wholesale', 'Licences'),
('004005', 'Shopping Mall', 'Licences'),
('004006', 'Night Clubs', 'Licences'),
('004007', 'Art/Handicraft Dealers', 'Licences'),
('004008', 'Cosmetics', 'Licences'),
('004009', 'Cold Storage Facilities', 'Licences'),
('004010', 'Building Materials/ Hardware', 'Licences'),
('004011', 'Book shops/ Stationery/ Office Equipment/ Computer & Accessories etc', 'Licences'),
('004012', 'Gold dealers/ Goldsmith/ Gold Merchants', 'Licences'),
('004013', 'Paint Dealers', 'Licences'),
('004014', 'Boutique/ Second Hand Clothing', 'Licences'),
('004015', 'Rubber/ Plastic Sale Shops', 'Licences'),
('004016', 'Tyre/ Battery Dealers', 'Licences'),
('004017', 'Electrical/ Electronic Appliances', 'Licences'),
('004018', 'Electrical Materials/ Accessories Shops', 'Licences'),
('004019', 'Spare Parts Dealers', 'Licences'),
('004020', 'Aluminium/ Other metal/ Glass fabricators', 'Licences'),
('004021', 'Courier Services', 'Licences'),
('004022', 'Fabric Dealers', 'Licences'),
('004023', 'Agro Chemicals/ Farm Input Dealers/ Machine Dealers', 'Licences'),
('004024', 'Funeral Homes/ Undertakers Licence', 'Licences'),
('004025', 'Bridal Homes', 'Licences'),
('004026', 'Commercial vehicles (Taxi)', 'Licences'),
('004027', 'Commercial vehicles (TOP)', 'Licences'),
('004028', 'Driving Schools', 'Licences'),
('004029', 'Heavy Duty Equipment/ Machinery', 'Licences'),
('004030', 'Signage  Makers/Writers', 'Licences'),
('004031', 'Not-for-profit Organisations', 'Licences'),
('004032', 'Commercialized State Corporations', 'Licences'),
('004033', 'Hospitals/ Clinics/Other Health Facilities', 'Licences'),
('004034', 'Veterinary Services', 'Licences'),
('004035', 'Medical Laboratory Facilities', 'Licences'),
('004036', 'Pharmacies/OTC/Pharmaceutical Companies', 'Licences'),
('004037', 'Herbal Medicine/ Extracts', 'Licences'),
('004038', 'Beer/ Wine/ Spirits/ Soft Drinks', 'Licences'),
('1141110', 'Goods Transporters', 'Licences'),
('1142027', 'Mineral/Sachet Water Producers & Retailers', 'Licences'),
('1412003', 'Stool Lands Revenue', 'Lands & Royalties'),
('1412007', 'Building Plans/Permits', 'Lands & Royalties'),
('1412017', 'Hotels/Guest Houses/Clubs/Rest Houses', 'Licences'),
('1412022', 'Property Rate Arrears', 'Rates'),
('1412023', 'Basic Rates', 'Rates'),
('1415001', 'Metro Bungalows', 'Rent of Land, Building & Houses'),
('1415002', 'Ground Rent - Permit of Temporal Structure/Kiosk', 'Rent of Land, Building & Houses'),
('1415011', 'Other Investment Income - Food Vendor Screening', 'Licences'),
('1415038', 'Rental of Facilities (Assembly Stores/Building)', 'Rent of Land, Building & Houses'),
('1422001', 'Funeral/ Announcement', 'Licences'),
('1422002', 'Herbalist/ Priests', 'Licences'),
('1422003', 'Hawkers', 'Licences'),
('1422004', 'Local Manufacturers (Metal)', 'Licences'),
('1422005', 'Chop Bars', 'Licences'),
('1422006', 'Local Manufacturers (wood) KDown', 'Licences'),
('1422007', 'Timber Industries', 'Licences'),
('1422008', 'Tailors & Seamstress', 'Licences'),
('1422009', 'Bakers', 'Licences'),
('1422010', 'Wheel Trucks/Bicycles/Tricycles/Motorcycles', 'Licences'),
('1422011', 'Self Employed Artisans', 'Licences'),
('1422012', 'Kiosk / Container', 'Licences'),
('1422013', 'Arrears for other Revenue', 'Licences'),
('1422014', 'Firewood/ Charcoal', 'Licences'),
('1422016', 'Restaurants/ Eatries', 'Licences'),
('1422017', 'Betting Centres/Lottery Business', 'Licences'),
('1422020', 'Commercial vehicles (Trotro)', 'Licences'),
('1422025', 'Private Professionals', 'Licences'),
('1422028', 'Telecom System/Security Service/Electronics', 'Licences'),
('1422029', 'Mobile Sale Van', 'Licences'),
('1422030', 'Entertainment Centre', 'Licences'),
('1422032', 'Drinking Spots (Akpeteshie/Spirit Sellers)', 'Licences'),
('1422036', 'Fuel & LPG Dealers', 'Licences'),
('1422038', 'Hair Dressers & Barbers', 'Licences'),
('1422041', 'Taxi Licence/Plates', 'Licences'),
('1422044', 'Financial Institutions', 'Licences'),
('1422047', 'Photographers/ Video Operator', 'Licences'),
('1422048', 'Shoe/ Sandals Makers/ Repairs', 'Licences'),
('1422051', 'Millers', 'Licences'),
('1422054', 'Laundaries/ Car Wash', 'Licences'),
('1422055', 'Printing Press/ Photocopy', 'Licences'),
('1422057', 'Private Schools', 'Licences'),
('1422058', 'Automobile Companies', 'Licences'),
('1422060', 'Airlines/Shiping Agents/Travel & Tour/ Tourism Facilities', 'Licences'),
('1422061', 'Susu Operators/Cooperatives/Credit Unions', 'Licences'),
('1422066', 'Public Letter Writer', 'Licences'),
('1422067', 'Beer Bars', 'Licences'),
('1422072', 'Registration of Contractors/Building/Road', 'Licences'),
('1422159', 'Communications Masts', 'Licences'),
('1423001', 'Market Tolls', 'Fees'),
('1423002', 'Foam/ Mattress', 'Licences'),
('1423025', 'Environmental Health Inspection Cert (Suitability Cert)', 'Licences'),
('1423078', 'Bus. Reg -Sch. Feeding, Info Centres, Other Bus.', 'Licences'),
('1423415', 'Raw Water (Commercial Water Sellers)', 'Licences');

-- --------------------------------------------------------

--
-- Table structure for table `target_entries`
--

DROP TABLE IF EXISTS `target_entries`;
CREATE TABLE IF NOT EXISTS `target_entries` (
  `target_revCenter` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `Amount` float NOT NULL,
  `Year` int(11) NOT NULL,
  `target_ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`target_ID`),
  KEY `target_revCenter` (`target_revCenter`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `target_entries`
--

INSERT INTO `target_entries` (`target_revCenter`, `Amount`, `Year`, `target_ID`) VALUES
('K0101', 4552, 2021, 6),
('K0101', 5878, 2021, 7),
('K0101', 5878, 2021, 8),
('K0101', 42, 2021, 9),
('K0101', 210, 2021, 10),
('K0101', 24, 2021, 11);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `last_name` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(25) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `access_level` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `username` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `center` varchar(70) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`username`),
  KEY `access_level` (`access_level`),
  KEY `center` (`center`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`last_name`, `first_name`, `email`, `password`, `access_level`, `username`, `center`) VALUES
('Frederick', 'Nyenku', 'nitrog@gmail.com', 'admin', 'Lvl_1', 'Admin', NULL),
('Ekow', 'Nitro', 'ekow@gmail.com', '12345', 'Lvl_2', 'Ekow', 'K0206');

-- --------------------------------------------------------

--
-- Table structure for table `usercredent`
--

DROP TABLE IF EXISTS `usercredent`;
CREATE TABLE IF NOT EXISTS `usercredent` (
  `username` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `usercredent`
--

INSERT INTO `usercredent` (`username`, `password`) VALUES
('Admin', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `value_books_details`
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

DROP TABLE IF EXISTS `value_books_stock_record`;
CREATE TABLE IF NOT EXISTS `value_books_stock_record` (
  `value_stock_revCenter` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `year` int(11) NOT NULL,
  `month` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `quarter` int(11) NOT NULL,
  `week` int(11) NOT NULL,
  `date` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value_book` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_serial` int(11) NOT NULL,
  `last_serial` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `amount` float NOT NULL,
  `purchase_amount` float NOT NULL,
  `remarks` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `value_record_ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`value_record_ID`),
  KEY `value_book` (`value_book`),
  KEY `value_stock_revCenter` (`value_stock_revCenter`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `value_books_stock_record`
--

INSERT INTO `value_books_stock_record` (`value_stock_revCenter`, `year`, `month`, `quarter`, `week`, `date`, `value_book`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks`, `value_record_ID`) VALUES
('K0101', 2021, 'May', 2, 3, '10-05-2021', 'VB03', 201001, 201300, 3, 600, 22.5, 'Initial Entry', 1),
('K0101', 2021, 'May', 2, 3, '10-05-2021', 'VB03', 201201, 201400, 2, 400, 15, 'Initial Entry', 2),
('K0101', 2021, 'May', 2, 3, '11-05-2021', 'VB03', 201201, 201500, 3, 600, 22.5, 'Initial Entry', 3),
('K0101', 2021, 'May', 2, 3, '10-05-2021', 'VB03', 201302, 201501, 2, 400, 15, 'Initial Entry', 4),
('K0101', 2021, 'May', 2, 3, '10-05-2021', 'VB03', 201005, 201204, 2, 400, 15, 'Initial Entry', 5);

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
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_2` FOREIGN KEY (`access_level`) REFERENCES `access_levels` (`access_ID`),
  ADD CONSTRAINT `user_ibfk_3` FOREIGN KEY (`center`) REFERENCES `revenue_centers` (`CenterID`);

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
