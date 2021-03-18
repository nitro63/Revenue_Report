-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 18, 2021 at 08:35 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `revenue_monitoring`
--

-- --------------------------------------------------------

--
-- Table structure for table `cheque_details`
--

CREATE TABLE `cheque_details` (
  `revCenter` varchar(50) NOT NULL,
  `gcr` int(11) NOT NULL,
  `year` int(11) NOT NULL,
  `month` varchar(20) NOT NULL,
  `date` varchar(20) NOT NULL,
  `cheque_date` varchar(20) NOT NULL,
  `cheque_number` int(11) NOT NULL,
  `bank` varchar(30) NOT NULL,
  `amount` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cheque_details`
--

INSERT INTO `cheque_details` (`revCenter`, `gcr`, `year`, `month`, `date`, `cheque_date`, `cheque_number`, `bank`, `amount`) VALUES
('ABINKYI', 684896, 2021, 'February', '17-03-2021', '17-03-2021', 687788, 'CBG', 8987),
('MANHYIA NORTH', 89632, 2021, 'March', '17-03-2021', '17-03-2021', 5454574, 'GCB', 54544);

-- --------------------------------------------------------

--
-- Table structure for table `collection_payment_entries`
--

CREATE TABLE `collection_payment_entries` (
  `revCenter` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `GCR` int(11) NOT NULL,
  `Amount` float NOT NULL,
  `Date` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `Month` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `Year` int(11) NOT NULL,
  `payment_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `collection_payment_entries`
--

INSERT INTO `collection_payment_entries` (`revCenter`, `GCR`, `Amount`, `Date`, `Month`, `Year`, `payment_type`) VALUES
('Environmental', 352654, 24654, '10-03-2021', 'February', 2021, 'Cheque'),
('MANHYIA NORTH', 45546, 6856, '10-03-2021', 'February', 2021, 'Cheque'),
('SKYMOUNT', 65565, 6565.25, '23-03-2021', 'February', 2021, 'Cheque'),
('SKYMOUNT', 6559, 546596, '17-03-2021', 'January', 2021, 'Cheque'),
('Marriage Unit', 65615, 64646.2, '17-03-2021', 'February', 2021, 'Cheque'),
('ABINKYI', 5446, 62626.6, '11-03-2021', 'February', 2021, 'Cheque'),
('SUBIN', 65155, 541565, '16-03-2021', 'February', 2021, 'Cheque'),
('SUBIN', 4586189, 5959.38, '12-03-2021', 'March', 2021, 'Cheque'),
('SUBIN', 684946, 548.84, '16-03-2021', 'April', 2021, 'Cheque Deposit Slip'),
('SUBIN', 959662, 6566.35, '19-03-2021', 'March', 2021, 'Cheque'),
('BANTAMA', 87921, 48562, '16-03-2021', 'March', 2021, 'Cheque'),
('BANTAMA', 44854, 756456, '11-03-2021', 'March', 2021, 'Cheque'),
('BANTAMA', 458965, 785, '16-03-2021', 'February', 2021, 'Cheque Deposit Slip'),
('BANTAMA', 78165, 515, '16-03-2021', 'January', 2021, 'Cheque'),
('Marriage Unit', 65696, 97646, '17-03-2021', 'March', 2021, 'Cheque'),
('Marriage Unit', 65465, 8945, '25-03-2021', 'March', 2021, 'Cheque Deposit Slip'),
('Marriage Unit', 7986, 656, '17-03-2021', 'March', 2021, 'Cheque'),
(' Licence Unit', 8541248, 5452, '12-03-2021', 'March', 2021, 'Cheque Deposit Slip'),
('GREENFIELD', 896851, 5656, '13-03-2021', 'March', 2021, 'Cheque'),
('ABINKYI', 684896, 7858, '17-03-2021', 'February', 2021, 'Cheque'),
('ABINKYI', 5444244, 5459, '17-03-2021', 'March', 2021, 'Cheque Deposit Slip'),
('MANHYIA SOUTH', 655354, 6554, '16-03-2021', 'March', 2021, 'Cheque'),
('MANHYIA SOUTH', 654656, 4854.27, '16-03-2021', 'March', 2021, 'Cheque'),
('MANHYIA NORTH', 23168, 1563, '17-03-2021', 'March', 2021, 'Cheque'),
('DEGEON', 1424585, 54546, '10-03-2021', 'March', 2021, 'Cheque'),
('DEGEON', 655489, 6261.36, '17-03-2021', 'March', 2021, 'Cheque'),
('MANHYIA NORTH', 89632, 65421, '17-03-2021', 'March', 2021, 'Cheque'),
('MANHYIA NORTH', 9856, 6546, '24-03-2021', 'March', 2021, 'Cheque');

-- --------------------------------------------------------

--
-- Table structure for table `daily_entries`
--

CREATE TABLE `daily_entries` (
  `revCenter` varchar(80) NOT NULL,
  `Code` varchar(20) NOT NULL,
  `revenueItem` varchar(80) NOT NULL,
  `revenueAmount` float NOT NULL,
  `revenueDate` varchar(20) NOT NULL,
  `revenueWeek` int(11) NOT NULL,
  `revenueMonth` varchar(30) NOT NULL,
  `revenueQuarter` int(11) NOT NULL,
  `revenueYear` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `revenue centers`
--

CREATE TABLE `revenue centers` (
  `CenterID` int(11) NOT NULL,
  `revenueCategory` varchar(70) NOT NULL,
  `revenueSubCategory` varchar(70) NOT NULL,
  `revenueItem` varchar(70) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `revenue centers`
--

INSERT INTO `revenue centers` (`CenterID`, `revenueCategory`, `revenueSubCategory`, `revenueItem`) VALUES
(1, 'KMA MAIN', '', 'LICENCE'),
(2, 'KMA MAIN', 'MARRIAGE UNIT', 'REGISTRATION'),
(3, 'KMA MAIN', 'MARRIAGE UNIT', 'SEARCH & PHOTOSHOOT'),
(4, 'KMA MAIN', 'DECONGESTION', 'METRO GUARDS OFFICE'),
(5, 'KMA MAIN', 'DECONGESTION', 'TOWING GUARD'),
(6, 'KMA MAIN', 'WASTE MANAGEMENT DEPARTMENT', 'ADVERTISEMENT(WORKS DEPARTMENT)'),
(7, 'KMA MAIN', 'WASTE MANAGEMENT DEPARTMENT', 'BANNERS (WORKS DEPARTMENT)'),
(8, 'KMA MAIN', 'WASTE MANAGEMENT DEPARTMENT', 'STATUTORY(WORKS DEPARTMENT)'),
(9, 'KMA MAIN', 'ENVIRONMENTAL DEPARTMENT', 'NOISE CONTROL'),
(10, 'KMA MAIN', 'ENVIRONMENTAL DEPARTMENT', 'FOOD VENDORS & SUITABILITY CERTIFICATE'),
(11, 'KMA MAIN', 'URBAN TRANSPORT DEPARTMENT', 'PROPERTY RATE'),
(12, 'KMA MAIN', 'URBAN TRANSPORT DEPARTMENT', 'TENDER DOCUMENT'),
(13, 'KMA MAIN', 'URBAN TRANSPORT DEPARTMENT', 'BUILDING PERMIT FORMS'),
(14, 'KMA MAIN', 'URBAN TRANSPORT DEPARTMENT', 'PROPERTY RATE [BANTAMA| SUBIN| MANHYIA]'),
(15, 'KMA MAIN', 'ESTATE UNIT', 'GOVERNMENT BUNGALOWS & QUATERS'),
(16, 'KMA MAIN', 'ESTATE UNIT', 'JUBILEE PARK'),
(17, 'SUB-METRO', '', 'BANTAMA'),
(18, 'SUB-METRO', '', 'NHYIAESO'),
(19, 'SUB-METRO', '', 'SUBIN'),
(20, 'SUB-METRO', '', 'MANHYIA SOUTH'),
(21, 'SUB-METRO', '', 'MANHYIA NORTH'),
(22, 'MARKETS & LORRY PARKS', '', 'ASAFO MARKET'),
(23, 'MARKETS & LORRY PARKS', '', 'RACE COURSE'),
(24, 'MARKETS & LORRY PARKS', '', 'SOKOBAN WOOD VILLAGE'),
(25, 'OTHER REVENUE', '', 'URBAN ROADS'),
(26, 'OTHER REVENUE', '', 'STOOL LANDS'),
(27, 'OTHER REVENUE', '', 'TOWN & COUNTRY PLANNING'),
(28, 'OUTSOURCED COMPANIES', '', 'GOLD PRINT (RATTARY PARK)'),
(29, 'OUTSOURCED COMPANIES', '', 'GREEN FIELD'),
(30, 'OUTSOURCED COMPANIES', '', 'GOLDSTREET'),
(31, 'OUTSOURCED COMPANIES', 'FIDELITY BANK', 'RENT'),
(32, 'OUTSOURCED COMPANIES', 'FIDELITY BANK', 'MARKET TOLLS(DAILIES)'),
(33, 'OUTSOURCED COMPANIES', '', 'SKYMOUNT CONSULT'),
(34, 'OUTSOURCED COMPANIES', '', 'KMA DAY & NIGHT (TOWING)'),
(35, 'OUTSOURCED COMPANIES', '', 'KOBBY JEI(TOWING)'),
(36, 'OUTSOURCED COMPANIES', '', 'DE-GEON INVESTMENT'),
(37, 'OUTSOURCED COMPANIES', '', 'ACHEAMFOUR TERMINAL'),
(38, 'OUTSOURCED COMPANIES', '', 'MY COMPANY 365(LORRY PARKS)'),
(39, 'OUTSOURCED COMPANIES', '', 'NANA AFIA SERWAA KOBI MARKET'),
(40, 'OUTSOURCED COMPANIES', '', 'AMANSIE GHANA LIMITED'),
(41, 'OUTSOURCED COMPANIES', '', 'PREMPEH ASSEMBLY HALL'),
(42, 'OUTSOURCED COMPANIES', '', 'KUMASI-CITY COMPANY LIMITED');

-- --------------------------------------------------------

--
-- Table structure for table `target_entries`
--

CREATE TABLE `target_entries` (
  `revCenter` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `Amount` float NOT NULL,
  `Year` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Target entries table for target analysis';

--
-- Dumping data for table `target_entries`
--

INSERT INTO `target_entries` (`revCenter`, `Amount`, `Year`) VALUES
('SKYMOUNT', 5698740, 2021),
('BANTAMA', 5634680, 2020),
('SKYMOUNT', 569854000, 2020),
('Marriage Unit', 54744500, 2021);

-- --------------------------------------------------------

--
-- Table structure for table `value_books_stock_record`
--

CREATE TABLE `value_books_stock_record` (
  `revCenter` varchar(30) NOT NULL,
  `year` int(11) NOT NULL,
  `month` varchar(20) NOT NULL,
  `date` varchar(20) NOT NULL,
  `value_book` varchar(50) NOT NULL,
  `first_serial` int(11) NOT NULL,
  `last_serial` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `amount` float NOT NULL,
  `purchase_amount` float NOT NULL,
  `remarks` varchar(80) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `value_books_stock_record`
--

INSERT INTO `value_books_stock_record` (`revCenter`, `year`, `month`, `date`, `value_book`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks`) VALUES
('SUBIN', 0, 'March', '11-03-2021', 'GCR', 2008001, 2008100, 1, 0, 8, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cheque_details`
--
ALTER TABLE `cheque_details`
  ADD UNIQUE KEY `cheque_number` (`cheque_number`);

--
-- Indexes for table `revenue centers`
--
ALTER TABLE `revenue centers`
  ADD PRIMARY KEY (`CenterID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `revenue centers`
--
ALTER TABLE `revenue centers`
  MODIFY `CenterID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=83;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
