-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 13, 2017 at 11:46 PM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `company`
--

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

CREATE TABLE IF NOT EXISTS `employee` (
  `Social_Security_Number` varchar(20) NOT NULL,
  `First_Name` varchar(30) NOT NULL,
  `Last_Name` varchar(30) NOT NULL,
  `Salaried` int(1) NOT NULL DEFAULT '0',
  `Hourly` int(1) NOT NULL DEFAULT '0',
  `Commission` int(1) NOT NULL DEFAULT '0',
  `Base_Plus_Commission` int(1) NOT NULL DEFAULT '0',
  `Weekly_Salary` double NOT NULL DEFAULT '0',
  `Hourly_Wage` double NOT NULL DEFAULT '0',
  `Commission_Rate` double NOT NULL DEFAULT '0',
  `Base_Salary` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`Social_Security_Number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `employee`
--

INSERT INTO `employee` (`Social_Security_Number`, `First_Name`, `Last_Name`, `Salaried`, `Hourly`, `Commission`, `Base_Plus_Commission`, `Weekly_Salary`, `Hourly_Wage`, `Commission_Rate`, `Base_Salary`) VALUES
('11-11-111', 'Charinda', 'Dissanayake', 1, 0, 0, 0, 5000, 0, 0, 0),
('11-11-112', 'Dian', 'Jayasuriya', 1, 0, 0, 0, 4500, 0, 0, 0),
('11-11-113', 'Neluk', 'Fernando', 1, 0, 0, 0, 4250, 0, 0, 0),
('22-22-222', 'Dinith', 'Arunasiri', 0, 1, 0, 0, 0, 250, 0, 0),
('22-22-223', 'Ashane', 'Gamage', 0, 1, 0, 0, 0, 225, 0, 0),
('22-22-224', 'Sanuka', 'Ranganath', 0, 1, 0, 0, 0, 200, 0, 0),
('33-33-333', 'Lahiru', 'Upasena', 0, 0, 1, 0, 0, 0, 0.5, 0),
('33-33-334', 'Randula', 'Samarasekara', 0, 0, 1, 0, 0, 0, 0.45, 0),
('33-33-335', 'Manoj', 'Silva', 0, 0, 1, 0, 0, 0, 0.4, 0),
('44-44-444', 'Manula', 'Gajadeera', 0, 0, 0, 1, 0, 0, 0.3, 7000),
('44-44-445', 'Kusal', 'Hettiarachchi', 0, 0, 0, 1, 0, 0, 0.25, 6500),
('44-44-446', 'Sasith', 'Peries', 0, 0, 0, 1, 0, 0, 0.2, 6000);

-- --------------------------------------------------------

--
-- Table structure for table `salary`
--

CREATE TABLE IF NOT EXISTS `salary` (
  `Social_Security_Number` varchar(20) NOT NULL,
  `Date` date NOT NULL,
  `Week` int(5) NOT NULL,
  `Gross_Sales` double NOT NULL DEFAULT '0',
  `Hours_Worked` double NOT NULL DEFAULT '0',
  `Earnings` double NOT NULL,
  PRIMARY KEY (`Social_Security_Number`,`Date`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `salary`
--

INSERT INTO `salary` (`Social_Security_Number`, `Date`, `Week`, `Gross_Sales`, `Hours_Worked`, `Earnings`) VALUES
('11-11-111', '2017-01-13', 2, 0, 0, 5000),
('11-11-112', '2017-01-13', 2, 0, 0, 4500),
('11-11-113', '2017-01-13', 2, 0, 0, 4250),
('22-22-222', '2017-01-13', 2, 0, 45, 11875),
('22-22-223', '2017-01-13', 2, 0, 47, 11362.5),
('22-22-224', '2017-01-13', 2, 0, 49, 10700),
('33-33-333', '2017-01-13', 2, 20000, 0, 10000),
('33-33-334', '2017-01-13', 2, 19000, 0, 8550),
('33-33-335', '2017-01-13', 2, 21000, 0, 8400),
('44-44-444', '2017-01-13', 2, 25000, 0, 15200),
('44-44-445', '2017-01-14', 2, 15000, 0, 10900),
('44-44-446', '2017-01-14', 2, 12000, 0, 9000);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `Username` varchar(30) NOT NULL,
  `Password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`Username`, `Password`) VALUES
('admin', '1111');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `salary`
--
ALTER TABLE `salary`
  ADD CONSTRAINT `employee_salary_fk` FOREIGN KEY (`Social_Security_Number`) REFERENCES `employee` (`Social_Security_Number`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
