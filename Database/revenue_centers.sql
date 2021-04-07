-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 07, 2021 at 12:22 AM
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
-- Table structure for table `revenue_centers`
--

CREATE TABLE `revenue_centers` (
  `CenterID` varchar(50) NOT NULL,
  `revenue_category` varchar(70) NOT NULL,
  `revenue_center` varchar(70) NOT NULL
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

--
-- Indexes for dumped tables
--

--
-- Indexes for table `revenue_centers`
--
ALTER TABLE `revenue_centers`
  ADD PRIMARY KEY (`CenterID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
